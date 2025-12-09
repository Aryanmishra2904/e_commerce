package com.aryan.e_commerce.test;

import com.aryan.e_commerce.user.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dbtest")
    public String testDB() {
        long count = userRepository.count();  // count documents in "users" collection
        return "MongoDB connected successfully! User count = " + count;
    }
}
