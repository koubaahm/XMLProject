package fr.univrouen.ProjetXML.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterUserDto {

    private String fullName;

    private String email;

    private String password;


}
