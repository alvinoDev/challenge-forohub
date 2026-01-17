package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.topico.DatosRegistroTopico;
import alvino.dev.challenge_forohub.domain.topico.DatosRespuestaTopico;
import alvino.dev.challenge_forohub.domain.topico.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println(datos);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(resp.id()).toUri();
        return ResponseEntity.created(url).body(resp);
    }

    @GetMapping
    public String list() {
        return "Lista de topicos";
    }

    @GetMapping("/{id}")
    public void getById() {
        System.out.println("read");
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
