package pl.wisniea.restmvc_web.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wisniea.restmvc_data.exceptions.UserServiceException;
import pl.wisniea.restmvc_web.filters.JwtUtils;
import pl.wisniea.restmvc_data.request.UserLoginRequest;
import pl.wisniea.restmvc_data.services.UserService;

import javax.servlet.http.HttpServletResponse;

import static pl.wisniea.restmvc_data.exceptions.ErrorMessages.AUTHENTICATION_FAILED;


@RestController
@RequestMapping("/rest")
public class LoginRestController {


    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public LoginRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) throws UserServiceException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));


        } catch (BadCredentialsException e) {
            throw new UserServiceException(AUTHENTICATION_FAILED.getErrorMessage());
        }

        final UserDetails userDetails = userService.loadUserByUsername(userLoginRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails.getUsername());

        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok(jwt);
    }

}
