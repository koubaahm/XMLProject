package fr.univrouen.ProjetXML.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "Détails de l'utilisateur pour l'authentification")
public class LoginUserDto {
    @ApiModelProperty(value = "Adresse email de l'utilisateur", required = true)
    private String email;

    @ApiModelProperty(value = "Mot de passe de l'utilisateur", required = true)
    private String password;
}