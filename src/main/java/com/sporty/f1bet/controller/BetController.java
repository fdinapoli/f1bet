package com.sporty.f1bet.controller;

import com.sporty.f1bet.model.dto.BetRequest;
import com.sporty.f1bet.model.entity.Bet;
import com.sporty.f1bet.service.BetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    public ResponseEntity<Bet> placeBet(@RequestBody BetRequest request) {
        Bet bet = betService.createBet(request.getUserId(), request.getSessionKey(), request.getDriverNumber(), request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(bet);
    }
}