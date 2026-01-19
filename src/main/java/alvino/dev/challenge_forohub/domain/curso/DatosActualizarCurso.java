package alvino.dev.challenge_forohub.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizarCurso(
        @NotBlank String nombre,
        Categoria categoria
) {
}
