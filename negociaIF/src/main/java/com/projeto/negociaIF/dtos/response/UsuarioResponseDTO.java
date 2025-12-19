package com.projeto.negociaIF.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioResponseDTO {

    Long id;
    String nome;
    String email;
    String telefone;
    LocalDateTime dataCadastro;

    Set<String> roles;
}
