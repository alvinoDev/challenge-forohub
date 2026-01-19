package alvino.dev.challenge_forohub.domain.curso;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String list() {
        return "List";
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
