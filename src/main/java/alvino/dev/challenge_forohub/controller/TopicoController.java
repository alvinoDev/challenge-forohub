package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.topico.DatosRegistroTopico;
import alvino.dev.challenge_forohub.domain.topico.DatosRespuestaTopico;
import alvino.dev.challenge_forohub.domain.topico.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> create(@RequestBody @Valid DatosRegistroTopico datos, UriComponentsBuilder uriBuilder) {
        DatosRespuestaTopico resp = topicoService.create(datos);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(resp.id()).toUri();
        return ResponseEntity.created(url).body(resp);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> list(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio,
            @PageableDefault (size = 10, page = 0, sort = {"fechaCreacion"}, direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        // var data = topicoService.findAll(pageable);
        // return ResponseEntity.ok(data);

        Page<DatosRespuestaTopico> datos = topicoService.listarFiltrado(curso, anio, pageable);
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> getById(@PathVariable Long id) {
        DatosRespuestaTopico respuesta = topicoService.findById(id);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public void update() {
        System.out.println("update");
    }

    @DeleteMapping("/{id}")
    public void delete() {
        System.out.println("delete");
    }
}
