package com.projeto.negociaIF.model;

import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.enums.StatusDisponibilidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="itens")
public class Item {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = true, precision=10, scale=2)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    StatusAprovacao  statusAprovacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    StatusDisponibilidade  statusDisponibilidade;

    @Column(nullable = true)
    private String motivoReprovacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<FotoItem> fotos = new ArrayList<>();

    /*@OneToMany(mappedBy = "item", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Interesse>  interesses = new ArrayList<>();*/

    @PrePersist
    public void prePersist(){
        if(dataCadastro == null){
            dataCadastro = LocalDateTime.now();
        }
    }
}
