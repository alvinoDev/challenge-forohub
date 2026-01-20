package alvino.dev.challenge_forohub.domain.perfil;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Perfil")
@Table(name = "perfil")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Boolean activo;

    // Constructor para registro
    public Perfil(DatosRegistroPerfil datos) {
        this.nombre = datos.nombre();
        this.activo = true;
    }

    // Método para actualizar registro
    public void updateData(DatosActualizarPerfil datos) {
        if (datos.nombre() != null) this.nombre = datos.nombre();
    }

    // Método para hacer una Eliminación Lógica
    public void softDelete() {
        this.activo = false;
    }
}
