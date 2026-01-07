package com.projeto.negociaIF.services;

import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.repositories.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    private final String pasta = System.getProperty("user.dir") + "/uploads/itens";

    public FotoItem buscarFotoPorid(Long id){
        return fotoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Foto com id " + id + " não encontrada."));
    }

    /*public FotoItem criarFoto(Long idItem, FotoItem fotoItem){
        Item item = itemService.buscarItemPorId(idItem);
        fotoItem.setItem(item);
        return fotoRepository.save(fotoItem);
    }*/

    public String salvarFoto(MultipartFile file) throws IOException {

        if(file == null || file.isEmpty()){
            throw new RegraNegocioObrigacaoException("Arquivo inválido!");
        }

        File diretorio = new File(pasta);
        if(!diretorio.exists()){
            diretorio.mkdirs();
        }

        String nome = file.getOriginalFilename();
        String extensao = nome.substring(nome.lastIndexOf('.'));

        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        File destino = new  File(diretorio, nomeArquivo);
        file.transferTo(destino);

        return "/uploads/itens/" + nomeArquivo;
    }

    /*public List<FotoItem> listarFotosPorItem(Long idItem){
        Item item =  itemService.buscarItemPorId(idItem);
        return fotoRepository.findByItem(item);
    }

    public FotoItem atualizarFoto(Long id, FotoItem fotoItem){
        FotoItem foto = buscarFotoPorid(id);
        foto.setUrl(fotoItem.getUrl());
        return fotoRepository.save(foto);
    }*/

    public void excluirFotoEspecifica(Long id){
        FotoItem foto = buscarFotoPorid(id);
        fotoRepository.delete(foto);
    }
}
