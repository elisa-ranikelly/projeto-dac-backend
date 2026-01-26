package com.projeto.negociaIF.services;

import com.projeto.negociaIF.exceptions.DuplicateFieldException;
import com.projeto.negociaIF.exceptions.RecursoNaoEncontradoException;
import com.projeto.negociaIF.exceptions.RegraNegocioObrigacaoException;
import com.projeto.negociaIF.model.Role;
import com.projeto.negociaIF.model.Usuario;
import com.projeto.negociaIF.repositories.RoleRepository;
import com.projeto.negociaIF.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário com id " + id + " não encontrado."));
    }

    public Usuario criarUsuario(Usuario usuario, String tipoUsuario){
        validarEmail(usuario.getEmail());

        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new DuplicateFieldException("Email já cadastrado.");
        }

        if (usuario.getTelefone() != null && !usuario.getTelefone().isBlank()) {
            if (usuarioRepository.existsByTelefone(usuario.getTelefone())) {
                throw new DuplicateFieldException("Telefone já cadastrado.");
            }
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Role role;
        if("ADMIN".equalsIgnoreCase(tipoUsuario)){
            role = roleRepository.findByNome("ADMIN")
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Role ADMIN não encontrado."));
        }else{
            role = roleRepository.findByNome("USER")
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Role USER não encontrada."));

            if (usuario.getTelefone() == null || usuario.getTelefone().isBlank()) {
                throw new IllegalArgumentException("Telefone é obrigatório para usuários comuns.");
            }
        }

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado){
        Usuario usuario = buscarUsuarioPorId(id);

        if(!usuario.getEmail().equals(usuarioAtualizado.getEmail())){
            validarEmail(usuarioAtualizado.getEmail());

            if(usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())){
                throw new DuplicateFieldException("O email já está sendo usado.");
            }

            usuario.setEmail(usuarioAtualizado.getEmail());
        }

        if(usuarioAtualizado.getTelefone() != null && !usuarioAtualizado.getTelefone().equals(usuario.getTelefone())){
            if (usuarioRepository.existsByTelefone(usuarioAtualizado.getTelefone())) {
                throw new DuplicateFieldException("O telefone já está sendo usado.");
            }

            usuario.setTelefone(usuarioAtualizado.getTelefone());
        }

        usuario.setNome(usuarioAtualizado.getNome());

        if(usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()){
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id){
        if(!usuarioRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Usuário com id " + id + " não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    /*public Usuario autenticar(String email, String senha){

        Usuario usuario = findByEmail(email)
                .orElseThrow(() -> new RegraNegocioObrigacaoException("Email ou senha inválidos!"));

        if(!usuario.getSenha().equals(senha)){
            throw new RegraNegocioObrigacaoException("Email ou senha inválidos!");
        }

        return usuario;
    }*/

    private boolean emailValido(String email){
        return email.endsWith("@academico.ifpb.edu.br") || email.endsWith("@ifpb.edu.br");
    }

    private void validarEmail(String email){
        if(!emailValido(email)){
            throw new IllegalArgumentException("Email inválido. Utilize seu email acadêmico.");
        }
    }
}
