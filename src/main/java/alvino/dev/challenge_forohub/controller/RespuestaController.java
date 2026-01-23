package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.respuesta.DatosActualizarRespuesta;
import alvino.dev.challenge_forohub.domain.respuesta.DatosDetalleRespuesta;
import alvino.dev.challenge_forohub.domain.respuesta.DatosRegistroRespuesta;
import alvino.dev.challenge_forohub.domain.respuesta.RespuestaService;

import alvino.dev.challenge_forohub.domain.topico.DatosRespuestaTopico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity<Page<DatosDetalleRespuesta>> list(
            @RequestParam(required = false) Long topicoId,
            @RequestParam(required = false) String autor,
            @PageableDefault(size = 10, page = 0, sort = {"fechaCreacion"}, direction = Sort.Direction.ASC)
            Pageable paginacion
    ) {
        var data = respuestaService.findAll(paginacion, topicoId, autor);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleRespuesta> getById(@PathVariable Long id){
        DatosDetalleRespuesta respuesta = respuestaService.findById(id);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosDetalleRespuesta> update(@PathVariable Long id, @RequestBody DatosActualizarRespuesta datos) {
        DatosDetalleRespuesta resp = respuestaService.update(id, datos);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/solucion")
    public ResponseEntity<DatosDetalleRespuesta> marcarSolucion(@PathVariable Long id, @RequestHeader("Autor-Id") Long usuarioIdAutenticado) {
        DatosDetalleRespuesta resp = respuestaService.marcarComoSolucion(id, usuarioIdAutenticado);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Autor-Id") Long usuarioIdAutenticado) {
        respuestaService.delete(id, usuarioIdAutenticado);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
