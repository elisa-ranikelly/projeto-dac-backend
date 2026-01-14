package com.projeto.negociaIF.services;

import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.enums.StatusDisponibilidade;
import com.projeto.negociaIF.exceptions.DuplicateFieldException;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.CategoriaRepository;
import com.projeto.negociaIF.repositories.FotoRepository;
import com.projeto.negociaIF.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Autowired
    private FotoService fotoService;

    public Item buscarItemPorId(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item com id " + id + " não encontrado."));
    }

    @Transactional
    public Item criarItem(Item item, Long idUsuarioDono, List<MultipartFile> fotos, Long idCategoria) throws IOException {

        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuarioDono);
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);

        if(itemRepository.existsByNomeIgnoreCaseAndDescricaoIgnoreCaseAndUsuario_IdAndCategoria_Id(item.getNome(), item.getDescricao(), idUsuarioDono, idCategoria)){
            throw new DuplicateFieldException("Esse item já foi criado. Utilize outro nome e outra descrição.");
        }

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_VENDA && item.getPreco() == null){
            throw new RegraNegocioObrigacaoException("O preço do item para vendas é obrigatório.");
        }

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_TROCA){
            item.setPreco(null);
        }

        if(fotos == null || fotos.isEmpty()){
            throw new RegraNegocioObrigacaoException("O item deve conter pelo menos uma foto.");
        }

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.VENDIDO ||
        item.getStatusDisponibilidade() == StatusDisponibilidade.TROCADO){
            throw new RegraNegocioObrigacaoException("Um item recém-criado não pode estar marcado como vendido ou trocado.");
        }

        item.setUsuario(usuario);
        item.setCategoria(categoria);
        item.setStatusAprovacao(StatusAprovacao.PENDENTE);
        item.setMotivoReprovacao(null);

        List<FotoItem> fotoItems = new ArrayList<>();

        for (MultipartFile foto : fotos) {
            String caminho = fotoService.salvarFoto(foto);

            FotoItem fotoItem = new FotoItem();
            fotoItem.setUrl(caminho);
            fotoItem.setItem(item);

            fotoItems.add(fotoItem);
        }

        item.setFotos(fotoItems);

        return itemRepository.save(item);
    }

    public List<Item> listarItemPorCategoria(Long idCategoria){
        Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);
        return itemRepository.findByCategoria(categoria);
    }

    public List<Item> buscarPorNomeECategoria(String nome, Long idCategoria) {
        categoriaService.buscarCategoriaPorId(idCategoria);
        List<Item> itens = itemRepository.findByNomeContainingIgnoreCaseAndCategoriaId(nome, idCategoria);

        if(itens.isEmpty()){
            throw new RecursoNaoEncontradoException("Nenhum item foi encontrado com esse nome nessa categoria.");
        }
        return itens;
    }

    public List<Item> listarItensPendentes(){
        return itemRepository.findByStatusAprovacao(StatusAprovacao.PENDENTE);
    }

    public List<Item> listarItensAprovados(){
        return itemRepository.findByStatusAprovacao(StatusAprovacao.APROVADO);
    }

    public List<Item> listarItensReprovados(){
        return itemRepository.findByStatusAprovacao(StatusAprovacao.REPROVADO);
    }

    public List<Item> listarMeusItens(Long idUsuario){
        usuarioService.buscarUsuarioPorId(idUsuario);
        return itemRepository.findByUsuario_Id(idUsuario);
    }

    public List<Item> listarItensParaCatalago(){
        return itemRepository.findByStatusAprovacaoAndStatusDisponibilidadeIn(StatusAprovacao.APROVADO, List.of(StatusDisponibilidade.DISPONIVEL_VENDA,  StatusDisponibilidade.DISPONIVEL_TROCA));
    }

    public List<Item> listarItensCatalogoPorCategoria(Long idCategoria){
        return itemRepository.findByStatusAprovacaoAndStatusDisponibilidadeInAndCategoriaId(StatusAprovacao.APROVADO, List.of(StatusDisponibilidade.DISPONIVEL_VENDA, StatusDisponibilidade.DISPONIVEL_TROCA), idCategoria);
    }

    public List<Item> listarItensCatalogoPorNomeECategoria(Long idCategoria, String nome) {
        return itemRepository.findByStatusAprovacaoAndStatusDisponibilidadeInAndCategoriaIdAndNomeContainingIgnoreCase(StatusAprovacao.APROVADO, List.of(StatusDisponibilidade.DISPONIVEL_VENDA, StatusDisponibilidade.DISPONIVEL_TROCA), idCategoria, nome);
    }

    public List<Item> listarItensCatalogoPorNome(String nome){
        return itemRepository.findByStatusAprovacaoAndStatusDisponibilidadeInAndNomeContainingIgnoreCase(StatusAprovacao.APROVADO, List.of(StatusDisponibilidade.DISPONIVEL_VENDA, StatusDisponibilidade.DISPONIVEL_TROCA), nome);
    }

    @Transactional
    public Item atualizarItem(Long id, Item itemAtualizado, List<MultipartFile> novasFotos, List<Long> idsFotosRemovidas) throws IOException{

        Item itemAtual = buscarItemPorId(id);

        if(itemAtual.getStatusAprovacao() == StatusAprovacao.APROVADO){
            if(itemAtualizado.getCategoria() != null &&
               !itemAtual.getCategoria().getId().equals(itemAtualizado.getCategoria().getId())){
                throw new RegraNegocioObrigacaoException(
                        "Não é permitido alterar a categoria de um item já aprovado!");
            }
        }else{
            if(itemAtualizado.getCategoria() != null){
                Categoria categoria = categoriaService.buscarCategoriaPorId(itemAtualizado.getCategoria().getId());
                itemAtual.setCategoria(categoria);
            }
        }

        if(itemAtual.getStatusAprovacao() == StatusAprovacao.REPROVADO){
            itemAtual.setStatusAprovacao(StatusAprovacao.PENDENTE);
            itemAtual.setMotivoReprovacao(null);
        }

        itemAtual.setNome(itemAtualizado.getNome());
        itemAtual.setDescricao(itemAtualizado.getDescricao());
        itemAtual.setStatusDisponibilidade(itemAtualizado.getStatusDisponibilidade());

        if(itemAtualizado.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_TROCA){
            itemAtual.setPreco(null);
        }

        if(itemAtualizado.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_VENDA){
            if(itemAtualizado.getPreco() == null || itemAtualizado.getPreco().compareTo(BigDecimal.ZERO) <= 0){
                throw new RegraNegocioObrigacaoException("O preço do item para vendas é obrigatório.");
            }
        }

        /*if(itemAtual.getStatusAprovacao() == StatusAprovacao.REPROVADO && idCategoria != null){
            Categoria categoria = categoriaService.buscarCategoriaPorId(idCategoria);
            itemAtual.setCategoria(categoria);
        }*/

        itemAtual.setPreco(itemAtualizado.getPreco());

        if(idsFotosRemovidas != null && !idsFotosRemovidas.isEmpty()){
            itemAtual.getFotos().removeIf(foto -> {
                if(idsFotosRemovidas.contains(foto.getId())){
                    fotoRepository.delete(foto);
                    return true;
                }
                return false;
            });
        }

        if(novasFotos != null && !novasFotos.isEmpty()){
            for(MultipartFile foto : novasFotos){
                String caminho = fotoService.salvarFoto(foto);
                FotoItem fotoItem = new FotoItem();
                fotoItem.setUrl(caminho);
                fotoItem.setItem(itemAtual);

                itemAtual.getFotos().add(fotoItem);
            }
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

        if(item.getStatusAprovacao() == StatusAprovacao.APROVADO){
            throw new RegraNegocioObrigacaoException("O item já foi aprovado e não pode ser reprovado.");
        }
        item.setMotivoReprovacao(motivo);
        item.setStatusAprovacao(StatusAprovacao.REPROVADO);
        return itemRepository.save(item);
    }

    public Item marcarItemComoVendido(Long id){
        Item item = buscarItemPorId(id);

        if(item.getStatusAprovacao() != StatusAprovacao.APROVADO){
            throw new RegraNegocioObrigacaoException("Item pendente ou reprovado não pode ser marcado como vendido.");
        }

        item.setStatusDisponibilidade(StatusDisponibilidade.VENDIDO);
        return itemRepository.save(item);
    }

    public Item marcarItemComoTrocado(Long id){
        Item item = buscarItemPorId(id);

        if(item.getStatusAprovacao() != StatusAprovacao.APROVADO){
            throw new RegraNegocioObrigacaoException("Item pendente ou reprovado não pode ser marcado como trocado.");
        }

        item.setStatusDisponibilidade(StatusDisponibilidade.TROCADO);
        return itemRepository.save(item);
    }
}
