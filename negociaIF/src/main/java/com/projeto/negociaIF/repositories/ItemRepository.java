package com.projeto.negociaIF.repositories;

import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.enums.StatusDisponibilidade;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findByCategoria(Categoria categoria);

    List<Item> findByStatusAprovacao(StatusAprovacao statusAprovacao);

    boolean existsByNomeIgnoreCaseAndDescricaoIgnoreCaseAndUsuario_IdAndCategoria_Id(String nome, String descricao, Long idUsuarioDono, Long idCategoria);

    List<Item> findByNomeContainingIgnoreCaseAndCategoriaId(String nome, Long idCategoria);

    List<Item> findByUsuario_Id(Long idUsuario);

    List<Item> findByStatusAprovacaoAndStatusDisponibilidadeIn(StatusAprovacao statusAprovacao, List<StatusDisponibilidade> statusDisponibilidade);

    List<Item> findByStatusAprovacaoAndStatusDisponibilidadeInAndCategoriaId(StatusAprovacao statusAprovacao, List<StatusDisponibilidade> statusDisponibilidade, Long idCategoria);

    List<Item> findByStatusAprovacaoAndStatusDisponibilidadeInAndCategoriaIdAndNomeContainingIgnoreCase(
            StatusAprovacao status,
            List<StatusDisponibilidade> disponibilidades,
            Long categoriaId,
            String nome
    );

    List<Item> findByStatusAprovacaoAndStatusDisponibilidadeInAndNomeContainingIgnoreCase(StatusAprovacao statusAprovacao, List<StatusDisponibilidade> statusDisponibilidades, String nome);


}
