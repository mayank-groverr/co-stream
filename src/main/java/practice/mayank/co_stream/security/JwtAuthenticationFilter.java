package practice.mayank.co_stream.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.mayank.co_stream.exception.ErrorResponseUtil;
import practice.mayank.co_stream.security.services.JwtService;
import practice.mayank.co_stream.security.services.CustomUserDetailService;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull  HttpServletResponse response,
           @NonNull  FilterChain filterChain) throws ServletException, IOException {



        // Authorization : Bearer
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

            
        String token = authorization.substring(7);
        Claims claim;

        try{
            //Extracting Claims
            claim = jwtService.extractAllClaims(token);
        }catch(ExpiredJwtException | MalformedJwtException | SignatureException ex) {


            ProblemDetail problemDetail = ErrorResponseUtil.of(
                    ex.getMessage(),
                    "Jwt Verification Failed",
                    HttpStatus.UNAUTHORIZED,
                    new ServletWebRequest(request, response)
            );

            // Writing Failure Response
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
            return;
        }

        String email = claim.getSubject();
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request,response);

    }

}

