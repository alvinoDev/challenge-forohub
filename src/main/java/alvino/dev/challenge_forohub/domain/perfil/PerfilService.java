package alvino.dev.challenge_forohub.domain.perfil;

import org.springframework.beans.factory.annotation.Autowired;
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
}
