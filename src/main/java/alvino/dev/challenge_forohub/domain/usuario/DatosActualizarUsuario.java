package alvino.dev.challenge_forohub.domain.usuario;

import java.util.List;

public record DatosActualizarUsuario(
        String nombre,
        String contrasena,
        List<Long> perfilIds
) {}
