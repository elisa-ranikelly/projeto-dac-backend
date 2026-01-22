package com.projeto.negociaIF.dtos.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioUpdateDTO {

    @NotBlank(message = "O nome é obrigatório.")
    String nome;

    @NotBlank(message = "O email é obrigatório.")
    String email;

    String senha;
    
    String telefone;
}
