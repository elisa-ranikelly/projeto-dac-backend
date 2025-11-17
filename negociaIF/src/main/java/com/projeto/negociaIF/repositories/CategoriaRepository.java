package com.projeto.negociaIF.repositories;

import com.projeto.negociaIF.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    boolean existsByNomeIgnoreCase(String nome);

}
