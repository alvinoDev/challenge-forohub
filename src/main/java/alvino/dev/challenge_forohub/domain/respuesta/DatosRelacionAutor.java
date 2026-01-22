package alvino.dev.challenge_forohub.domain.respuesta;

import alvino.dev.challenge_forohub.domain.usuario.Usuario;

public record DatosRelacionAutor(
        Long id,
        String nombre
) {
    public DatosRelacionAutor(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getNombre()
        );
    }
}
