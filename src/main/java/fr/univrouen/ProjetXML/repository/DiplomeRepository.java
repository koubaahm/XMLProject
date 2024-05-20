package fr.univrouen.ProjetXML.repository;

import fr.univrouen.ProjetXML.entities.Diplome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;


@RepositoryRestController
public interface DiplomeRepository extends JpaRepository<Diplome,Long> {
}
