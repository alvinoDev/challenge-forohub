package alvino.dev.challenge_forohub.domain.topico;

import alvino.dev.challenge_forohub.domain.curso.Curso;
import alvino.dev.challenge_forohub.domain.curso.CursoRepository;
import alvino.dev.challenge_forohub.domain.usuario.Usuario;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TopicoServiceTest {
    @Mock
    private TopicoRepository topicoRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TopicoService topicoService;

    private Usuario autorTest;
    private Curso cursoTest;
    private DatosRegistroTopico datosRegistro;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        autorTest = crearUsuarioTest(1L, "usuario@test.com", "Usuario Test");
        cursoTest = crearCursoTest(1L, "Java Spring Boot");
        datosRegistro = new DatosRegistroTopico(
                "¿Cómo usar Spring Security?",
                "Necesito ayuda con la configuración",
                1L, // autorId
                1L  // cursoId
        );
    }

    // ==========| TESTS DE CREATE
    @Test
    @DisplayName("Debería crear un tópico exitosamente")
    void create_DeberiaCrearTopico_CuandoDatosValidos() {
        // Given
        when(topicoRepository.existsByTitulo(datosRegistro.titulo())).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autorTest));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoTest));

        Topico topicoGuardado = crearTopicoTest(1L, datosRegistro.titulo(), datosRegistro.mensaje());
        when(topicoRepository.save(any(Topico.class))).thenReturn(topicoGuardado);

        // When
        DatosRespuestaTopico resultado = topicoService.create(datosRegistro);

        // Then
        assertThat(resultado).isNotNull();
        //assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.titulo()).isEqualTo(datosRegistro.titulo());

        verify(topicoRepository, times(1)).existsByTitulo(datosRegistro.titulo());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).findById(1L);
        verify(topicoRepository, times(1)).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el título está duplicado")
    void create_DeberiaLanzarExcepcion_CuandoTituloDuplicado() {
        // Given
        when(topicoRepository.existsByTitulo(datosRegistro.titulo())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> topicoService.create(datosRegistro))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Titulo duplicado");

        verify(topicoRepository, times(1)).existsByTitulo(datosRegistro.titulo());
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el autor no existe")
    void create_DeberiaLanzarExcepcion_CuandoAutorNoExiste() {
        // Given
        when(topicoRepository.existsByTitulo(datosRegistro.titulo())).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> topicoService.create(datosRegistro))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Autor no encontrado");

        verify(usuarioRepository, times(1)).findById(1L);
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el curso no existe")
    void create_DeberiaLanzarExcepcion_CuandoCursoNoExiste() {
        // Given
        when(topicoRepository.existsByTitulo(datosRegistro.titulo())).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autorTest));
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> topicoService.create(datosRegistro))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Curso no encontrado");

        verify(cursoRepository, times(1)).findById(1L);
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    // ==========| TESTS DE FINDALL
    @Test
    @DisplayName("Debería listar todos los tópicos activos con paginación")
    void findAll_DeberiaRetornarTopicosActivos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Topico topico1 = crearTopicoTest(1L, "Título 1", "Mensaje 1");
        Topico topico2 = crearTopicoTest(2L, "Título 2", "Mensaje 2");
        Page<Topico> pageTopicos = new PageImpl<>(List.of(topico1, topico2));

        when(topicoRepository.findByActivoTrue(pageable)).thenReturn(pageTopicos);

        // When
        Page<DatosRespuestaTopico> resultado = topicoService.findAll(pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(2);
        assertThat(resultado.getContent().get(0).titulo()).isEqualTo("Título 1");
        assertThat(resultado.getContent().get(1).titulo()).isEqualTo("Título 2");

        verify(topicoRepository, times(1)).findByActivoTrue(pageable);
    }

    // ==========| TESTS DE LISTAR FILTRADO
    @Test
    @DisplayName("Debería listar sin filtros cuando no se especifican parámetros")
    void listarFiltrado_DeberiaListarTodos_CuandoNoHayFiltros() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Topico topico = crearTopicoTest(1L, "Título", "Mensaje");
        Page<Topico> pageTopicos = new PageImpl<>(List.of(topico));

        when(topicoRepository.findByActivoTrue(pageable)).thenReturn(pageTopicos);

        // When
        Page<DatosRespuestaTopico> resultado = topicoService.listarFiltrado(null, null, pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        verify(topicoRepository, times(1)).findByActivoTrue(pageable);
        verify(topicoRepository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Debería filtrar por nombre de curso")
    void listarFiltrado_DeberiaFiltrarPorCurso() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String nombreCurso = "Java";
        Topico topico = crearTopicoTest(1L, "Título Java", "Mensaje");
        Page<Topico> pageTopicos = new PageImpl<>(List.of(topico));

        when(topicoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pageTopicos);

        // When
        Page<DatosRespuestaTopico> resultado = topicoService.listarFiltrado(nombreCurso, null, pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        verify(topicoRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Debería filtrar por año")
    void listarFiltrado_DeberiaFiltrarPorAnio() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Integer anio = 2024;
        Topico topico = crearTopicoTest(1L, "Título 2024", "Mensaje");
        Page<Topico> pageTopicos = new PageImpl<>(List.of(topico));

        when(topicoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pageTopicos);

        // When
        Page<DatosRespuestaTopico> resultado = topicoService.listarFiltrado(null, anio, pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        verify(topicoRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Debería filtrar por curso y año simultáneamente")
    void listarFiltrado_DeberiaFiltrarPorCursoYAnio() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String nombreCurso = "Spring";
        Integer anio = 2024;
        Topico topico = crearTopicoTest(1L, "Título Spring 2024", "Mensaje");
        Page<Topico> pageTopicos = new PageImpl<>(List.of(topico));

        when(topicoRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(pageTopicos);

        // When
        Page<DatosRespuestaTopico> resultado = topicoService.listarFiltrado(nombreCurso, anio, pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        verify(topicoRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    // ==========| TESTS DE FINDBYID
    @Test
    @DisplayName("Debería encontrar tópico por ID cuando está activo")
    void findById_DeberiaRetornarTopico_CuandoExisteYEstaActivo() {
        // Given
        Long id = 1L;
        Topico topico = crearTopicoTest(id, "Título Test", "Mensaje Test");
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.of(topico));

        // When
        DatosRespuestaTopico resultado = topicoService.findById(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.titulo()).isEqualTo("Título Test");

        verify(topicoRepository, times(1)).findByIdAndActivoTrue(id);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando tópico no existe o está inactivo")
    void findById_DeberiaLanzarExcepcion_CuandoNoExisteOEstaInactivo() {
        // Given
        Long id = 999L;
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> topicoService.findById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tópico no encontrado o inactivo");

        verify(topicoRepository, times(1)).findByIdAndActivoTrue(id);
    }

    // ==========| TESTS DE UPDATE
    @Test
    @DisplayName("Debería actualizar el tópico correctamente")
    void update_DeberiaActualizarTopico_CuandoExisteYEstaActivo() {
        // Given
        Long id = 1L;
        DatosActualizarTopico datosActualizar = new DatosActualizarTopico(
                "Título Actualizado",
                "Mensaje Actualizado",
                EstadoTopico.RESUELTO
        );

        Topico topicoExistente = crearTopicoTest(id, "Título Original", "Mensaje Original");
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.of(topicoExistente));

        // When
        DatosRespuestaTopico resultado = topicoService.update(id, datosActualizar);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.titulo()).isEqualTo("Título Actualizado");
        assertThat(resultado.mensaje()).isEqualTo("Mensaje Actualizado");

        verify(topicoRepository, times(1)).findByIdAndActivoTrue(id);
        // No se debe llamar a save() porque @Transactional usa dirty checking
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    @DisplayName("Debería actualizar solo los campos no nulos")
    void update_DeberiaActualizarSoloCamposNoNulos() {
        // Given
        Long id = 1L;
        DatosActualizarTopico datosActualizar = new DatosActualizarTopico(
                "Nuevo Título",
                null, // mensaje no se actualiza
                null  // status no se actualiza
        );

        Topico topicoExistente = crearTopicoTest(id, "Título Original", "Mensaje Original");
        topicoExistente.setStatus(EstadoTopico.NO_RESPONDIDO);
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.of(topicoExistente));

        // When
        DatosRespuestaTopico resultado = topicoService.update(id, datosActualizar);

        // Then
        assertThat(resultado.titulo()).isEqualTo("Nuevo Título");
        assertThat(resultado.mensaje()).isEqualTo("Mensaje Original"); // No cambió
        assertThat(resultado.status()).isEqualTo(EstadoTopico.NO_RESPONDIDO); // No cambió
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar tópico inexistente")
    void update_DeberiaLanzarExcepcion_CuandoTopicoNoExiste() {
        // Given
        Long id = 999L;
        DatosActualizarTopico datosActualizar = new DatosActualizarTopico(
                "Título",
                "Mensaje",
                EstadoTopico.RESUELTO
        );
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> topicoService.update(id, datosActualizar))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tópico no encontrado o inactivo");
    }

    // ==========| TESTS DE DELETE
    @Test
    @DisplayName("Debería realizar soft delete correctamente")
    void delete_DeberiaDesactivarTopico_CuandoExisteYEstaActivo() {
        // Given
        Long id = 1L;
        Topico topico = crearTopicoTest(id, "Título", "Mensaje");
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.of(topico));

        // When
        topicoService.delete(id);

        // Then
        assertThat(topico.getActivo()).isFalse();
        verify(topicoRepository, times(1)).findByIdAndActivoTrue(id);
        verify(topicoRepository, never()).delete(any(Topico.class)); // No es hard delete
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar tópico inexistente")
    void delete_DeberiaLanzarExcepcion_CuandoTopicoNoExiste() {
        // Given
        Long id = 999L;
        when(topicoRepository.findByIdAndActivoTrue(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> topicoService.delete(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tópico no encontrado o ya inactivo");

        verify(topicoRepository, times(1)).findByIdAndActivoTrue(id);
    }

    // ========== MÉTODOS AUXILIARES ==========
    private Topico crearTopicoTest(Long id, String titulo, String mensaje) {
        Topico topico = new Topico();
        topico.setId(id);
        topico.setTitulo(titulo);
        topico.setMensaje(mensaje);
        topico.setStatus(EstadoTopico.NO_RESPONDIDO);
        topico.setAutor(autorTest);
        topico.setCurso(cursoTest);
        topico.setActivo(true);
        topico.setFechaCreacion(LocalDateTime.now());
        return topico;
    }

    private Usuario crearUsuarioTest(Long id, String email, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreoElectronico(email);
        usuario.setNombre(nombre);
        return usuario;
    }

    private Curso crearCursoTest(Long id, String nombre) {
        Curso curso = new Curso();
        curso.setId(id);
        curso.setNombre(nombre);
        return curso;
    }
}