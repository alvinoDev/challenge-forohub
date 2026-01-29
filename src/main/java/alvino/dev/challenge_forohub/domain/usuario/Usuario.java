package alvino.dev.challenge_forohub.domain.usuario;

import alvino.dev.challenge_forohub.domain.perfil.Perfil;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Usuario")
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "correo_electronico", unique = true)
    private String correoElectronico;
    private String contrasena;
    private Boolean activo;

    @ManyToMany(fetch = FetchType.EAGER) // LAZY
    @JoinTable(
            name = "usuario_perfil",  // Tabla intermedia
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private List<Perfil> perfiles = new ArrayList<>();  // relación muchos-a-muchos

    // Constructor para registro
    public Usuario(DatosRegistroUsuario datos) {
        this.nombre = datos.nombre();
        this.correoElectronico = datos.correoElectronico();
        this.contrasena = datos.contrasena();  // Temporal: texto plano; en etapa 2, hash
        this.activo = true;
    }

    // Método para agregar perfiles
    public void agregarPerfil(Perfil perfil) {
        this.perfiles.add(perfil);
    }

    // Método para actualizar
    public void updateData(DatosActualizarUsuario datos) {
        if (datos.nombre() != null) this.nombre = datos.nombre();
        if (datos.contrasena() != null) this.contrasena = datos.contrasena();  // En etapa 2: hash
    }

    // Eliminación lógica
    public void softDelete() { this.activo = false; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfiles.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getNombre()))  // ej: "ROLE_USUARIO"
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return this.contrasena;
    }

    @Override
    public String getUsername() {
        return this.correoElectronico;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired(); // return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked(); // return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired(); // return true;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();// return this.activo;
    }

    public void setContrasena(@Nullable String contrasena) {
        this.contrasena = contrasena;
    }
}
