package fr.univrouen.ProjetXML.dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginReponse {

    @Getter
    private String token;

    private long expiresIn;

}

