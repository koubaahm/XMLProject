package fr.univrouen.ProjetXML.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import fr.univrouen.ProjetXML.entities.CV24;
import fr.univrouen.ProjetXML.services.BindingXmlService;
import fr.univrouen.ProjetXML.services.CvService;
import lombok.AllArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;



/**
 * contrôleur responsable de la gestion des requêtes liées aux CV.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/cv")
public class CvController {

    private final CvService cvService;
    private final BindingXmlService bindingXmlService;


    /**
     * récupère tous les CV.
     *
     * @return une liste de tous les CV
     */
    @GetMapping("/all")
    public String getAllCv24(Model model) {
        // Récupérer tous les CVs du service
        List<CV24> cvList = cvService.getAllCv24();

        // Ajouter la liste de CVs au modèle pour l'utiliser dans la vue
        model.addAttribute("CV24", cvList);

        // Retourner le nom de la vue Thymeleaf
        return "cvs";
    }

    /**
     * récupère un CV par son ID.
     *
     * @param id    l'ID du CV à récupérer
     * @param model le modèle pour la vue
     * @return le nom de la vue à afficher
     */

    @GetMapping("/{id}")
    public String getCvById(@PathVariable Long id, Model model) {
        try {
            // Chemin absolu vers le fichier XSLT
            File xsltFile = new ClassPathResource("cv24.tp4.xslt").getFile();

            // Vérifie que la ressource XSLT existe
            if (!xsltFile.exists()) {
                throw new IOException("XSLT file not found at specified path");
            }

            // Trouve le CV par son ID ou lance une exception s'il n'est pas trouvé
            CV24 cv = cvService.getCvById(id);

            // Convertit l'entité CV en données XML
            String xmlData = bindingXmlService.convertEntityToXml(cv);

            // Applique la transformation XSLT
            String htmlData = bindingXmlService.applyXsltTransformation(xmlData, xsltFile);

            // Affiche le contenu HTML de la vue cvDisplay dans la console
            System.out.println("HTML Content of cvDisplay view: \n" + htmlData);

            // Ajoute le contenu HTML au modèle pour qu'il soit affiché dans la vue
            model.addAttribute("cvHtml", htmlData);

            // Retourne le nom de la vue à afficher
            return "cvDetail";
        } catch (Exception e) {
            // Gère les exceptions et affiche une page d'erreur si nécessaire
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // Retourne le nom de la vue d'erreur
        }
    }



    /**
     * enregistre un nouveau CV.
     *
     * @param file le XML représentant le CV à enregistrer
     * @return une réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @PostMapping
    public String saveCv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le fichier est vide
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le fichier est vide.");
                return "redirect:/error"; // Redirection vers une page d'erreur
            }

            // Convertir le fichier MultipartFile en une chaîne de caractères
            String xmlData = new String(file.getBytes(), StandardCharsets.UTF_8);

            // Appeler la méthode saveCv du service en lui passant les données XML
            ResponseEntity<String> response = cvService.saveCv(xmlData);

            // Vérifier la réponse
            if (response.getStatusCode().is2xxSuccessful()) {
                // Redirection vers la vue des CVs après un ajout réussi
                return "redirect:/cv/all"; // Redirection vers la vue de tous les CVs
            } else {
                redirectAttributes.addFlashAttribute("error", response.getBody());
                return "redirect:/error"; // Redirection vers une page d'erreur en cas d'échec
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la lecture du fichier.");
            return "redirect:/error"; // Redirection vers une page d'erreur en cas d'exception
        }
    }




    /**
     * met à jour un CV existant.
     *
     * @param id      l'ID du CV à mettre à jour
     * @param xmlData le XML représentant le CV mis à jour
     * @return une réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCv(@PathVariable Long id, @RequestBody String xmlData) {
        return cvService.updateCv(id, xmlData);
    }

    /**
     * supprime un CV par son ID.
     *
     * @param id l'ID du CV à supprimer
     * @return une réponse HTTP indiquant le succès ou l'échec de l'opération
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCv(@PathVariable Long id) {
        cvService.deleteCv(id);
        return ResponseEntity.ok("CV supprimé avec succès !");
    }


}
