package com.sporty.f1bet.service;

import com.sporty.f1bet.model.entity.AppUser;
import com.sporty.f1bet.model.entity.Bet;
import com.sporty.f1bet.repository.AppUserRepository;
import com.sporty.f1bet.repository.BetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BetService {
    private final BetRepository betRepository;
    private final AppUserRepository userRepository;

    public BetService(BetRepository betRepository, AppUserRepository userRepository) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
    }

    public Bet createBet(Long userId, Long sessionKey, Long driverNumber, Double amount) {
        if (betRepository.findByAppUserIdAndSessionKey(userId, sessionKey).isPresent()) {
            throw new IllegalStateException("User already placed a bet for this session.");
        }

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        double newBalance = user.getBalance() - amount;
        if (newBalance < 0) {
            throw new IllegalArgumentException("User doesn't have the balance to place this bet");
        }

        Bet bet = new Bet();
        bet.setAppUser(user);
        bet.setSessionKey(sessionKey);
        bet.setDriverNumber(driverNumber);
        bet.setAmount(amount);
        bet.setClosed(false);

        user.setBalance(newBalance);
        userRepository.save(user);

        return betRepository.save(bet);
    }
}
