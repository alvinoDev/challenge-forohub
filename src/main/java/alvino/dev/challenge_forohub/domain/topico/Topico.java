package alvino.dev.challenge_forohub.domain.topico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Topico")
@Table(name = "topico")
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

    private Long autorId;  // Referencia a Usuario (agrega @ManyToOne después)
    private Long cursoId;  // Referencia a Curso

    // Constructor para registro (usado en service)
    public Topico(DatosRegistroTopico datos) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.status = EstadoTopico.NO_RESPONDIDO;  // Valor por defecto
        this.autorId = datos.autorId();
        this.cursoId = datos.cursoId();
    }
}
