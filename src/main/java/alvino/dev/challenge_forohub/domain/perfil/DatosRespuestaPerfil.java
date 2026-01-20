package alvino.dev.challenge_forohub.domain.perfil;

import alvino.dev.challenge_forohub.domain.curso.Curso;

public record DatosRespuestaPerfil(
        Long id,
        String nombre
) {
    public DatosRespuestaPerfil(Perfil perfil) {
        this(
                perfil.getId(),
                perfil.getNombre()
        );
    }
}
