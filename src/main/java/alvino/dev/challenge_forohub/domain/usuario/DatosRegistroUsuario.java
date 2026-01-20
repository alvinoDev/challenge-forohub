package alvino.dev.challenge_forohub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DatosRegistroUsuario(
        @NotBlank String nombre,
        @NotBlank @Email String correoElectronico,
        @NotBlank String contrasena,
        @NotNull List<Long> perfilIds  // IDs de perfiles a asignar (ej: [1] para ROLE_USUARIO)
) {}
