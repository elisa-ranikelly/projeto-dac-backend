package com.projeto.negociaIF.services;

import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.enums.StatusDisponibilidade;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.CategoriaRepository;
import com.projeto.negociaIF.repositories.FotoRepository;
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

    @Autowired
    private FotoRepository  fotoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

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

        if(item.getFotos() == null || item.getFotos().isEmpty()){
            throw new RegraNegocioObrigacaoException("O item deve conter pelo menos uma foto.");
        }

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.VENDIDO ||
        item.getStatusDisponibilidade() == StatusDisponibilidade.TROCADO){
            throw new RegraNegocioObrigacaoException("Um item recém-criado não pode estar marcado como vendido ou trocado.");
        }

        List<FotoItem> fotos = item.getFotos();

        item.setUsuario(usuario);
        item.setCategoria(categoria);
        item.setFotos(null);
        item.setStatusAprovacao(StatusAprovacao.PENDENTE);
        item.setMotivoReprovacao(null);

        Item novoItem = itemRepository.save(item);

        for(FotoItem foto : fotos){
            if(foto.getUrl() == null || foto.getUrl().isBlank()){
                throw new RegraNegocioObrigacaoException("A URL da foto não pode ser vazia.");
            }

            foto.setItem(novoItem);
            fotoRepository.save(foto);
        }

        novoItem.setFotos(fotos);
        return novoItem;
    }

    public List<Item> listarItemPorCategoria(Long idCategoria){
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);
        return itemRepository.findByCategoria(categoria);
    }

    public List<Item> listarItensPendentes(){
        return itemRepository.findByStatusAprovacao(StatusAprovacao.PENDENTE);
    }

    public Item atualizarItem(Long id, Item itemAtualizado){

        Item itemAtual = buscarItemPorId(id);

        itemAtual.setNome(itemAtualizado.getNome());
        itemAtual.setDescricao(itemAtualizado.getDescricao());
        itemAtual.setStatusDisponibilidade(itemAtualizado.getStatusDisponibilidade());
        itemAtual.setPreco(itemAtualizado.getPreco());

        if(itemAtual.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_TROCA){
            itemAtual.setPreco(null);
        }

        if(itemAtual.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_VENDA && itemAtual.getPreco() == null){
            throw new RegraNegocioObrigacaoException("O preço do item para vendas é obrigatório.");
        }

        if(itemAtual.getStatusAprovacao() == StatusAprovacao.REPROVADO){
            itemAtual.setStatusAprovacao(StatusAprovacao.PENDENTE);
            itemAtual.setMotivoReprovacao(null);
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
        item.setMotivoReprovacao(null);
        return itemRepository.save(item);
    }

    public Item reprovarItem(Long id, String motivo){
        Item item = buscarItemPorId(id);
        item.setMotivoReprovacao(motivo);
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

}
