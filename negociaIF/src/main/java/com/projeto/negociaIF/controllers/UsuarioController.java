package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.create.UsuarioCreateDTO;
import com.projeto.negociaIF.dtos.response.UsuarioResponseDTO;
import com.projeto.negociaIF.dtos.update.UsuarioUpdateDTO;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/negocia-if/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService  usuarioService;

    @GetMapping("/buscar-usuario/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        BeanUtils.copyProperties(usuario, usuarioResponseDTO);

        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @PostMapping("/criar-usuario")
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@RequestBody @Valid UsuarioCreateDTO  usuarioCreateDTO){
        Usuario novoUsuario = new Usuario();
        BeanUtils.copyProperties(usuarioCreateDTO, novoUsuario);

        Usuario usuarioSalvo = usuarioService.criarUsuario(novoUsuario);

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        BeanUtils.copyProperties(usuarioSalvo, usuarioResponseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
    }

    @GetMapping("/listar-usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        List<UsuarioResponseDTO> listaUsuarios = usuarios.stream().map(usuario -> {
            UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
            BeanUtils.copyProperties(usuario, usuarioResponseDTO);
            return usuarioResponseDTO;
        }).toList();

        return ResponseEntity.ok(listaUsuarios);
    }

    @PutMapping("/atualizar-usuario/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO){
        Usuario usuarioAtualizado = new Usuario();
        BeanUtils.copyProperties(usuarioUpdateDTO, usuarioAtualizado);

        Usuario usuarioSalvo =  usuarioService.atualizarUsuario(id, usuarioAtualizado);
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        BeanUtils.copyProperties(usuarioSalvo, usuarioResponseDTO);

        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @DeleteMapping("/excluir-usuario/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id){
        usuarioService.deletarUsuario(id);
        return  ResponseEntity.noContent().build();
    }
}
