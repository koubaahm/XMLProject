package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.dtos.LoginUserDto;
import fr.univrouen.ProjetXML.dtos.RegisterUserDto;
import fr.univrouen.ProjetXML.entities.Role;
import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.enums.RoleEnum;
import fr.univrouen.ProjetXML.repository.RoleRepository;
import fr.univrouen.ProjetXML.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;



    public User signup(RegisterUserDto input) {
        // Vérifiez que le champ "fullName" de l'objet RegisterUserDto n'est pas nul ou vide
        if (input.getFullName() == null || input.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }

        // Récupérer le rôle par défaut depuis la base de données (ou le créer si nécessaire)
        Role defaultRole = roleRepository.findByName(RoleEnum.USER)
                .orElseGet(() -> {
                    Role role = new Role(RoleEnum.USER);
                    return roleRepository.save(role);
                });

        // Créer un nouvel utilisateur avec le rôle par défaut
        User user = User.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(defaultRole)
                .build();

        // Enregistrer l'utilisateur dans la base de données
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
