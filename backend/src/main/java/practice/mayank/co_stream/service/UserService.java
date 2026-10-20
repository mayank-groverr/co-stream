package practice.mayank.co_stream.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.mayank.co_stream.dto.user.LoginRequest;
import practice.mayank.co_stream.dto.user.PasswordRequestDto;
import practice.mayank.co_stream.dto.user.UserRequest;
import practice.mayank.co_stream.dto.user.UserResponse;
import practice.mayank.co_stream.entity.User;
import practice.mayank.co_stream.exception.customexceptions.BadRequestException;
import practice.mayank.co_stream.exception.customexceptions.ResourceNotFoundException;
import practice.mayank.co_stream.mapper.UserMapper;
import practice.mayank.co_stream.repository.UserRepository;
import practice.mayank.co_stream.security.entity.RoleEnum;
import practice.mayank.co_stream.security.services.RoleService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final MongoTemplateService mongoTemplateService;

    @Transactional
    public UserResponse getUser(){
        User authenticatedUser = getAuthenticatedUser();
        return userMapper.userToUserResponse(authenticatedUser);
    }


    @Transactional
    public UserResponse createNewUser(UserRequest userRequest){
        User user = userMapper.userRequestToUser(userRequest);
        roleService.assignRole(user, RoleEnum.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        return userMapper.userToUserResponse(save);
    }


    @Transactional
    public String updatePassword(PasswordRequestDto passwordRequestDto){
        User authenticatedUser = getAuthenticatedUser();
        if(passwordEncoder.matches(passwordRequestDto.oldPassword(), authenticatedUser.getPassword())){
            mongoTemplateService.runUpdateCriteria(
                    Criteria.where("_id").is(authenticatedUser.getId()),
                    new Update().set("password" , passwordEncoder.encode(passwordRequestDto.newPassword())),
                    User.class
            );
            return "Password update successfully";
        }
        throw new BadRequestException("Incorrect old password");
    }

    @Transactional
    public void deleteUser(){
        User authenticatedUser = getAuthenticatedUser();
        userRepository.delete(authenticatedUser);
    }

    public User findUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new ResourceNotFoundException("No user found with email:" + email));
    }

    public static User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public User authenticate(LoginRequest loginRequest) {
        Authentication authenticateUser =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.userEmail(),
                                loginRequest.password()
                        )
                );
        return (User) authenticateUser.getPrincipal();
    }


    public List<User> findAllById(List<String> userIds) {
        return userRepository.findAllById(userIds);
    }
}
