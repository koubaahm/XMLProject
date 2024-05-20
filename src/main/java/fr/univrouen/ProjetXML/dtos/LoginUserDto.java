package fr.univrouen.ProjetXML.dtos;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoginUserDto {

    private String email;

    private String password;
}