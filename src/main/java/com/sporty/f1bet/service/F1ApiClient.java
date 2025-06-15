package com.sporty.f1bet.service;

import com.sporty.f1bet.model.dto.DriverDto;
import com.sporty.f1bet.model.dto.SessionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "f1ApiClient", url = "https://api.openf1.org/v1")
public interface F1ApiClient {

    @GetMapping("/sessions")
    List<SessionDto> getSessions(
            @RequestParam(value = "country_name", required = false) String country,
            @RequestParam(value = "session_name", required = false) String session,
            @RequestParam(value = "year", required = false) Integer year
    );

    @GetMapping("/drivers")
    List<DriverDto> getDrivers(
            @RequestParam(value = "session_key", required = false) Long session
    );
}