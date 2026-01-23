package alvino.dev.challenge_forohub.domain.respuesta;

import alvino.dev.challenge_forohub.domain.topico.EstadoTopico;
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

    @Transactional
    public DatosDetalleRespuesta update(Long id, DatosActualizarRespuesta datos) {
        Respuesta respuesta = respuestaRepository.findByIdAndActivoTrue(id).orElseThrow(() -> new RuntimeException("Respuesta no encontrado o inactivo"));
        respuesta.updateData(datos);

        // respuestaRepository.save(respuesta);  ← No es necesario con @Transactional + dirty checking
        return new DatosDetalleRespuesta(respuesta);
    }

    @Transactional
    public DatosDetalleRespuesta marcarComoSolucion(Long id, Long usuarioIdAutenticado) {
        var respuesta = respuestaRepository.findByIdAndActivoTrue(id).orElseThrow(() -> new RuntimeException("Respuesta no encontrada o inactiva"));

        // Obtenemos el tópico asociado y cambiamos su estado
        // JPA mantiene la relación, así que 'respuesta.getTopico()' nos da el objeto real
        var topico = respuesta.getTopico();

        // Solo el autor del Tópico decide la solución [Con Spring Security, no necesitaremos pasar el Autor-Id manualmente]
        if (!topico.getAutor().getId().equals(usuarioIdAutenticado)) {
            throw new RuntimeException("Solo el autor del tópico puede marcar una respuesta como solución");
        }

        // No permitir doble solución
        if (topico.getStatus() == EstadoTopico.RESUELTO) {
            throw new RuntimeException("Este tópico ya ha sido marcado como resuelto");
        }

        topico.marcarComoResuelto();

        respuesta.marcarComoSolucion();

        // No hace falta repository.save, @Transactional hará el "Dirty Checking"
        return new DatosDetalleRespuesta(respuesta);
    }

    @Transactional
    public void delete(Long id, Long usuarioIdAutenticado) {
        Respuesta respuesta = respuestaRepository.findByIdAndActivoTrue(id).orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        // Solo el autor de la respuesta puede borrarla
        if (!respuesta.getAutor().getId().equals(usuarioIdAutenticado)) {
            throw new RuntimeException("No tienes permiso para eliminar esta respuesta");
        }

        // Si la respuesta era la solución, "reabrimos" el tópico
        if (respuesta.getSolucion()) {
            respuesta.getTopico().setStatus(EstadoTopico.NO_RESPONDIDO); // O el estado inicial que uses
        }

        respuesta.softDelete();
        // No necesitas save() con @Transactional
    }
}
