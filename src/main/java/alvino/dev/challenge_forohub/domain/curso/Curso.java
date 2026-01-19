package alvino.dev.challenge_forohub.domain.curso;

import alvino.dev.challenge_forohub.domain.topico.EstadoTopico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Curso")
@Table(name = "curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)  // Mapea el enum a VARCHAR en BD
    private Categoria categoria;

    // Constructor para registro
    public Curso(DatosRegistroCurso datos) {
        this.nombre = datos.nombre();
        this.categoria = datos.categoria();
    }

    // Método para actualizar registro
    public void update(DatosActualizarCurso datos) {
        if (datos.nombre() != null) this.nombre = datos.nombre();
        if (datos.categoria() != null) this.categoria = datos.categoria();
    }
}
