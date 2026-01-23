package alvino.dev.challenge_forohub.domain.respuesta;

import alvino.dev.challenge_forohub.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    // Listar todas las activas
    // @Query("SELECT r FROM Respuesta r JOIN FETCH r.autor WHERE r.activo = true")
    @EntityGraph(attributePaths = {"autor"})
    Page<Respuesta> findAllByActivoTrue(Pageable pageable);

    // Listar por ID de Tópico y que estén activas
    @EntityGraph(attributePaths = {"autor"})
    Page<Respuesta> findAllByTopicoIdAndActivoTrue(Long topicoId, Pageable pageable);

    // Listar por Nombre de Autor (usando el objeto relacionado) y que estén activas
    @EntityGraph(attributePaths = {"autor"})
    Page<Respuesta> findAllByAutorNombreContainingAndActivoTrue(String nombre, Pageable pageable);

    @EntityGraph(attributePaths = {"autor"})
    Optional<Respuesta> findByIdAndActivoTrue(Long id);
}
