package com.projeto.negociaIF.dtos.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InteresseCreateDTO {

    @NotNull(message = "O ID do usuário interessado é obrigatório.")
    Long idUsuarioInteressado;

    @NotNull(message = "O ID do item é obrigatório.")
    Long idItem;
}
