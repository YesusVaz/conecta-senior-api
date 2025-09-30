package br.com.conectasenior.api.services;

import br.com.conectasenior.api.dtos.UsuarioDTO;
import br.com.conectasenior.api.dtos.requests.LoginRequest;
import br.com.conectasenior.api.dtos.requests.RegistroRequest;
import br.com.conectasenior.api.dtos.responses.LoginResponse;
import br.com.conectasenior.api.dtos.responses.TokenResponse;
import br.com.conectasenior.api.entities.Usuario;
import br.com.conectasenior.api.exceptions.BusinessException;
import br.com.conectasenior.api.repositories.UsuarioRepository;
import br.com.conectasenior.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para operações de autenticação
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Autentica usuário e retorna token JWT
     */
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Tentativa de login para: {}", loginRequest.getEmail());

        // Autentica credenciais
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())
        );

        // Busca usuário autenticado
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        // Gera token JWT
        String token = jwtService.generateToken(usuario);

        log.info("Login realizado com sucesso para usuário: {}", usuario.getEmail());
        return new LoginResponse(token, "Bearer", usuario.getEmail(), usuario.getNome(), usuario.getTipo().name());
    }

    /**
     * Registra novo usuário
     */
    public UsuarioDTO registro(RegistroRequest registroRequest) {
        log.info("Registro de novo usuário: {}", registroRequest.getEmail());

        // Verifica se email já existe
        if (usuarioRepository.findByEmail(registroRequest.getEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado");
        }

        // Cria novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(registroRequest.getNome());
        novoUsuario.setEmail(registroRequest.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(registroRequest.getSenha()));
        // Convertendo String para enum TipoUsuario
        novoUsuario.setTipo(Usuario.TipoUsuario.valueOf(registroRequest.getPapel().toUpperCase()));
        novoUsuario.setTelefone(registroRequest.getTelefone());
        novoUsuario.setAtivo(true);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        log.info("Usuário registrado com sucesso: {}", usuarioSalvo.getEmail());

        return modelMapper.map(usuarioSalvo, UsuarioDTO.class);
    }

    /**
     * Renova token JWT
     */
    public TokenResponse refreshToken(String authorizationHeader) {
        log.info("Renovando token JWT");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BusinessException("Token inválido");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (jwtService.validateToken(token, usuario)) {
            String novoToken = jwtService.generateToken(usuario);
            return new TokenResponse(novoToken, "Bearer", jwtExpiration);
        }

        throw new BusinessException("Token inválido ou expirado");
    }
}
