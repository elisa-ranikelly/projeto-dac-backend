package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.request.LoginRequestDTO;
import com.projeto.negociaIF.dtos.response.UsuarioResponseDTO;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.model.Role;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.UsuarioRepository;
import com.projeto.negociaIF.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/negocia-if/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){

        Usuario usuario = usuarioService.autenticar(loginRequestDTO.getEmail(), loginRequestDTO.getSenha());

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        BeanUtils.copyProperties(usuario, usuarioResponseDTO);

        Set<String> roles = usuario.getRoles()
                .stream()
                .map(Role::getNome)
                .collect(Collectors.toSet());
        usuarioResponseDTO.setRoles(roles);

        return ResponseEntity.ok(usuarioResponseDTO);
    }
}
