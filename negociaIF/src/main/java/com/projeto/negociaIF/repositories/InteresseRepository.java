/*package com.projeto.negociaIF.repositories;

import com.projeto.negociaIF.model.Interesse;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse,Long> {

    boolean existsByUsuarioAndItem(Usuario usuario, Item item);

    List<Interesse> findByUsuario(Usuario usuario);

    List<Interesse> findByItem(Item item);

}*/
