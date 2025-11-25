package com.projeto.negociaIF.services;

import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.enums.StatusDisponibilidade;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.Interesse;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.InteresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteresseService {

    @Autowired
    private InteresseRepository interesseRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UsuarioService usuarioService;

    public Interesse buscarInteressePorId(Long id){
        return interesseRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Interesse com id " + id + " não encontrado."));
    }

    public Interesse criarInteresse(Long idInteressado, Long idItem){
        Usuario usuario = usuarioService.buscarUsuarioPorId(idInteressado);
        Item item = itemService.buscarItemPorId(idItem);

        if(item.getUsuario().getId().equals(idInteressado)){
            throw new RegraNegocioObrigacaoException("Você não pode demostrar interesse no seu próprio item.");
        }

        if(interesseRepository.existsByUsuarioAndItem(usuario, item)){
            throw new RegraNegocioObrigacaoException("Você já demonstrou interesse nesse item.");
        }

        if(item.getStatusAprovacao() != (StatusAprovacao.APROVADO)){
            throw new RegraNegocioObrigacaoException("O item ainda não foi aprovado, não é possível criar interesses.");
        }

        if(item.getStatusDisponibilidade() != StatusDisponibilidade.DISPONIVEL_VENDA &&
        item.getStatusDisponibilidade() != StatusDisponibilidade.DISPONIVEL_TROCA){
            throw new RegraNegocioObrigacaoException("Não é possível criar interesse. Item não está disponível");
        }

        Interesse interesse = new Interesse();
        interesse.setUsuario(usuario);
        interesse.setItem(item);
        interesse.setAceito(false);

        return interesseRepository.save(interesse);
    }

    public List<Interesse> listarInteresses(){
        return interesseRepository.findAll();
    }

    public List<Interesse> listarInteressesPorUsuario(Long idInteressado){
        Usuario usuario = usuarioService.buscarUsuarioPorId(idInteressado);
        return interesseRepository.findByUsuario(usuario);
    }

    public List<Interesse> listarInteressesPorItem(Long idItem){
        Item item = itemService.buscarItemPorId(idItem);
        return interesseRepository.findByItem(item);
    }

    public void excluirInteressePorId(Long id, Long idUsuario){
        Interesse interesse = buscarInteressePorId(id);

        if(!interesse.getUsuario().getId().equals(idUsuario)){
            throw new RegraNegocioObrigacaoException("Você não pode excluir interesse de outro usuário.");
        }

        if(interesse.isAceito()){
            throw new RegraNegocioObrigacaoException("Você não pode excluir um interesse que foi aceito.");
        }

        interesseRepository.delete(interesse);
    }

    public Interesse aceitarInteresse(Long id, Long idUsuarioDono){
        Interesse interesse = buscarInteressePorId(id);
        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuarioDono);
        Item item = interesse.getItem();

        if(!item.getUsuario().getId().equals(idUsuarioDono)){
            throw new RegraNegocioObrigacaoException("Somente o dono do item pode aceitar o interesse");
        }

        if(item.getStatusAprovacao() != StatusAprovacao.APROVADO){
            throw new RegraNegocioObrigacaoException("Não é possível aceitar interesses de item pendentes.");
        }

        interesse.setAceito(true);

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.DISPONIVEL_TROCA){
            itemService.marcarItemComoTrocado(item.getId());
        }

        if(item.getStatusDisponibilidade() ==  StatusDisponibilidade.DISPONIVEL_VENDA){
            itemService.marcarItemComoVendido(item.getId());
        }

        if(item.getStatusDisponibilidade() == StatusDisponibilidade.VENDIDO || item.getStatusDisponibilidade() == StatusDisponibilidade.TROCADO && interesse.isAceito()){
            throw new RegraNegocioObrigacaoException("Não é possível aceitar interesse, pois o item não está mais disponível.");
        }

        return  interesseRepository.save(interesse);
    }
}
