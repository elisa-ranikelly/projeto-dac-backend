package com.projeto.negociaIF.dtos.create;

import com.projeto.negociaIF.enums.StatusDisponibilidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemCreateDTO {

    @NotBlank(message = "O nome do item é obrigatório.")
    String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    String descricao;

    BigDecimal preco;

    @NotNull(message = "A disponibilidade do item é obrigatória.")
    StatusDisponibilidade statusDisponibilidade;

    @NotNull(message = "O id da categoria é obrigatório.")
    Long idCategoria;

    @NotEmpty(message = "É obrigatório informar pelo menos uma foto.")
    List<String> fotos;
}
