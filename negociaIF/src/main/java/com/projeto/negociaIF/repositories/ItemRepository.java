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

    List<Item> findByCategoriaAndNomeIgnoreCaseContaining(Categoria categoria, String nomeCategoria);

}
