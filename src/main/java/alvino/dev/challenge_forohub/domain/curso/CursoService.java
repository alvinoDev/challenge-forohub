package alvino.dev.challenge_forohub.domain.curso;

import alvino.dev.challenge_forohub.domain.topico.DatosRespuestaTopico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public DatosRespuestaCurso create(DatosRegistroCurso datos) {
        if(cursoRepository.existsByNombre(datos.nombre())) {
            throw new RuntimeException("Curso Ya existe");
        }

        Curso curso = new Curso(datos);
        cursoRepository.save(curso);
        return new DatosRespuestaCurso(curso);
    }

    public Page<DatosRespuestaCurso> findAll(Pageable pageable) {
        return cursoRepository.findAll(pageable).map(DatosRespuestaCurso::new);
    }

    public DatosRespuestaCurso getById(Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        return new DatosRespuestaCurso(curso);
    }
}
