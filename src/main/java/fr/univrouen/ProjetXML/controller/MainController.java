package fr.univrouen.ProjetXML.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Api(value = "Gestion de la page d'accueil", tags = "Endpoints pour l'entree de l'application")
public class MainController {

    /**
     * Redirige vers la page de connexion.
     *
     * @return ModelAndView Redirection vers la page de connexion
     */
    @ApiOperation(value = "Redirige vers la page de connexion", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 302, message = "Redirection vers la page de connexion")
    })
    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("redirect:/auth/login");
    }

    /**
     * Affiche la page d'accueil avec les informations du projet.
     *
     * @param model Model utilisé pour transmettre les données à la vue
     * @return String Nom de la vue d'accueil
     */
    @ApiOperation(value = "Affiche la page d'accueil avec les informations du projet", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Affiche la page d'accueil avec succès")
    })
    @GetMapping("/acceuil")
    public String acceuil(Model model) {

        model.addAttribute("projectName", "Projet XML");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("teamMembers", new String[]{"Koubaa & Ahmed", "Ait Hmadouch & Rania"});
        model.addAttribute("universityLogo", "/images/logo-universite-de-rouen-normandie.png");
        return "acceuil";
    }
}