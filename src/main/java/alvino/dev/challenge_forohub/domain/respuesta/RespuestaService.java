package alvino.dev.challenge_forohub.domain.respuesta;

import alvino.dev.challenge_forohub.domain.topico.TopicoRepository;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RespuestaService {
    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TopicoRepository topicoRepository;

    @Transactional
    public DatosDetalleRespuesta create(DatosRegistroRespuesta datos) {
        var topico = topicoRepository.findById(datos.topicoId()).orElseThrow(() -> new RuntimeException("Tópico no encontrado"));
        var autor = usuarioRepository.findById(datos.autorId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Respuesta respuesta = new Respuesta(datos, topico, autor);

        respuestaRepository.save(respuesta);
        return new DatosDetalleRespuesta(respuesta);
    }

    public Page<DatosDetalleRespuesta> findAll(Pageable pageable, Long topicoId, String autor) {
        // Si envían topicoId, filtramos por tópico
        if (topicoId != null) {
            return respuestaRepository.findAllByTopicoIdAndActivoTrue(topicoId, pageable)
                    .map(DatosDetalleRespuesta::new);
        }

        // Si envían nombre de autor, filtramos por autor
        if (autor != null && !autor.isBlank()) {
            return respuestaRepository.findAllByAutorNombreContainingAndActivoTrue(autor, pageable)
                    .map(DatosDetalleRespuesta::new);
        }

        // Si no hay filtros, devolvemos todo lo activo
        return respuestaRepository.findAllByActivoTrue(pageable)
                .map(DatosDetalleRespuesta::new);
    }

    public DatosDetalleRespuesta findById(Long id){
        var respuesta = respuestaRepository.findByIdAndActivoTrue(id).orElseThrow(() -> new RuntimeException("Respuesta no encontrado o inactivo"));
        return new DatosDetalleRespuesta(respuesta);
    }
}
