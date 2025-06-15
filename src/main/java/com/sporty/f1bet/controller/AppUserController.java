package com.sporty.f1bet.controller;

import com.sporty.f1bet.model.entity.AppUser;
import com.sporty.f1bet.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        return ResponseEntity.ok(appUserService.createUser(appUser));
    }

}
