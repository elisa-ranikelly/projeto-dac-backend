package com.projeto.negociaIF.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaResponseDTO {

    Long id;
    String nome;
    List<ItemResponseDTO> itens;
}
