package alvino.dev.challenge_forohub.domain.perfil;

import alvino.dev.challenge_forohub.domain.curso.DatosRespuestaCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    @Autowired
    private PerfilRepository perfilRepository;

    public DatosRespuestaPerfil create(DatosRegistroPerfil datos) {
        if(perfilRepository.existsByNombre(datos.nombre())) {
            throw new RuntimeException("El PERFIL ya existe");
        }

        var perfil = new Perfil(datos);
        perfilRepository.save(perfil);
        return new DatosRespuestaPerfil(perfil);
    }

    public Page<DatosRespuestaPerfil> findAll(Pageable pageable) {
        return perfilRepository.findAll(pageable).map(DatosRespuestaPerfil::new);
    }

    public DatosRespuestaPerfil getById(Long id) {
        var curso = perfilRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        return new DatosRespuestaPerfil(curso);
    }
}
