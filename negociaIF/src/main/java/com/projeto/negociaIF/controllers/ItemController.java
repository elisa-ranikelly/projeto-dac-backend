package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.create.ItemCreateDTO;
import com.projeto.negociaIF.dtos.create.MotivoReprovacaoCreateDTO;
import com.projeto.negociaIF.dtos.response.FotoResponseDTO;
import com.projeto.negociaIF.dtos.response.ItemResponseDTO;
import com.projeto.negociaIF.dtos.update.ItemUpdateDTO;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarItemPorId(@PathVariable Long id){
        Item item = itemService.buscarItemPorId(id);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);
        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList()
        );

        return ResponseEntity.ok(itemResponseDTO);
    }

    @PostMapping("/{idUsuarioDono}")
    public ResponseEntity<ItemResponseDTO> criarItem(@RequestBody @Valid ItemCreateDTO  itemCreateDTO, @PathVariable Long idUsuarioDono){
        Item item = new Item();
        BeanUtils.copyProperties(itemCreateDTO,item);

        List<FotoItem> fotos = itemCreateDTO.getFotos().stream().map(url -> {
            FotoItem fotoItem = new FotoItem();
            fotoItem.setUrl(url);
            return fotoItem;
        }).toList();

        item.setFotos(fotos);

        Item novoItem = itemService.criarItem(item, idUsuarioDono, itemCreateDTO.getIdCategoria());

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(novoItem,itemResponseDTO);
        itemResponseDTO.setCategoria(novoItem.getCategoria().getNome());
        itemResponseDTO.setFotos(novoItem.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(itemResponseDTO);
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ItemResponseDTO>> listarItemPorCategoria(@PathVariable Long idCategoria){
        List<Item> itens = itemService.listarItemPorCategoria(idCategoria);

        List<ItemResponseDTO> lista = itens.stream().map(item -> {
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            BeanUtils.copyProperties(item,itemResponseDTO);
            itemResponseDTO.setCategoria(item.getCategoria().getNome());
            itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                    new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());
            return itemResponseDTO;
        }).toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<ItemResponseDTO>> listarItensPendentes(){
        List<Item> itens = itemService.listarItensPendentes();

        List<ItemResponseDTO> lista = itens.stream().map(item -> {
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            BeanUtils.copyProperties(item,itemResponseDTO);
            itemResponseDTO.setCategoria(item.getCategoria().getNome());
            itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                    new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

            return itemResponseDTO;
        }).toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable Long id, @RequestBody @Valid ItemUpdateDTO itemUpdateDTO){
        Item itemAtualizado = new Item();
        BeanUtils.copyProperties(itemUpdateDTO,itemAtualizado);

        Item itemSalvo = itemService.atualizarItem(id, itemAtualizado);
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(itemSalvo,itemResponseDTO);
        itemResponseDTO.setCategoria(itemSalvo.getCategoria().getNome());
        itemResponseDTO.setFotos(itemSalvo.getFotos().stream()
                .map(foto -> new FotoResponseDTO(foto.getId(), foto.getUrl()))
                        .toList()
        );

        return ResponseEntity.ok(itemResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirItem(@PathVariable Long id){
        itemService.excluirItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<ItemResponseDTO> aprovarItem(@PathVariable Long id){
        Item item = itemService.aprovarItem(id);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);

        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

        return ResponseEntity.ok(itemResponseDTO);
    }

    @PutMapping("/{id}/reprovar")
    public ResponseEntity<ItemResponseDTO> reprovarItem(@PathVariable Long id, @RequestBody @Valid MotivoReprovacaoCreateDTO  motivoReprovacaoDTO){
        Item item = itemService.reprovarItem(id, motivoReprovacaoDTO.getMotivoReprovacao());

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);
        itemResponseDTO.setStatusAprovacao(item.getStatusAprovacao());
        itemResponseDTO.setMotivoReprovacao(item.getMotivoReprovacao());
        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

        return ResponseEntity.ok(itemResponseDTO);
    }

    @PutMapping("/{id}/vendido")
    public ResponseEntity<ItemResponseDTO> marcarItemComoVendido(@PathVariable Long id){
        Item item = itemService.marcarItemComoVendido(id);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);
        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

        return ResponseEntity.ok(itemResponseDTO);
    }

    @PutMapping("/{id}/trocado")
    public ResponseEntity<ItemResponseDTO> marcarItemComoTrocado(@PathVariable Long id){
        Item item = itemService.marcarItemComoTrocado(id);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);
        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

        return ResponseEntity.ok(itemResponseDTO);
    }
}
