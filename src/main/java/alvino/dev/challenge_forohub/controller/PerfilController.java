package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.curso.DatosRespuestaCurso;
import alvino.dev.challenge_forohub.domain.perfil.DatosRegistroPerfil;
import alvino.dev.challenge_forohub.domain.perfil.DatosRespuestaPerfil;
import alvino.dev.challenge_forohub.domain.perfil.PerfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
