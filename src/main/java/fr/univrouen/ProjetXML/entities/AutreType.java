package fr.univrouen.ProjetXML.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import javax.xml.bind.annotation.*;


@Entity
@Table(name = "autre")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor@NoArgsConstructor
public class AutreType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;

    @ManyToOne
    @JoinColumn(name = "divers_id")
    @XmlTransient
    private Divers divers;

    @XmlAttribute(required = true)
    @Pattern(regexp = "^[a-zA-Z \\-'']*$")
    private String titre;

    @XmlAttribute
    @Pattern(regexp = "[a-zA-Z0-9\\s.,:;~@\\(\\)\\-'_!?$*=]{0,128}")
    private String comment; // Optional
}

