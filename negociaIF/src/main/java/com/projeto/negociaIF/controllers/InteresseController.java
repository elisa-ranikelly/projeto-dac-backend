package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.accept.InteresseAcceptDTO;
import com.projeto.negociaIF.dtos.create.InteresseCreateDTO;
import com.projeto.negociaIF.dtos.response.InteresseResponseDTO;
import com.projeto.negociaIF.model.Interesse;
import com.projeto.negociaIF.services.InteresseService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/interesses")
public class InteresseController {

    @Autowired
    private InteresseService interesseService;

    @GetMapping("/{id}")
    public ResponseEntity<InteresseResponseDTO> buscarInteressePorId(@PathVariable Long id){
        Interesse interesse = interesseService.buscarInteressePorId(id);

        InteresseResponseDTO interesseResponseDTO = new InteresseResponseDTO();
        BeanUtils.copyProperties(interesse, interesseResponseDTO);

        return ResponseEntity.ok(interesseResponseDTO);
    }

    @PostMapping()
    public ResponseEntity<InteresseResponseDTO> criarInteresse(@Valid @RequestBody InteresseCreateDTO  interesseCreateDTO){
        Interesse interesse = interesseService.criarInteresse(interesseCreateDTO.getIdUsuarioInteressado(), interesseCreateDTO.getIdItem());

        InteresseResponseDTO interesseResponseDTO = new InteresseResponseDTO();
        BeanUtils.copyProperties(interesse, interesseResponseDTO);

        interesseResponseDTO.setIdUsuarioInteressado(interesse.getUsuario().getId());
        interesseResponseDTO.setIdItem(interesse.getItem().getId());

        return ResponseEntity.ok(interesseResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<InteresseResponseDTO>> listarTodosInteresses(){
        List<Interesse> interesses = interesseService.listarInteresses();

        List<InteresseResponseDTO> listaInteresse = interesses.stream().map(interesse -> {
            InteresseResponseDTO interesseResponseDTO = new InteresseResponseDTO();
            BeanUtils.copyProperties(interesse, interesseResponseDTO);
            interesseResponseDTO.setIdUsuarioInteressado(interesse.getUsuario().getId());
            interesseResponseDTO.setIdItem(interesse.getItem().getId());
            return interesseResponseDTO;
        }).toList();

        return ResponseEntity.ok(listaInteresse);
    }

    @GetMapping("/usuario/{idInteressado}")
    public ResponseEntity<List<InteresseResponseDTO>> listarInteressePorIdUsuario(@PathVariable Long idInteressado){
        List<Interesse> interesses = interesseService.listarInteressesPorUsuario(idInteressado);

        List<InteresseResponseDTO> listaInteresse = interesses.stream().map(interesse -> {
            InteresseResponseDTO interesseResponseDTO = new InteresseResponseDTO();
            BeanUtils.copyProperties(interesse, interesseResponseDTO);
            interesseResponseDTO.setIdUsuarioInteressado(interesse.getUsuario().getId());
            interesseResponseDTO.setIdItem(interesse.getItem().getId());
            return interesseResponseDTO;
        }).toList();

        return ResponseEntity.ok(listaInteresse);
    }

    @GetMapping("item/{idItem}")
    public ResponseEntity<List<InteresseResponseDTO>> listarInteressePorIdItem(@PathVariable Long idItem){
        List<Interesse> interesses = interesseService.listarInteressesPorItem(idItem);

        List<InteresseResponseDTO> listaInteresse = interesses.stream().map(interesse -> {
            InteresseResponseDTO interesseResponseDTO = new InteresseResponseDTO();
            BeanUtils.copyProperties(interesse, interesseResponseDTO);
            interesseResponseDTO.setIdUsuarioInteressado(interesse.getUsuario().getId());
            interesseResponseDTO.setIdItem(interesse.getItem().getId());
            return interesseResponseDTO;
        }).toList();

        return ResponseEntity.ok(listaInteresse);
    }

    @DeleteMapping("/{id}/usuario/{idUsuario}")
    public ResponseEntity<Void> excluirInteresse(@PathVariable Long id, @PathVariable Long idUsuario){
        interesseService.excluirInteressePorId(id,idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/aceitar")
    public ResponseEntity<InteresseResponseDTO> aceitarInteresse(@Valid @RequestBody InteresseAcceptDTO interesseAcceptDTO){
        Interesse interesse = interesseService.aceitarInteresse(interesseAcceptDTO.getId(), interesseAcceptDTO.getIdUsuarioDono());

        InteresseResponseDTO interesseResponseDTO = new InteresseResponseDTO();
        BeanUtils.copyProperties(interesse, interesseResponseDTO);
        interesseResponseDTO.setIdUsuarioInteressado(interesse.getUsuario().getId());
        interesseResponseDTO.setIdItem(interesse.getItem().getId());

        return ResponseEntity.ok(interesseResponseDTO);
    }
}
