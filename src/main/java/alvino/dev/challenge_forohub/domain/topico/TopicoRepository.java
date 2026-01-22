package alvino.dev.challenge_forohub.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long>, JpaSpecificationExecutor<Topico> {
    // Métodos personalizados, ej: findByTitulo(String titulo);

    boolean existsByTitulo(String titulo);
    // Si quieres cumplir la regla de negocio del reto (Título Y Mensaje):
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    @Query("SELECT t FROM Topico t JOIN FETCH t.autor JOIN FETCH t.curso WHERE t.activo = true")
    Page<Topico> findByActivoTrue(Pageable pageable);

    @Query("SELECT t FROM Topico t JOIN FETCH t.autor JOIN FETCH t.curso WHERE t.id = :id AND t.activo = true")
    Optional<Topico> findByIdAndActivoTrue(@Param("id") Long id);
}
