package alvino.dev.challenge_forohub.domain.topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        EstadoTopico status,
        Long autorId,
        Long cursoId
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
            topico.getId(),
            topico.getTitulo(),
            topico.getMensaje(),
            topico.getFechaCreacion(),
            topico.getStatus(),
            topico.getAutorId(),
            topico.getCursoId()
        );
    }
}
