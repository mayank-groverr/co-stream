package practice.mayank.co_stream.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.exception.customexceptions.ResourceNotFoundException;
import practice.mayank.co_stream.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.orElseThrow(() -> new ResourceNotFoundException("No user found with this email" + email));
    }
}
