package alvino.dev.challenge_forohub.domain.perfil;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizarPerfil(
        @NotBlank String nombre
) { }
