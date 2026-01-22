package alvino.dev.challenge_forohub.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DatosDetalleRespuesta(
        Long id,
        String mensaje,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime fechaCreacion,
        Boolean solucion,
        Long topicoId,
        DatosRelacionAutor autor
) {
    public DatosDetalleRespuesta(Respuesta respuesta) {
        this(
            respuesta.getId(),
            respuesta.getMensaje(),
            respuesta.getFechaCreacion(),
            respuesta.getSolucion(),
            respuesta.getTopico().getId(),
            new DatosRelacionAutor(respuesta.getAutor())
        );
    }
}
