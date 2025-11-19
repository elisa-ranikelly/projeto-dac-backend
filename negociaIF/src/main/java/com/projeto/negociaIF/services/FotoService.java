package com.projeto.negociaIF.services;

import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.repositories.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private ItemService itemService;

    public FotoItem buscarFotoPorid(Long id){
        return fotoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Foto com id " + id + " não encontrada."));
    }

    public FotoItem criarFoto(Long idItem, FotoItem fotoItem){
        Item item = itemService.buscarItemPorId(idItem);
        fotoItem.setItem(item);
        return fotoRepository.save(fotoItem);
    }

    public List<FotoItem> listarFotosPorItem(Long idItem){
        Item item =  itemService.buscarItemPorId(idItem);
        return fotoRepository.findByItem(item);
    }

    public FotoItem atualizarFoto(Long id, FotoItem fotoItem){
        FotoItem foto = buscarFotoPorid(id);
        foto.setUrl(fotoItem.getUrl());
        return fotoRepository.save(foto);
    }

    public void excluirFotoEspecifica(Long id){
        FotoItem foto = buscarFotoPorid(id);
        fotoRepository.delete(foto);
    }

    public void excluirFotosPorItem(Long idItem){
        Item item =  itemService.buscarItemPorId(idItem);
        List<FotoItem> fotos = fotoRepository.findByItem(item);

        if(fotos.isEmpty()){
            return;
        }
        fotoRepository.deleteAll(fotos);
    }
}
