package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.request.LoginRequestDTO;
import com.projeto.negociaIF.dtos.response.UsuarioResponseDTO;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.model.Role;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.UsuarioRepository;
import com.projeto.negociaIF.security.JwtTokenService;
import com.projeto.negociaIF.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/negocia-if/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        String token = jwtTokenService.gerarToken((UserDetails) authentication.getPrincipal());

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        BeanUtils.copyProperties(usuario, dto);

        Set<String> roles = usuario.getRoles()
                .stream()
                .map(Role::getNome)
                .collect(Collectors.toSet());

        dto.setRoles(roles);
        dto.setToken(token);

        return ResponseEntity.ok(dto);
    }
}
