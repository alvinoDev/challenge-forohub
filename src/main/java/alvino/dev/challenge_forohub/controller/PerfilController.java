package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.curso.DatosRespuestaCurso;
import alvino.dev.challenge_forohub.domain.perfil.DatosRegistroPerfil;
import alvino.dev.challenge_forohub.domain.perfil.DatosRespuestaPerfil;
import alvino.dev.challenge_forohub.domain.perfil.PerfilService;
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
@RequestMapping("/perfiles")
public class PerfilController {
    @Autowired
    private PerfilService perfilService;

    @PostMapping
    public ResponseEntity<DatosRespuestaPerfil> create(@RequestBody @Valid DatosRegistroPerfil datos, UriComponentsBuilder uriBuilder) {
        DatosRespuestaPerfil resp = perfilService.create(datos);
        URI url = uriBuilder.path("/perfiles/{id}").buildAndExpand(resp.id()).toUri();
        return ResponseEntity.created(url).body(resp);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaPerfil>> list(@PageableDefault(size = 10, page = 0, sort = {"nombre"}) Pageable pageable) {
        var data = perfilService.findAll(pageable);
        return ResponseEntity.ok(data);
    }
}
