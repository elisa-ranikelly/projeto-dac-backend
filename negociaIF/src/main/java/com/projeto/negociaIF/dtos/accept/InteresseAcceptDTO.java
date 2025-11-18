package com.projeto.negociaIF.dtos.accept;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InteresseAcceptDTO {

    @NotNull(message = "O ID do interesse é obrigatório.")
    Long id;

    @NotNull(message = "O ID do usuário dono do item é obrigatório.")
    Long idUsuarioDono;
}
