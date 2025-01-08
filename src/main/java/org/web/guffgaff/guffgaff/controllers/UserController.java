package org.web.guffgaff.guffgaff.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final List<String> user = new ArrayList<String>();

    @GetMapping("/users")
    public List<String> UserName() {

        return user;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username) {
        if (!user.contains(username)) {
            user.add(username);
        }
        return ResponseEntity.ok("User registered: " + username);
    }
}
