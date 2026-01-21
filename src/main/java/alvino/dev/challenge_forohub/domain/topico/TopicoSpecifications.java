package alvino.dev.challenge_forohub.domain.topico;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TopicoSpecifications {
    public static Specification<Topico> activoTrue() {
        return (root, query, cb) -> cb.isTrue(root.get("activo"));
    }

    public static Specification<Topico> conCursoNombre(String nombreCurso) {
        return (root, query, cb) -> {
            // Join con curso
            var curso = root.join("curso", JoinType.INNER);
            return cb.like(cb.lower(curso.get("nombre")), "%" + nombreCurso.toLowerCase() + "%");
        };
    }

    public static Specification<Topico> enAnio(Integer anio) {
        return (root, query, cb) -> {
            LocalDateTime inicio = LocalDateTime.of(anio, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.of(anio, 12, 31, 23, 59, 59);
            return cb.between(root.get("fechaCreacion"), inicio, fin);
        };
    }
}
