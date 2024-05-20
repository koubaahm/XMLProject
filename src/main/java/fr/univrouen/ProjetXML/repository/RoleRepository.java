package fr.univrouen.ProjetXML.repository;

import fr.univrouen.ProjetXML.entities.Role;
import fr.univrouen.ProjetXML.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum roleEnum);
}