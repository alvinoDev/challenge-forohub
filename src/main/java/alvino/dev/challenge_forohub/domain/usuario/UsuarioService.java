package alvino.dev.challenge_forohub.domain.usuario;

import alvino.dev.challenge_forohub.domain.perfil.Perfil;
import alvino.dev.challenge_forohub.domain.perfil.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<DatosRespuestaUsuario> findAll(Pageable pageable) {
        return usuarioRepository.findAllWithPerfiles(pageable).map(DatosRespuestaUsuario::new);
    }

    public DatosRespuestaUsuario getById(Long id) {
        var usuario = usuarioRepository.findByIdWithPerfiles(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new DatosRespuestaUsuario(usuario);
    }

    @Transactional
    public DatosRespuestaUsuario update(Long id, DatosActualizarUsuario datos) {
        var usuario = usuarioRepository.findByIdWithPerfiles(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.updateData(datos);

        // Actualiza perfiles si se envían
        if (datos.perfilIds() != null) {
            usuario.getPerfiles().clear();  // Borra actuales | y/o ya los cargamos con JOIN FETCH
            for (Long perfilId : datos.perfilIds()) {
                Perfil perfil = perfilRepository.findById(perfilId).orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilId));
                usuario.agregarPerfil(perfil);
            }
        }
        // usuarioRepository.save(usuario); // No afecta el resultado final, pero es innecesario si ya tengo @Transactional

        // Hibernate detecta los cambios y hace el commit solo.
        return new DatosRespuestaUsuario(usuario);
    }

    @Transactional
    public void delete(Long id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.softDelete();

        // usuarioRepository.save(usuario); // No afecta el resultado final, pero es innecesario si ya tengo @Transactional
        // Al terminar el método, Hibernate enviará el UPDATE automáticamente
    }
}
