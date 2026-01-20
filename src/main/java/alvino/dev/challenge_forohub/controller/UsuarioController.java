package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.usuario.DatosRegistroUsuario;
import alvino.dev.challenge_forohub.domain.usuario.DatosRespuestaUsuario;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrar(@RequestBody @Valid DatosRegistroUsuario datos, UriComponentsBuilder uriBuilder) {
        DatosRespuestaUsuario respuesta = usuarioService.create(datos);
        URI url = uriBuilder.path("/usuarios/{id}").buildAndExpand(respuesta.id()).toUri();
        return ResponseEntity.created(url).body(respuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaUsuario>> list(@PageableDefault(size = 10, page = 0, sort = {"nombre"}) Pageable pageable) {
        var data = usuarioService.findAll(pageable);
        return ResponseEntity.ok(data);
    }
}
