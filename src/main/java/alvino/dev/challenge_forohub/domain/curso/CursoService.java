package alvino.dev.challenge_forohub.domain.curso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
