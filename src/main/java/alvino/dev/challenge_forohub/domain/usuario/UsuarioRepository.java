package alvino.dev.challenge_forohub.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Para validar duplicados
    boolean existsByCorreoElectronico(String correoElectronico);

    // Mostrar Usuarios con perfiles
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.perfiles WHERE u.activo = true")
    Page<Usuario> findAllWithPerfiles(Pageable pageable);

    // Mostrar Usuario específico con perfiles
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.perfiles WHERE u.id = :id")
    Optional<Usuario> findByIdWithPerfiles(Long id);

    // Buscar Correo del Usuario
    Optional<UserDetails> findByCorreoElectronico(String correoElectronico);
}
