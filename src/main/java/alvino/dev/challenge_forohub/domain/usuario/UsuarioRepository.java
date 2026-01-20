package alvino.dev.challenge_forohub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Para validar duplicados
    boolean existsByCorreoElectronico(String correoElectronico);
}
