package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
@Api(value = "Gestion des utilisateurs", tags = "endpoints pour la gestion des utilisateurs")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les informations sur l'utilisateur authentifié.
     *
     * @return ResponseEntity<User> Les informations sur l'utilisateur authentifié
     */
    @ApiOperation(value = "Récupère les informations sur l'utilisateur authentifié", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Les informations de l'utilisateur authentifié ont été récupérées avec succès"),
            @ApiResponse(code = 401, message = "Utilisateur non authentifié")
    })
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return ResponseEntity<List<User>> La liste de tous les utilisateurs
     */
    @ApiOperation(value = "Récupère la liste de tous les utilisateurs", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste de tous les utilisateurs a été récupérée avec succès"),
            @ApiResponse(code = 403, message = "Accès refusé")
    })
    @GetMapping("/")

    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }
}