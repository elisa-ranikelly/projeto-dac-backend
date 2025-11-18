package com.projeto.negociaIF.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InteresseResponseDTO {
    Long id;
    Long idUsuarioInteressado;
    Long idItem;
    boolean aceito;
}
