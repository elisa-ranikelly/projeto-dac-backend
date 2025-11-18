package com.projeto.negociaIF.dtos.response;

import com.projeto.negociaIF.enums.StatusDisponibilidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponseDTO {

    String nome;
    String descricao;
    BigDecimal preco;
    StatusDisponibilidade statusDisponibilidade;
    String categoria;
    List<FotoResponseDTO> fotos;
}
