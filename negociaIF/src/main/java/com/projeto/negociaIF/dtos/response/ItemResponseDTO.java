package com.projeto.negociaIF.dtos.response;

import com.projeto.negociaIF.enums.StatusAprovacao;
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
    Long id;
    String nome;
    String descricao;
    BigDecimal preco;
    StatusDisponibilidade statusDisponibilidade;
    StatusAprovacao statusAprovacao;
    String motivoReprovacao;
    String categoria;
    String telefone;
    List<FotoResponseDTO> fotos;
}
