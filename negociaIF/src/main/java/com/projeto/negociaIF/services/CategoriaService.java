package com.projeto.negociaIF.services;

import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria buscarCategoriaPorId(Long id) {
       return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria com id " + " não encontrada."));
    }
}
