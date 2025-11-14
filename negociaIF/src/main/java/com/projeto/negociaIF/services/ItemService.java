package com.projeto.negociaIF.services;

import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.enums.StatusDisponibilidade;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    public Item buscarItemPorId(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item com id " + id + " não encontrado."));
    }

    public Item criarItem(Item item, Long idUsuarioDono, Long idCategoria){

        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuarioDono);
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_VENDA && item.getPreco() == null){
            throw new RegraNegocioObrigacaoException("O preço do item para vendas é obrigatório.");
        }

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_TROCA){
            item.setPreco(null);
        }

        item.setUsuario(usuario);
        item.setCategoria(categoria);
        item.setStatusAprovacao(StatusAprovacao.PENDENTE);

        return itemRepository.save(item);
    }

    public List<Item> listarItemPorCategoria(Long idCategoria){
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);
        return itemRepository.findByCategoria(categoria);
    }

    public List<Item> buscarItemComFiltros(Long idCategoria, String nome){
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);
        return itemRepository.findByCategoriaAndNomeIgnoreCaseContaining(categoria, nome);
    }

    public Item atualizarItem(Long id, Item itemAtualizado){

        Item itemAtual = buscarItemPorId(id);

        itemAtual.setNome(itemAtualizado.getNome());
        itemAtual.setDescricao(itemAtualizado.getDescricao());
        itemAtual.setFotos(itemAtualizado.getFotos());
        itemAtual.setStatusDisponibilidade(itemAtualizado.getStatusDisponibilidade());
        itemAtual.setPreco(itemAtualizado.getPreco());

        if(itemAtual.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_TROCA){
            itemAtual.setPreco(null);
        }

        if(itemAtual.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_VENDA && itemAtual.getPreco() == null){
            throw new RegraNegocioObrigacaoException("O preço do item para vendas é obrigatório.");
        }

        return itemRepository.save(itemAtual);
    }

    public void excluirItem(Long id){
        Item  item = buscarItemPorId(id);
        itemRepository.deleteById(id);
    }

    public Item aprovarItem(Long id){
        Item item = buscarItemPorId(id);
        item.setStatusAprovacao(StatusAprovacao.APROVADO);
        return itemRepository.save(item);
    }

    public Item reprovarItem(Long id){
        Item item = buscarItemPorId(id);
        item.setStatusAprovacao(StatusAprovacao.REPROVADO);
        return itemRepository.save(item);
    }

    public Item marcarItemComoVendido(Long id){
        Item item = buscarItemPorId(id);
        item.setStatusDisponibilidade(StatusDisponibilidade.VENDIDO);
        return itemRepository.save(item);
    }

    public Item marcarItemComoTrocado(Long id){
        Item item = buscarItemPorId(id);
        item.setStatusDisponibilidade(StatusDisponibilidade.TROCADO);
        return itemRepository.save(item);
    }

    public Item marcarItemParaVenda(Long id){
        Item item = buscarItemPorId(id);
        item.setStatusDisponibilidade(StatusDisponibilidade.DISPONIVEL_VENDA);
        return itemRepository.save(item);
    }

    public Item marcarItemParaTroca(Long id){
        Item item = buscarItemPorId(id);
        item.setStatusDisponibilidade(StatusDisponibilidade.DISPONIVEL_TROCA);
        item.setPreco(null);
        return itemRepository.save(item);
    }

}
