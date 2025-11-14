package com.projeto.negociaIF.repositories;

import com.projeto.negociaIF.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    boolean existsByCategoria(String nome);

    String findByCategoria(String nome);

    String findByNomeContainingIgnoreCase(String nome);

    boolean existsByItenscategoria(Long idCategoria);

}
