package com.projeto.negociaIF.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(precision=10, scale=2)
    private BigDecimal preco;

    @Lob
    private byte[] foto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusItem statusItem;

    @ManyToOne
    @JoinColumn(name = "id_destaque", nullable = true)
    private DestaqueItem destaque;

    @PrePersist
    public void prePersist(){
        if(dataCadastro == null){
            dataCadastro = LocalDateTime.now();
        }
    }


}
