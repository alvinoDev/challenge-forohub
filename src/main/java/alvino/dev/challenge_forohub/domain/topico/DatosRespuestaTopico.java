package alvino.dev.challenge_forohub.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fechaCreacion,

        EstadoTopico status,
        DatosRelacionAutor autor,
        DatosRelacionCurso curso
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
            topico.getId(),
            topico.getTitulo(),
            topico.getMensaje(),
            topico.getFechaCreacion(),
            topico.getStatus(),
            new DatosRelacionAutor(topico.getAutor()),
            new DatosRelacionCurso(topico.getCurso())
        );
    }
}
