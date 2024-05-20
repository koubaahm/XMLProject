package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.dtos.CV24Dto;
import fr.univrouen.ProjetXML.entities.CV24;
import fr.univrouen.ProjetXML.entities.Diplome;
import fr.univrouen.ProjetXML.repository.CV24Repository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CvService {

    private final CV24Repository cv24Repository;
    private final BindingXmlService bindingXmlService;


    public List<CV24> getAllCv24() {
        return cv24Repository.findAll();
    }


    public CV24 getCvById(Long id) {
        return cv24Repository.findById(id).orElse(null);
    }

    public ResponseEntity<String> getCvHtmlById(Long id) {
        try {
            CV24 cv = getCvById(id);

            if (cv == null) {
                String errorMessage = "<html><body><h1>Erreur</h1><p>CV avec l'identifiant " + id + " introuvable.</p></body></html>";
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }

            String xmlData = bindingXmlService.convertEntityToXml(cv);


            File xsltFile = new ClassPathResource("cv24.tp4.xslt").getFile();
            String htmlData = bindingXmlService.applyXsltTransformation(xmlData, xsltFile);


            return new ResponseEntity<>(htmlData, HttpStatus.OK);
        } catch (Exception e) {

            String errorMessage = "<html><body><h1>Erreur</h1><p>Une erreur s'est produite lors du traitement du CV.</p></body></html>";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> saveCv(String xmlData) {
        try {

            CV24 cv = bindingXmlService.convertXmlToEntity(xmlData);


            if (cv.getObjectif() == null || cv.getObjectif().getStatus() == null) {
                throw new RuntimeException("L'objectif ou le statut est nul après la désérialisation.");
            }


            cv24Repository.save(cv);

            return ResponseEntity.ok("CV enregistré avec succès !");
        } catch (JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la conversion du XML en entité.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de l'enregistrement du CV.");
        }
    }


    public ResponseEntity<String> updateCv(Long id, String xmlData) {
        try {

            if (!cv24Repository.existsById(id)) {
                return ResponseEntity.badRequest().body("CV non trouvé !");
            }


            CV24 cvToUpdate = bindingXmlService.convertXmlToEntity(xmlData);
            cvToUpdate.setId(id);


            if (cvToUpdate.getObjectif() == null || cvToUpdate.getObjectif().getStatus() == null) {
                throw new RuntimeException("L'objectif ou le statut est nul après la désérialisation.");
            }


            cv24Repository.save(cvToUpdate);

            return ResponseEntity.ok("CV mis à jour avec succès !");
        } catch (JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la conversion du XML en entité.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la mise à jour du CV.");
        }
    }


    public List<CV24Dto> getAllCvXMl() {
        List<CV24> cvList = cv24Repository.findAll();
        return cvList.stream().map(this::createCvXMl).collect(Collectors.toList());
    }

    private CV24Dto createCvXMl(CV24 cv) {
        CV24Dto cv24Dto = new CV24Dto();
        cv24Dto.setId(cv.getId());
        cv24Dto.setGenre(cv.getIdentite().getGenre());
        cv24Dto.setNom(cv.getIdentite().getNom());
        cv24Dto.setPrenom(cv.getIdentite().getPrenom());
        cv24Dto.setStatus(cv.getObjectif().getStatus());


        Optional<Diplome> diplomeOptional = cv.getCompetence().getDiplomes().stream()
                .max(Comparator.comparing(Diplome::getDate));

        diplomeOptional.ifPresent(diplome -> {
            cv24Dto.setDiplomeInstitut(diplome.getInstitut());
            cv24Dto.setDiplomeNiveau(diplome.getNiveau());
        });

        return cv24Dto;
    }

    public ResponseEntity<String> getCvXmlById(Long id) {
        try {

            CV24 cv = getCvById(id);

            if (cv == null) {

                String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                        "    <id>" + id + "</id>\n" +
                        "    <status>ERROR</status>\n" +
                        "</cv24>";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            String xmlData = bindingXmlService.convertEntityToXml(cv);

            return ResponseEntity.ok(xmlData);
        } catch (Exception e) {

            String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                    "    <id>" + id + "</id>\n" +
                    "    <status>ERROR</status>\n" +
                    "</cv24>";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    public ResponseEntity<String> deleteCv(Long id) {
        try {
            if (cv24Repository.existsById(id)) {
                cv24Repository.deleteById(id);
                String successMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                        "    <id>" + id + "</id>\n" +
                        "    <status>DELETED</status>\n" +
                        "</cv24>";
                return ResponseEntity.ok(successMessage);
            } else {
                String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                        "    <id>" + id + "</id>\n" +
                        "    <status>ERROR</status>\n" +
                        "</cv24>";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } catch (Exception e) {
            String errorMessage = "<cv24 xmlns=\"http://univ.fr/cv24\">\n" +
                    "    <id>" + id + "</id>\n" +
                    "    <status>ERROR</status>\n" +
                    "</cv24>";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


}
