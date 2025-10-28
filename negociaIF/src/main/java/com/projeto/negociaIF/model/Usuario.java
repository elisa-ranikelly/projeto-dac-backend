package com.projeto.negociaIF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="usuarios")
public class Usuario extends Pessoa{

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable=false)
    private String tipo;

    @Column(nullable=false)
    private String telefone;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> itens = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        if(dataCadastro == null){
            dataCadastro = LocalDateTime.now();
        }
    }
}
