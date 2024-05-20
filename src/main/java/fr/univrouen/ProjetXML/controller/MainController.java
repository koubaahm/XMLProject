package fr.univrouen.ProjetXML.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("redirect:/auth/login");
    }

    @GetMapping("/acceuil")
    public String home(Model model) {
        model.addAttribute("projectName", "Nom du Projet");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("teamMembers", new String[]{"Nom & Prénom 1", "Nom & Prénom 2", "Nom & Prénom 3"});
        model.addAttribute("universityLogo", "/images/logo_rouen.png"); // Assurez-vous que le chemin vers le logo est correct
        return "acceuil";
    }
}
