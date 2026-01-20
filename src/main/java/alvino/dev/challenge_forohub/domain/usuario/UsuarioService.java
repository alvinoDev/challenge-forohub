package alvino.dev.challenge_forohub.domain.usuario;

import alvino.dev.challenge_forohub.domain.perfil.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Transactional
    public DatosRespuestaUsuario create(DatosRegistroUsuario datos) {
        if (usuarioRepository.existsByCorreoElectronico(datos.correoElectronico())) {
            throw new RuntimeException("Correo electrónico ya registrado");
        }
        var usuario = new Usuario(datos);

        // Asigna perfiles
        for (Long perfilId : datos.perfilIds()) {
            var perfil = perfilRepository.findById(perfilId)
                    .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
            usuario.agregarPerfil(perfil);
        }

        usuarioRepository.save(usuario);
        return new DatosRespuestaUsuario(usuario);
    }
}
