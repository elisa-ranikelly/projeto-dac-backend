package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.create.FotoCreateDTO;
import com.projeto.negociaIF.dtos.response.FotoResponseDTO;
import com.projeto.negociaIF.dtos.update.FotoUpdateDTO;
import com.projeto.negociaIF.model.FotoItem;
import com.projeto.negociaIF.services.FotoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocia-if/fotos")
public class FotoController {

    @Autowired
    private FotoService fotoService;

    @GetMapping("/buscar-foto/{id}")
    public ResponseEntity<FotoResponseDTO> buscarFotoPorId(@PathVariable Long id) {
        FotoItem foto = fotoService.buscarFotoPorid(id);

        FotoResponseDTO fotoResponseDTO = new FotoResponseDTO();
        BeanUtils.copyProperties(foto, fotoResponseDTO);

        return ResponseEntity.ok(fotoResponseDTO);
    }

    @PostMapping("/criar-foto/{idItem}")
    public ResponseEntity<FotoResponseDTO> criarFoto(@PathVariable Long idItem, @RequestBody @Valid FotoCreateDTO fotoCreateDTO) {
        FotoItem foto = new FotoItem();
        BeanUtils.copyProperties(fotoCreateDTO, foto);

        FotoItem fotoSalva = fotoService.criarFoto(idItem, foto);
        FotoResponseDTO fotoResponseDTO = new FotoResponseDTO();
        BeanUtils.copyProperties(fotoSalva, fotoResponseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(fotoResponseDTO);
    }

    @GetMapping("/listar-foto-item/{idItem}")
    public ResponseEntity<List<FotoResponseDTO>> listarFotoPorItem(@PathVariable Long idItem) {
        List<FotoItem> fotos = fotoService.listarFotosPorItem(idItem);

        List<FotoResponseDTO> listaFotos = fotos.stream().map(ft -> {
            FotoResponseDTO fotoResponseDTO = new FotoResponseDTO();
            BeanUtils.copyProperties(ft, fotoResponseDTO);
            return fotoResponseDTO;
        }).toList();

        return ResponseEntity.ok(listaFotos);
    }

    @PutMapping("/atualizar-foto/{id}")
    public ResponseEntity<FotoResponseDTO> atualizarFoto(@PathVariable Long id, @RequestBody @Valid FotoUpdateDTO fotoUpdateDTO) {
        FotoItem fotoAtualizada  = new FotoItem();
        BeanUtils.copyProperties(fotoUpdateDTO, fotoAtualizada);

        FotoItem fotoSalva = fotoService.atualizarFoto(id, fotoAtualizada);
        FotoResponseDTO fotoResponseDTO = new FotoResponseDTO();
        BeanUtils.copyProperties(fotoSalva, fotoResponseDTO);

        return ResponseEntity.ok(fotoResponseDTO);
    }

    @DeleteMapping("/excluir-foto/{id}")
    public ResponseEntity<Void> excluirFotoEspecifica(@PathVariable Long id) {
        fotoService.excluirFotoEspecifica(id);
        return ResponseEntity.noContent().build();
    }


}
