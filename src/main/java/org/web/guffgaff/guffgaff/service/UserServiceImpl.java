package org.web.guffgaff.guffgaff.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.web.guffgaff.guffgaff.entities.User;
import org.web.guffgaff.guffgaff.repo.UserRepo;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("inside USer servcie IMPl :" + username);
        User user = userRepo.findByUsername(username);
        System.out.println("From Repo :"+user.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("username not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

    }
}
