package com.sporty.f1bet.service;

import com.sporty.f1bet.model.entity.AppUser;
import com.sporty.f1bet.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser createUser(AppUser appUser) {
        appUser.setBalance(100.0);
        return appUserRepository.save(appUser);
    }
}
