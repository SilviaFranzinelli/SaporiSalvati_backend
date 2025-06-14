package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.Role;
import it.epicode.SaporiSalvati.model.User;
import it.epicode.SaporiSalvati.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private UserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        Optional<User> adminUser = appUserService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        }

        // Creazione dell'utente user se non esiste
        Optional<User> normalUser = appUserService.findByUsername("user");
        if (normalUser.isEmpty()) {
            appUserService.registerUser("user", "userpwd", Set.of(Role.ROLE_USER));
        }

        // Creazione dell'utente seller se non esiste
        Optional<User> normalSeller = appUserService.findByUsername("seller");
        if (normalUser.isEmpty()) {
            appUserService.registerUser("seller", "sellerpwd", Set.of(Role.ROLE_SELLER));
        }


    }
}
