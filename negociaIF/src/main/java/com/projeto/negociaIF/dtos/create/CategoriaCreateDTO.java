package com.projeto.negociaIF.dtos.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaCreateDTO {

    @NotBlank(message = "O nome é obrigatório.")
    String nome;
}
