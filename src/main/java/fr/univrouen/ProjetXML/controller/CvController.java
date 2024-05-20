package fr.univrouen.ProjetXML.controller;


import fr.univrouen.ProjetXML.dtos.CV24Dto;

import org.springframework.http.MediaType;
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




@Controller
@AllArgsConstructor
@RequestMapping("/cv24")
public class CvController {

    private final CvService cvService;
    private final BindingXmlService bindingXmlService;


    @GetMapping("/resume")
    public String getAllCv24(Model model) {

        List<CV24> cvList = cvService.getAllCv24();

        model.addAttribute("CV24", cvList);

        return "cvs";
    }

    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<CV24Dto>> getAllCvSummariesAsXml() {
        List<CV24Dto> cvXMLS = cvService.getAllCvXMl();
        return ResponseEntity.ok(cvXMLS);
    }


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

    @GetMapping("/html/{id}")
    public ResponseEntity<String> getCvHtmlById(@PathVariable Long id) {
        return cvService.getCvHtmlById(id);
    }


    @GetMapping("/xml/{id}")
    public ResponseEntity<String> getCvXmlById(@PathVariable Long id) {
        return cvService.getCvXmlById(id);
    }


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


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCv(@PathVariable Long id, @RequestBody String xmlData) {
        return cvService.updateCv(id, xmlData);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCv(@PathVariable Long id) {
        cvService.deleteCv(id);
        return ResponseEntity.ok("CV supprimé avec succès !");
    }


}
