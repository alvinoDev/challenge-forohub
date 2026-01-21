package alvino.dev.challenge_forohub.domain.topico;

import alvino.dev.challenge_forohub.domain.curso.Curso;

public record DatosRelacionCurso(
        Long id,
        String nombre
) {
    public DatosRelacionCurso(Curso curso) {
        this(
            curso.getId(),
            curso.getNombre()
        );
    }
}
