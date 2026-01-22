package alvino.dev.challenge_forohub.domain.topico;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizarTopico(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        EstadoTopico status
) { }
