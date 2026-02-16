package com.ecom.my_ecom.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecom.my_ecom.entities.myentity;
import com.ecom.my_ecom.productrepository.Repository;

@Component
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private Repository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.findByusername("admin@fitkart.com").isEmpty()) {
            myentity admin = new myentity();
            admin.setName("Admin");
            admin.setGmail("admin@fitkart.com");
            admin.setUsername("admin@fitkart.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepo.save(admin);
            System.out.println("✅ Admin user created: admin@fitkart.com / admin123");
        }
    }
}
