package alvino.dev.challenge_forohub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    // Métodos personalizados, ej: findByTitulo(String titulo);

    boolean existsByTitulo(String titulo);
    // Si quieres cumplir la regla de negocio del reto (Título Y Mensaje):
    boolean existsByTituloAndMensaje(String titulo, String mensaje);
}
