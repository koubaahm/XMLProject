package fr.univrouen.ProjetXML.dtos;

import fr.univrouen.ProjetXML.entities.Diplome;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "cv24Dto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CV24Dto {

    private Long id;
    private String genre;
    private String nom;
    private String prenom;
    private String status;
    private String diplomeInstitut;
    private Integer diplomeNiveau;


}
