package org.web.guffgaff.guffgaff.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.web.guffgaff.guffgaff.dto.LoginDTO;
import org.web.guffgaff.guffgaff.dto.LoginResponse;
import org.web.guffgaff.guffgaff.entities.User;
import org.web.guffgaff.guffgaff.models.LoginForm;
import org.web.guffgaff.guffgaff.repo.UserRepo;
import org.web.guffgaff.guffgaff.utils.JWTUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    private final List<String> user = new ArrayList<String>();

    @Autowired
    private PasswordEncoder bEncoder;

    @GetMapping("/register")
    public String userRegistration(Model model) {
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);
        return "signup";
    }
    @PostMapping("/do-register")
    public String doRegister(@ModelAttribute LoginForm loginForm) {
        User user = new User();
        user.setUsername(loginForm.getUsername());

        user.setPassword(bEncoder.encode(loginForm.getPassword()));
        user.setEmail(loginForm.getEmail());
        user.setPhone(loginForm.getPhone());
        user.setName(loginForm.getName());
        userRepo.save(user);
        return "login";
    }


    @ResponseBody
    @GetMapping("/users")
    public List<String> UserName(Principal principal) {
        List<String> usernames = userRepo.findAllUsernames(principal.getName());
        return usernames;
    }

    @GetMapping("/login")
    public String login() {
    return "login";

    }

    @ResponseBody
    @PostMapping("/api/v1/authenticate")
    public ResponseEntity<?> loginResponse(@RequestBody LoginDTO loginDTO) {
        System.out.println("Inside Login response "+loginDTO);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            System.out.println("Inside Login response "+ authentication.getName());
            String token = jwtUtils.generateToken(authentication.getName());
            System.out.println("Inside Login response "+ token);
            return ResponseEntity.ok(new LoginResponse(token,"Successfull",loginDTO.getUsername()));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
