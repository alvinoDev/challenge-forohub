package alvino.dev.challenge_forohub.domain.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNombre(String nombre);

    Page<Curso> findAllByActivoTrue(Pageable pageable);

}
