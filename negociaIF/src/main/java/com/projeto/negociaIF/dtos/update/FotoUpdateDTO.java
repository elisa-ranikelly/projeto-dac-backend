package com.projeto.negociaIF.dtos.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FotoUpdateDTO {

    @NotBlank(message = "A URL da foto é obrigatória")
    String url;
}
