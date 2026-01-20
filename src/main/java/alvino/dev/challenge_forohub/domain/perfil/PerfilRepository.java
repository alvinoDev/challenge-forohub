package alvino.dev.challenge_forohub.domain.perfil;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    boolean existsByNombre(String nombre);
}
