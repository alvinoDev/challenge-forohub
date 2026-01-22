package alvino.dev.challenge_forohub.domain.topico;

import alvino.dev.challenge_forohub.domain.curso.Curso;
import alvino.dev.challenge_forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Topico")
@Table(name = "topico")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EstadoTopico status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;  // Referencia a Usuario (agrega @ManyToOne después)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;  // Referencia a Curso

    private Boolean activo;

    // Constructor para registro (usado en service)
    public Topico(DatosRegistroTopico datos, Usuario autor, Curso curso) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.status = EstadoTopico.NO_RESPONDIDO;  // Valor por defecto
        this.autor = autor;
        this.curso = curso;
        this.activo = true;
    }

    // Método para actualizar
    public void updateData(DatosActualizarTopico datos) {
        if (datos.titulo() != null) this.titulo = datos.titulo();
        if (datos.mensaje() != null) this.mensaje = datos.mensaje();
        if (datos.status() != null) this.status = datos.status();
    }
}
