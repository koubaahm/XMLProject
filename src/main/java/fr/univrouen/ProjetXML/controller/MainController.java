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
        model.addAttribute("projectName", "Projet XML");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("teamMembers", new String[]{"Koubaa & Ahmed", "Ait Hmadouch & Rania"});
        model.addAttribute("universityLogo", "/images/logo-universite-de-rouen-normandie.png");
        return "acceuil";
    }
}
