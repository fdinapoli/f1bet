package com.sporty.f1bet.service;

import com.sporty.f1bet.model.dto.DriverDto;
import com.sporty.f1bet.model.dto.SessionDto;
import com.sporty.f1bet.model.entity.AppUser;
import com.sporty.f1bet.model.entity.Bet;
import com.sporty.f1bet.model.entity.DriverOdds;
import com.sporty.f1bet.repository.AppUserRepository;
import com.sporty.f1bet.repository.BetRepository;
import com.sporty.f1bet.repository.DriverOddsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private static final List<Integer> ODDS_POSSIBLE_VALUES = List.of(2, 3, 4);
    private final F1ApiClient f1ApiClient;
    private final DriverOddsRepository driverOddsRepository;
    private final BetRepository betRepository;
    private final AppUserRepository appUserRepository;
    private final Random random = new Random();

    public SessionService(F1ApiClient f1ApiClient, DriverOddsRepository driverOddsRepository, BetRepository betRepository, AppUserRepository appUserRepository) {
        this.f1ApiClient = f1ApiClient;
        this.driverOddsRepository = driverOddsRepository;
        this.betRepository = betRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<SessionDto> getSessions(String country, String sessionType, Integer year) {
        List<SessionDto> sessions = f1ApiClient.getSessions(country, sessionType, year);
        if (sessions.isEmpty()) {
            return List.of();
        }

        List<Long> sessionKeys = sessions.stream()
                .map(SessionDto::getSessionKey)
                .filter(Objects::nonNull)
                .toList();

        List<DriverDto> allDrivers = sessionKeys.stream()
                .map(f1ApiClient::getDrivers)
                .flatMap(List::stream)
                .toList();

        List<DriverOdds> existingOdds = driverOddsRepository.findBySessionKeyIn(sessionKeys);

        Map<Long, Map<Long, DriverOdds>> oddsMap = new HashMap<>();
        for (DriverOdds odds : existingOdds) {
            oddsMap
                    .computeIfAbsent(odds.getSessionKey(), k -> new HashMap<>())
                    .put(odds.getDriverNumber(), odds);
        }

        for (DriverDto driver : allDrivers) {
            Long sessionKey = driver.getSessionKey();
            Long driverNumber = driver.getDriverNumber();
            Map<Long, DriverOdds> sessionOdds = oddsMap.computeIfAbsent(sessionKey, k -> new HashMap<>());
            DriverOdds odds = sessionOdds.get(driverNumber);

            if (odds == null) {
                Integer newOdds = ODDS_POSSIBLE_VALUES.get(random.nextInt(ODDS_POSSIBLE_VALUES.size()));
                odds = new DriverOdds();
                odds.setSessionKey(sessionKey);
                odds.setDriverNumber(driverNumber);
                odds.setOdds(newOdds);
                driverOddsRepository.save(odds);
                sessionOdds.put(driverNumber, odds);
            }

            driver.setOdds(odds.getOdds());
        }

        Map<Long, List<DriverDto>> driversBySession = allDrivers.stream()
                .collect(Collectors.groupingBy(DriverDto::getSessionKey));

        for (SessionDto session : sessions) {
            List<DriverDto> drivers = driversBySession.getOrDefault(session.getSessionKey(), List.of());
            session.setDrivers(drivers);
        }

        return sessions;
    }

    public void closeSession(Long sessionKey, Long winningDriverNumber) {
        List<Bet> bets = betRepository.findAllBySessionKey(sessionKey);

        for (Bet bet : bets) {
            if (bet.getClosed()) continue;

            boolean won = bet.getDriverNumber().equals(winningDriverNumber);
            bet.setClosed(true);

            if (won) {
                int odds = driverOddsRepository.findBySessionKeyAndDriverNumber(sessionKey, winningDriverNumber).getOdds();
                double reward = bet.getAmount() * odds;

                AppUser user = bet.getAppUser();
                user.setBalance(user.getBalance() + reward);
                appUserRepository.save(user);
            }

            betRepository.save(bet);
        }
    }
}
