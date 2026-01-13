package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.create.ItemCreateDTO;
import com.projeto.negociaIF.dtos.create.MotivoReprovacaoCreateDTO;
import com.projeto.negociaIF.dtos.response.FotoResponseDTO;
import com.projeto.negociaIF.dtos.response.ItemResponseDTO;
import com.projeto.negociaIF.dtos.update.ItemUpdateDTO;
import com.projeto.negociaIF.enums.StatusAprovacao;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.model.Item;
import com.projeto.negociaIF.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/negocia-if/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/buscar-item/{id}")
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

    @PostMapping(value = "/criar-item/{idUsuarioDono}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponseDTO> criarItem(@PathVariable Long idUsuarioDono, @RequestPart("item") @Valid ItemCreateDTO  itemCreateDTO, @RequestPart("fotos") List<MultipartFile> fotos) throws IOException {
        Item item = new Item();
        BeanUtils.copyProperties(itemCreateDTO,item);

        Item novoItem = itemService.criarItem(item, idUsuarioDono, fotos, itemCreateDTO.getIdCategoria());

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(novoItem, itemResponseDTO);
        itemResponseDTO.setCategoria(novoItem.getCategoria().getNome());
        itemResponseDTO.setFotos(novoItem.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(itemResponseDTO);
    }

    @GetMapping("/listar-item-categoria/{idCategoria}")
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

    @GetMapping("/buscar-item-por-nome/{idCategoria}")
    public ResponseEntity<List<ItemResponseDTO>> buscarItemPorNome(@PathVariable Long idCategoria, @RequestParam String nome){
        List<Item> itens = itemService.buscarPorNomeECategoria(nome, idCategoria);

        List<ItemResponseDTO> lista = itens.stream().map(item -> {
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            BeanUtils.copyProperties(item, itemResponseDTO);

            itemResponseDTO.setCategoria(item.getCategoria().getNome());

            itemResponseDTO.setFotos(
                    item.getFotos().stream()
                            .map(foto -> new FotoResponseDTO(foto.getId(), foto.getUrl()))
                            .toList()
            );

            return itemResponseDTO;
        }).toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listar-itens-pendentes")
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

    @GetMapping("/listar-itens-aprovados")
    public ResponseEntity<List<ItemResponseDTO>> listarItensAprovados(){
        List<Item> itens = itemService.listarItensAprovados();

        List<ItemResponseDTO> lista = itens.stream().map(item ->  {
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            BeanUtils.copyProperties(item,itemResponseDTO);
            itemResponseDTO.setCategoria(item.getCategoria().getNome());
            itemResponseDTO.setTelefone(item.getUsuario().getTelefone());
            itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                    new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());
            return itemResponseDTO;

        }).toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listar-itens-reprovados")
    public ResponseEntity<List<ItemResponseDTO>> listarItensReprovados(){
        List<Item> itens = itemService.listarItensReprovados();

        List<ItemResponseDTO> lista = itens.stream().map(item ->  {
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            BeanUtils.copyProperties(item,itemResponseDTO);
            itemResponseDTO.setCategoria(item.getCategoria().getNome());
            itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                    new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());
            return itemResponseDTO;

        }).toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listar-meus-itens/{idUsuario}")
    public ResponseEntity<List<ItemResponseDTO>> listarMeusItens(@PathVariable Long idUsuario){
        List<Item> itens = itemService.listarMeusItens(idUsuario);

        List<ItemResponseDTO> lista = itens.stream().map(item ->{
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            BeanUtils.copyProperties(item,itemResponseDTO);

            itemResponseDTO.setCategoria(item.getCategoria().getNome());
            itemResponseDTO.setStatusDisponibilidade(item.getStatusDisponibilidade());
            itemResponseDTO.setStatusAprovacao(item.getStatusAprovacao());
            itemResponseDTO.setMotivoReprovacao(item.getMotivoReprovacao());

            itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                    new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());
            return itemResponseDTO;
        }).toList();
        return ResponseEntity.ok(lista);
    }

    @PutMapping(value = "/atualizar-item/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable Long id,
                                                         @RequestPart("item") @Valid ItemUpdateDTO itemUpdateDTO,
                                                         @RequestPart(value = "novasFotos", required = false) List<MultipartFile> novasFotos,
                                                         @RequestPart(value = "idsFotosRemovidas", required = false) List<Long> idsFotosRemovidas) throws IOException {
        Item item = new Item();
        BeanUtils.copyProperties(itemUpdateDTO,item);

       // Long idCategoria = itemUpdateDTO.getIdCategoria();

        if(itemUpdateDTO.getIdCategoria() != null){
            Categoria categoria = new  Categoria();
            categoria.setId(itemUpdateDTO.getIdCategoria());
            item.setCategoria(categoria);
        }

        Item itemSalvo = itemService.atualizarItem(id, item, novasFotos, idsFotosRemovidas);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(itemSalvo,itemResponseDTO);

        itemResponseDTO.setCategoria(itemSalvo.getCategoria().getNome());
        itemResponseDTO.setFotos(itemSalvo.getFotos().stream()
                .map(foto -> new FotoResponseDTO(foto.getId(), foto.getUrl()))
                        .toList()
        );

        return ResponseEntity.ok(itemResponseDTO);
    }

    @DeleteMapping("/excluir-item/{id}")
    public ResponseEntity<Void> excluirItem(@PathVariable Long id){
        itemService.excluirItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/aprovar-item/{id}")
    public ResponseEntity<ItemResponseDTO> aprovarItem(@PathVariable Long id){
        Item item = itemService.aprovarItem(id);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);

        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

        return ResponseEntity.ok(itemResponseDTO);
    }

    @PutMapping("/reprovar-item/{id}")
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

    @PutMapping("/item-vendido/{id}")
    public ResponseEntity<ItemResponseDTO> marcarItemComoVendido(@PathVariable Long id){
        Item item = itemService.marcarItemComoVendido(id);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        BeanUtils.copyProperties(item,itemResponseDTO);
        itemResponseDTO.setCategoria(item.getCategoria().getNome());
        itemResponseDTO.setFotos(item.getFotos().stream().map(foto ->
                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList());

        return ResponseEntity.ok(itemResponseDTO);
    }

    @PutMapping("/item-trocado/{id}")
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
