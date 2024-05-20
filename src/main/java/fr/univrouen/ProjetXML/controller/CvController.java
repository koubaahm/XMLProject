package fr.univrouen.ProjetXML.controller;

import fr.univrouen.ProjetXML.dtos.CV24Dto;
import fr.univrouen.ProjetXML.entities.CV24;
import fr.univrouen.ProjetXML.services.BindingXmlService;
import fr.univrouen.ProjetXML.services.CvService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cv24")
@Api(value = "Gestion des CVs", tags = "Endpoints pour les CVs ")
public class CvController {

    private final CvService cvService;
    private final BindingXmlService bindingXmlService;

    /**
     * Affiche la liste des CVs.
     * @param model Le modèle pour ajouter les attributs.
     * @return La vue des CVs.
     */
    @ApiOperation(value = "Afficher la liste des CVs", response = List.class)
    @GetMapping("/resume")
    public String getAllCv24(Model model) {
        List<CV24> cvList = cvService.getAllCv24();
        model.addAttribute("CV24", cvList);
        return "cvs";
    }

    /**
     * Renvoie la liste des CVs en format XML.
     * @return La liste des CVs en XML.
     */
    @ApiOperation(value = "Obtenir la liste des CVs en format XML", response = List.class)
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<CV24Dto>> getAllCvSummariesAsXml() {
        List<CV24Dto> cvXMLS = cvService.getAllCvXMl();
        return ResponseEntity.ok(cvXMLS);
    }

    /**
     * Affiche un CV spécifique par ID.
     * @param id L'ID du CV.
     * @param model Le modèle pour ajouter les attributs.
     * @return La vue du détail du CV.
     */
    @ApiOperation(value = "Afficher un CV spécifique par ID", response = String.class)
    @GetMapping("/{id}")
    public String getCvById(@PathVariable Long id, Model model) {
        try {
            File xsltFile = new ClassPathResource("cv24.tp4.xslt").getFile();
            if (!xsltFile.exists()) {
                throw new IOException("XSLT file not found at specified path");
            }

            CV24 cv = cvService.getCvById(id);
            String xmlData = bindingXmlService.convertEntityToXml(cv);
            String htmlData = bindingXmlService.applyXsltTransformation(xmlData, xsltFile);

            System.out.println("HTML Content of cvDisplay view: \n" + htmlData);

            model.addAttribute("cvHtml", htmlData);
            return "cvDetail";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }

    /**
     * Renvoie le contenu HTML d'un CV spécifique par ID.
     * @param id L'ID du CV.
     * @return Le contenu HTML du CV.
     */
    @ApiOperation(value = "Obtenir le contenu HTML d'un CV spécifique par ID", response = String.class)
    @GetMapping("/html/{id}")
    public ResponseEntity<String> getCvHtmlById(@PathVariable Long id) {
        return cvService.getCvHtmlById(id);
    }

    /**
     * Renvoie le contenu XML d'un CV spécifique par ID.
     * @param id L'ID du CV.
     * @return Le contenu XML du CV.
     */
    @ApiOperation(value = "Obtenir le contenu XML d'un CV spécifique par ID", response = String.class)
    @GetMapping("/xml/{id}")
    public ResponseEntity<String> getCvXmlById(@PathVariable Long id) {
        return cvService.getCvXmlById(id);
    }

    /**
     * Sauvegarde un CV à partir d'un fichier téléchargé.
     * @param file Le fichier téléchargé contenant le CV en XML.
     * @param redirectAttributes Les attributs de redirection pour les messages.
     * @return La redirection vers la vue des CVs ou la page d'erreur.
     */
    @ApiOperation(value = "Sauvegarder un CV à partir d'un fichier téléchargé", response = String.class)
    @PostMapping
    public String saveCv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le fichier est vide.");
                return "redirect:/error";
            }

            String xmlData = new String(file.getBytes(), StandardCharsets.UTF_8);
            ResponseEntity<String> response = cvService.saveCv(xmlData);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/cv24/resume";
            } else {
                redirectAttributes.addFlashAttribute("error", response.getBody());
                return "redirect:/error";
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la lecture du fichier.");
            return "redirect:/error";
        }
    }

    /**
     * Met à jour un CV spécifique par ID.
     * @param id L'ID du CV.
     * @param xmlData Les données XML du CV.
     * @return La réponse indiquant le succès ou l'échec de la mise à jour.
     */
    @ApiOperation(value = "Mettre à jour un CV spécifique par ID", response = ResponseEntity.class)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCv(@PathVariable Long id, @RequestBody String xmlData) {
        return cvService.updateCv(id, xmlData);
    }

    /**
     * Supprime un CV spécifique par ID.
     * @param id L'ID du CV.
     * @return La réponse indiquant le succès de la suppression.
     */
    @ApiOperation(value = "Supprimer un CV spécifique par ID", response = ResponseEntity.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCv(@PathVariable Long id) {
        cvService.deleteCv(id);
        return ResponseEntity.ok("CV supprimé avec succès !");
    }
}
