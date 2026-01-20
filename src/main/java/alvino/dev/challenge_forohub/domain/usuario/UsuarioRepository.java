package alvino.dev.challenge_forohub.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Para validar duplicados
    boolean existsByCorreoElectronico(String correoElectronico);

    // Mostrar Usuarios con perfiles
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.perfiles")
    Page<Usuario> findAllWithPerfiles(Pageable pageable);
}
