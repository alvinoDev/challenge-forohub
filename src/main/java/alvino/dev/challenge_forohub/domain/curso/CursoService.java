package alvino.dev.challenge_forohub.domain.curso;

import alvino.dev.challenge_forohub.infra.exceptions.ValidacionDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public DatosRespuestaCurso create(DatosRegistroCurso datos) {
        if(cursoRepository.existsByNombre(datos.nombre())) {
            throw new RuntimeException("Curso Ya existe");
        }

        var curso = new Curso(datos);
        cursoRepository.save(curso);
        return new DatosRespuestaCurso(curso);
    }

    public Page<DatosRespuestaCurso> findAll(Pageable pageable) {
        return cursoRepository.findAllByActivoTrue(pageable).map(DatosRespuestaCurso::new);
    }

    public DatosRespuestaCurso getById(Long id) {
        var curso = cursoRepository.findById(id).orElseThrow(() -> new ValidacionDeNegocioException("Curso no encontrado"));
        return new DatosRespuestaCurso(curso);
    }

    public DatosRespuestaCurso update(Long id, DatosActualizarCurso datos) {
        var curso = cursoRepository.findById(id).orElseThrow(() -> new ValidacionDeNegocioException("Curso no encontrado"));
        curso.update(datos);
        cursoRepository.save(curso);
        return new DatosRespuestaCurso(curso);
    }

    public void delete(Long id) {
        var curso = cursoRepository.findById(id).orElseThrow(() -> new ValidacionDeNegocioException("Curso no encontrado"));
        curso.softDelete();
        cursoRepository.save(curso);
    }
}
