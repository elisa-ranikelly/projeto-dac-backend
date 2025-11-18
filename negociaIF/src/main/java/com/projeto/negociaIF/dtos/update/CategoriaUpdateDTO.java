package com.projeto.negociaIF.dtos.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaUpdateDTO {
    @NotBlank(message = "O nome é obrigatório.")
    String nome;
}
