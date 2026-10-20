package practice.mayank.co_stream.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import practice.mayank.co_stream.entity.User;

public class IdAuthentication extends UsernamePasswordAuthenticationToken {

    public IdAuthentication(UserDetails userDetails) {
        super(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public String getName() {
        return ((User) getPrincipal()).getId();
    }
}