package alvino.dev.challenge_forohub.domain.topico;

import alvino.dev.challenge_forohub.domain.curso.Curso;
import alvino.dev.challenge_forohub.domain.curso.CursoRepository;
import alvino.dev.challenge_forohub.domain.usuario.Usuario;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public DatosRespuestaTopico create(DatosRegistroTopico datos) {
        if( topicoRepository.existsByTitulo(datos.titulo()) ){
            throw new RuntimeException("Titulo duplicado");
        }

        Usuario autor = usuarioRepository.findById(datos.autorId()).orElseThrow(() -> new RuntimeException("Autor no encontrado"));
        Curso curso = cursoRepository.findById(datos.cursoId()).orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Topico topico = new Topico(datos, autor, curso);

        topicoRepository.save(topico);
        return new DatosRespuestaTopico(topico);
    }

    public Page<DatosRespuestaTopico> findAll(Pageable pageable) {
        return topicoRepository.findByActivoTrue(pageable).map(DatosRespuestaTopico::new);
    }

    @Transactional(readOnly = true)
    public Page<DatosRespuestaTopico> listarFiltrado(String nombreCurso, Integer anio, Pageable pageable) {

        // Si no hay filtros, devolvemos todos los activos
        if (nombreCurso == null && anio == null) {
            return topicoRepository.findByActivoTrue(pageable).map(DatosRespuestaTopico::new);
        }

        // Construimos especificación dinámica
        Specification<Topico> spec = Specification.where(TopicoSpecifications.activoTrue());

        if (nombreCurso != null && !nombreCurso.isBlank()) {
            spec = spec.and(TopicoSpecifications.conCursoNombre(nombreCurso));
        }

        if (anio != null) {
            spec = spec.and(TopicoSpecifications.enAnio(anio));
        }

        return topicoRepository.findAll(spec, pageable).map(DatosRespuestaTopico::new);
    }

    public DatosRespuestaTopico findById(Long id) {
        var topico = topicoRepository.findByIdAndActivoTrue(id).orElseThrow(() -> new RuntimeException("Tópico no encontrado o inactivo"));
        return new DatosRespuestaTopico(topico);
    }


}
