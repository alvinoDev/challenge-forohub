package alvino.dev.challenge_forohub.domain.curso;

import alvino.dev.challenge_forohub.domain.topico.DatosRespuestaTopico;
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
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<DatosRespuestaCurso> create(@RequestBody @Valid DatosRegistroCurso datos, UriComponentsBuilder uriBuilder) {
        DatosRespuestaCurso resp = cursoService.create(datos);
        URI url = uriBuilder.path("/cursos/{id}").buildAndExpand(resp.id()).toUri();
        return ResponseEntity.created(url).body(resp);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaCurso>> list(@PageableDefault(size = 10, page = 0, sort = {"nombre"}) Pageable pageable) {
        var data = cursoService.findAll(pageable);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaCurso> getById(@PathVariable Long id) {
        var curso = cursoService.getById(id);
        return ResponseEntity.ok(curso);
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
