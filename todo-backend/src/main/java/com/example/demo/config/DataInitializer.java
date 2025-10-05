package com.example.demo.config;

import com.example.demo.entities.Role;
import com.example.demo.entities.RoleType;
import com.example.demo.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public abstract class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository){

        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args){

        if (roleRepository.findByName(RoleType.ADMIN).isEmpty()){
            roleRepository.save(new Role(RoleType.ADMIN));
        }
        if (roleRepository.findByName(RoleType.USER).isEmpty()){
            roleRepository.save(new Role(RoleType.USER));
        }
    }
}
