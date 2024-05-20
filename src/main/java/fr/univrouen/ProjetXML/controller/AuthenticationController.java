package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.dtos.LoginUserDto;
import fr.univrouen.ProjetXML.dtos.RegisterUserDto;
import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.services.AuthenticationService;
import fr.univrouen.ProjetXML.services.JwtService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
@Api(tags = "Gestion d'authentification", value = "Endpoints pour l'authentification des utilisateurs")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Affiche le formulaire d'inscription.
     *
     * @return ModelAndView Le modèle et la vue pour le formulaire d'inscription
     */
    @ApiOperation(value = "Afficher le formulaire d'inscription", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Formulaire d'inscription affiché avec succès")
    })
    @GetMapping("/signup")
    public ModelAndView showSignupForm() {
        return new ModelAndView("signup");
    }

    /**
     * Affiche le formulaire de connexion.
     *
     * @return ModelAndView Le modèle et la vue pour le formulaire de connexion
     */
    @ApiOperation(value = "Afficher le formulaire de connexion", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Formulaire de connexion affiché avec succès")
    })
    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        return new ModelAndView("login");
    }

    /**
     * Inscription d'un nouvel utilisateur.
     *
     * @param fullName Nom complet de l'utilisateur
     * @param email    Adresse email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return RedirectView Redirection vers la page d'accueil après inscription
     */
    @ApiOperation(value = "Inscription d'un nouvel utilisateur", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Utilisateur inscrit avec succès"),
            @ApiResponse(code = 400, message = "Erreur lors de l'inscription de l'utilisateur")
    })
    @PostMapping("/signup")
    public RedirectView register(
            @ApiParam(value = "Nom complet de l'utilisateur", required = true) @RequestParam String fullName,
            @ApiParam(value = "Adresse email de l'utilisateur", required = true) @RequestParam String email,
            @ApiParam(value = "Mot de passe de l'utilisateur", required = true) @RequestParam String password) {
        RegisterUserDto registerUserDto = new RegisterUserDto(fullName, email, password);
        User registeredUser = authenticationService.signup(registerUserDto);
        String redirectUrl = "/acceuil";
        return new RedirectView(redirectUrl);
    }

    /**
     * Authentification de l'utilisateur.
     *
     * @param email    Adresse email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return RedirectView Redirection vers la page d'accueil après connexion
     */
    @ApiOperation(value = "Authentification de l'utilisateur", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Utilisateur authentifié avec succès"),
            @ApiResponse(code = 401, message = "Erreur d'authentification de l'utilisateur")
    })
    @PostMapping("/login")
    public RedirectView authenticate(
            @ApiParam(value = "Adresse email de l'utilisateur", required = true) @RequestParam String email,
            @ApiParam(value = "Mot de passe de l'utilisateur", required = true) @RequestParam String password) {
        LoginUserDto loginUserDto = new LoginUserDto(email, password);
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        String redirectUrl = "/acceuil";
        return new RedirectView(redirectUrl);
    }
}