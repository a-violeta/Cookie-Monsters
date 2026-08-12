package com.app.security;

import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // fetch your custom User entity from the database
        User appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // reject deleted accounts here so this covers BOTH the login flow (via
        // AuthenticationManager) AND every subsequent authenticated request, since
        // JwtAuthenticationFilter calls this method on every request with a JWT -
        // a still-valid token for a just-deleted account gets rejected on its very
        // next use, not just at login
        if (appUser.isDeleted()) {
            throw new UsernameNotFoundException("This account has been deleted");
        }

        /*  convert it to a Spring Security UserDetails object
            we pass an empty list of authorities (roles) for now, as we just need basic authentication
        */
        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                new ArrayList<>()
        );
    }
}