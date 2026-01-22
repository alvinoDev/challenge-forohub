package alvino.dev.challenge_forohub.domain.respuesta;

import alvino.dev.challenge_forohub.domain.topico.TopicoRepository;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
