package com.projeto.negociaIF.repositories;

import com.projeto.negociaIF.model.Interesse;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse,Long> {

    /*Usuario findByUsuario(Usuario usuario);

    Item findByItem(Item item);

    boolean existsByUsuarioAndItem(Usuario usuario, Item item);

    Long findByItemUsuario(Long idDono);*/

}
