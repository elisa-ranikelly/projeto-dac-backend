package com.projeto.negociaIF.datainitializer;

import com.projeto.negociaIF.model.Role;
import com.projeto.negociaIF.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init(){
        if(!roleRepository.existsByNome("ADMIN")){
            roleRepository.save(new Role(null, "ADMIN"));
        }

        if(!roleRepository.existsByNome("USER")){
            roleRepository.save(new Role(null, "USER"));
        }
    }
}
