package com.projeto.negociaIF.dtos.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FotoCreateDTO {

    @NotBlank(message = "A URL da foto é obrigatória.")
    String url;
}
