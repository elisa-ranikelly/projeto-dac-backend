package com.projeto.negociaIF.dtos.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MotivoReprovacaoCreateDTO {

    @NotBlank(message = "O motivo da reprovação é obrigatória.")
    private String motivoReprovacao;
}
