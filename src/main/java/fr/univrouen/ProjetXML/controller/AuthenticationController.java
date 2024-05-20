package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.dtos.LoginUserDto;
import fr.univrouen.ProjetXML.dtos.RegisterUserDto;
import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.services.AuthenticationService;
import fr.univrouen.ProjetXML.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/auth")
@Controller
@AllArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @GetMapping("/signup")
    public ModelAndView showSignupForm() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        return new ModelAndView("login");
    }




    @PostMapping("/signup")
    public RedirectView register(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        RegisterUserDto registerUserDto = new RegisterUserDto(fullName, email, password);
        User registeredUser = authenticationService.signup(registerUserDto);
        String redirectUrl = "/cv/all";
        return new RedirectView(redirectUrl);
    }


 /*   @PostMapping("/login")
    public ResponseEntity<LoginReponse> authenticate(@RequestParam String email, @RequestParam String password) {
        LoginUserDto loginUserDto = new LoginUserDto(email, password);
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginReponse loginResponse = LoginReponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
    */



    @PostMapping("/login")
    public RedirectView authenticate(@RequestParam String email, @RequestParam String password) {
        LoginUserDto loginUserDto = new LoginUserDto(email, password);
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        // Générer le token JWT et configurer la réponse de redirection
        String jwtToken = jwtService.generateToken(authenticatedUser);
        String redirectUrl = "/cv/all";
        return new RedirectView(redirectUrl);
    }


}
