package alvino.dev.challenge_forohub.domain.usuario;

import alvino.dev.challenge_forohub.domain.perfil.Perfil;

public record DatosRelacionPerfil(
        Long id,
        String nombre
) {
    public DatosRelacionPerfil(Perfil perfil) {
        this(
            perfil.getId(),
            perfil.getNombre()
        );
    }
}
