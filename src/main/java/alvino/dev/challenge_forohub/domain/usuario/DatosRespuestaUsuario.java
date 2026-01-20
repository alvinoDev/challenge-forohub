package alvino.dev.challenge_forohub.domain.usuario;

import java.util.List;

public record DatosRespuestaUsuario(
        Long id,
        String nombre,
        String correoElectronico,
        List<DatosRelacionPerfil> perfiles
) {
    public DatosRespuestaUsuario(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getCorreoElectronico(),
            usuario.getPerfiles().stream().map(DatosRelacionPerfil::new).toList()
        );
    }
}
