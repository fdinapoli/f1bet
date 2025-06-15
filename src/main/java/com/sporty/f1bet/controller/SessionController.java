package com.sporty.f1bet.controller;

import com.sporty.f1bet.model.dto.SessionDto;
import com.sporty.f1bet.model.dto.SessionResultDto;
import com.sporty.f1bet.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<SessionDto> getSessions(
            @RequestParam(value = "country_name", required = false) String countryName,
            @RequestParam(value = "session_name", required = false) String sessionName,
            @RequestParam(value = "year", required = false) Integer year
    ) {
        return sessionService.getSessions(countryName, sessionName, year);
    }


    @PostMapping("/close")
    public ResponseEntity<Void> closeSession(@RequestBody SessionResultDto result) {
        sessionService.closeSession(result.getSessionKey(), result.getWinningDriverNumber());
        return ResponseEntity.ok().build();
    }
}
