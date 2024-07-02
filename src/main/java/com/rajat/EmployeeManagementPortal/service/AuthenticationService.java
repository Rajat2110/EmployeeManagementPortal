package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.LoginRequest;
import com.rajat.EmployeeManagementPortal.request.RegisterRequest;
import com.rajat.EmployeeManagementPortal.response.AuthenticationResponse;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

/**
 * Service method for authentication operations
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * Register a new user
     * @param request The details required for registering a new user
     * @return AuthenticationResponse containing jwt and role
     */
    public AuthenticationResponse register(RegisterRequest request) {
        var newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setName(request.getName());
        newUser.setContact(request.getContact());
        newUser.setGender(request.getGender());
        newUser.setDateOfBirth(request.getDateOfBirth());
        newUser.setRole(request.getRole());

        var savedUser = userRepository.save(newUser);

        var jwtToken = jwtService.generateToken(newUser);
        return new AuthenticationResponse(jwtToken, savedUser.getRole());
    }

    /**
     * Logs in a user
     * @param request LoginRequest containing email and password for login
     * @return Jwt token and user role on successful authentication
     */
    public AuthenticationResponse login(LoginRequest request) {
        try {
            String decryptedPassword = decrypt(request.getPassword());
            //try to authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            decryptedPassword
                    )
            );
            //find user using email. If not found throw exception
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(jwtToken, user.getRole());
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the profile of a user
     * @return ProfileResponse with user profile details
     */
    public ProfileResponse userProfile() {
        //get the user principal using authentication context
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //build the profile response using found details and return
        ProfileResponse response = new ProfileResponse();
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setContact(user.getContact());
        response.setGender(user.getGender());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setRole(user.getRole());

        return response;
    }

    public String decrypt(String encryptedText) throws Exception {
        String encryptionKey = "1234567890abcdef";
        String iv = "abcdef1234567890";
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
