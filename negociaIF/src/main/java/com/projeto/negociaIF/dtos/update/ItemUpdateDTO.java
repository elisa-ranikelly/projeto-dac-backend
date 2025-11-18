package com.projeto.negociaIF.dtos.update;

import com.projeto.negociaIF.enums.StatusDisponibilidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemUpdateDTO {

    @NotBlank(message = "O nome do item é obrigatório.")
    String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    String descricao;

    BigDecimal preco;

    @NotNull(message = "A disponibilidade do item é obrigatória.")
    StatusDisponibilidade statusDisponibilidade;
}
