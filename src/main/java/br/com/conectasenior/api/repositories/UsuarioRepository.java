package br.com.conectasenior.api.repositories;

import br.com.conectasenior.api.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações com a entidade Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por email (usado para autenticação)
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuários ativos
     */
    Page<Usuario> findByAtivoTrue(Pageable pageable);

    /**
     * Busca usuários por tipo
     */
    List<Usuario> findByTipoAndAtivoTrue(Usuario.TipoUsuario tipo);

    /**
     * Busca usuários vinculados a um idoso específico
     */
    @Query("SELECT u FROM Usuario u JOIN u.idososVinculados i WHERE i.id = :idosoId AND u.ativo = true")
    List<Usuario> findUsuariosVinculadosAoIdoso(@Param("idosoId") Long idosoId);

    /**
     * Verifica se email já existe
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários por nome (busca parcial)
     */
    Page<Usuario> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome, Pageable pageable);
}
