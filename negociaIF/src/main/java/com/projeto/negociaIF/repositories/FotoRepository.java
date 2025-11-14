package com.projeto.negociaIF.repositories;

import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepository extends JpaRepository<FotoItem,Long> {

    /*Item findByItem(Item item);*/
}
