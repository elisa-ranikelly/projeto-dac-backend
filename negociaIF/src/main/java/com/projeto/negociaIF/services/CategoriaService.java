package com.projeto.negociaIF.services;

import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria buscarCategoriaPorId(Long id) {
       return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria com id " + id + " não encontrada."));
    }

    public Categoria criarCategoria(Categoria categoria){
        if(categoriaRepository.existsByNomeIgnoreCase(categoria.getNome())){
            throw new RegraNegocioObrigacaoException("Já existe uma categoria cadastrada com esse nome.");
        }

        return  categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategorias(){
        return categoriaRepository.findAll();
    }

    public Categoria atualizarCategoria(Long id, Categoria categoriaAtualizada){
        Categoria categoria =  buscarCategoriaPorId(id);

        if(!categoria.getNome().equals(categoriaAtualizada.getNome())){
            if(categoriaRepository.existsByNomeIgnoreCase(categoriaAtualizada.getNome())){
                throw new RegraNegocioObrigacaoException("Já existe uma categoria cadastrada com esse nome.");
            }
        }

        categoria.setNome(categoriaAtualizada.getNome());
        return categoriaRepository.save(categoria);
    }

    public void excluirCategoria(Long id){
        Categoria categoria =  buscarCategoriaPorId(id);
        if(categoria.getItens() != null && !categoria.getItens().isEmpty()){
            throw new RegraNegocioObrigacaoException("Essa categoria não pode ser excluída, pois há itens vinculados a ela.");
        }
        categoriaRepository.deleteById(id);
    }
}
