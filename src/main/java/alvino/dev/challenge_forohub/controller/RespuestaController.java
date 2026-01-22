package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.respuesta.DatosDetalleRespuesta;
import alvino.dev.challenge_forohub.domain.respuesta.DatosRegistroRespuesta;
import alvino.dev.challenge_forohub.domain.respuesta.RespuestaService;

import alvino.dev.challenge_forohub.domain.topico.DatosRespuestaTopico;
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
@RequestMapping("/respuestas")
public class RespuestaController {
    @Autowired
    private RespuestaService respuestaService;

    @PostMapping
    public ResponseEntity<DatosDetalleRespuesta> create(@RequestBody @Valid DatosRegistroRespuesta datos, UriComponentsBuilder uriBuilder) {
        DatosDetalleRespuesta resp = respuestaService.create(datos);
        URI url = uriBuilder.path("/respuestas/{id}").buildAndExpand(resp.id()).toUri();
        return ResponseEntity.created(url).body(resp);
    }
}
