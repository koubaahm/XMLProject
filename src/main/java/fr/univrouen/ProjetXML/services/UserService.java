package fr.univrouen.ProjetXML.services;

import fr.univrouen.ProjetXML.entities.User;
import fr.univrouen.ProjetXML.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public  class  UserService {
    private  final UserRepository userRepository ;

    public  UserService (UserRepository userRepository) {
        this .userRepository = userRepository ;
    }

    public List<User> allUsers () {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
}
