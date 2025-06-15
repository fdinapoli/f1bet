package com.sporty.f1bet.repository;

import com.sporty.f1bet.model.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    Optional<Bet> findByAppUserIdAndSessionKey(Long userId, Long sessionKey);

    List<Bet> findAllBySessionKey(Long sessionKey);
}