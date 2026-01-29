package alvino.dev.challenge_forohub.domain.topico;

import alvino.dev.challenge_forohub.domain.curso.Categoria;
import alvino.dev.challenge_forohub.domain.curso.Curso;
import alvino.dev.challenge_forohub.domain.curso.CursoRepository;
import alvino.dev.challenge_forohub.domain.usuario.Usuario;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicoRepositoryTest {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Usuario autorTest;
    private Curso cursoTest;

    @BeforeEach
    void setUp() {
        // Crear datos base para los tests
        autorTest = crearUsuarioTest("usuario@test.com", "Usuario Test");
        cursoTest = crearCursoTest("Java Spring Boot");
    }

    @Test
    @DisplayName("Debería verificar si existe un tópico por título")
    void existsByTitulo_DeberiaRetornarTrue_CuandoExisteElTitulo() {
        // Given
        String titulo = "¿Cómo usar Spring Security?";
        crearTopicoTest(titulo, "Mensaje de prueba", autorTest, cursoTest);

        // When
        boolean existe = topicoRepository.existsByTitulo(titulo);

        // Then
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe el título")
    void existsByTitulo_DeberiaRetornarFalse_CuandoNoExisteElTitulo() {
        // When
        boolean existe = topicoRepository.existsByTitulo("Título que no existe");

        // Then
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Debería verificar duplicados por título y mensaje")
    void existsByTituloAndMensaje_DeberiaRetornarTrue_CuandoExisteAmbos() {
        // Given
        String titulo = "Pregunta sobre JPA";
        String mensaje = "¿Cómo funciona el EntityManager?";
        crearTopicoTest(titulo, mensaje, autorTest, cursoTest);

        // When
        boolean existe = topicoRepository.existsByTituloAndMensaje(titulo, mensaje);

        // Then
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Debería listar solo tópicos activos con paginación")
    void findByActivoTrue_DeberiaRetornarSoloTopicosActivos() {
        // Given
        crearTopicoTest("Tópico Activo 1", "Mensaje 1", autorTest, cursoTest);
        crearTopicoTest("Tópico Activo 2", "Mensaje 2", autorTest, cursoTest);

        Topico topicoInactivo = crearTopicoTest("Tópico Inactivo", "Mensaje 3", autorTest, cursoTest);
        topicoInactivo.softDelete();
        entityManager.persist(topicoInactivo);
        entityManager.flush();

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<Topico> resultado = topicoRepository.findByActivoTrue(pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(2);
        assertThat(resultado.getContent())
                .allMatch(t -> t.getActivo().equals(true));
    }

    @Test
    @DisplayName("Debería encontrar tópico por ID cuando está activo")
    void findByIdAndActivoTrue_DeberiaRetornarTopico_CuandoEstaActivo() {
        // Given
        Topico topico = crearTopicoTest("Test Título", "Test Mensaje", autorTest, cursoTest);

        // When
        var resultado = topicoRepository.findByIdAndActivoTrue(topico.getId());

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Test Título");
        assertThat(resultado.get().getAutor()).isEqualTo(autorTest);
        assertThat(resultado.get().getCurso()).isEqualTo(cursoTest);
    }

    @Test
    @DisplayName("No debería encontrar tópico por ID cuando está inactivo")
    void findByIdAndActivoTrue_DeberiaRetornarVacio_CuandoEstaInactivo() {
        // Given
        Topico topico = crearTopicoTest("Test Título", "Test Mensaje", autorTest, cursoTest);
        topico.softDelete();
        entityManager.persist(topico);
        entityManager.flush();

        // When
        var resultado = topicoRepository.findByIdAndActivoTrue(topico.getId());

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debería cargar autor y curso con JOIN FETCH")
    void findByActivoTrue_DeberiaCargarAutorYCurso() {
        // Given
        crearTopicoTest("Tópico con relaciones", "Mensaje", autorTest, cursoTest);
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<Topico> resultado = topicoRepository.findByActivoTrue(pageable);

        // Then
        assertThat(resultado.getContent()).isNotEmpty();
        Topico topico = resultado.getContent().get(0);

        // Verificar que las relaciones están cargadas (no lazy)
        assertThat(topico.getAutor()).isNotNull();
        assertThat(topico.getCurso()).isNotNull();
        assertThat(topico.getAutor().getNombre()).isNotNull();
        assertThat(topico.getCurso().getNombre()).isNotNull();
    }

    @Test
    @DisplayName("Debería guardar un tópico correctamente")
    void save_DeberiaGuardarTopico() {
        // Given
        Topico topico = new Topico();
        topico.setTitulo("Nuevo Tópico");
        topico.setMensaje("Mensaje del nuevo tópico");
        topico.setStatus(EstadoTopico.NO_RESPONDIDO);
        topico.setAutor(autorTest);
        topico.setCurso(cursoTest);
        topico.setActivo(true);
        topico.setFechaCreacion(LocalDateTime.now());

        // When
        Topico guardado = topicoRepository.save(topico);

        // Then
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getTitulo()).isEqualTo("Nuevo Tópico");
        assertThat(guardado.getActivo()).isTrue();
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Topico crearTopicoTest(String titulo, String mensaje, Usuario autor, Curso curso) {
        Topico topico = new Topico();
        topico.setTitulo(titulo);
        topico.setMensaje(mensaje);
        topico.setStatus(EstadoTopico.NO_RESPONDIDO);
        topico.setAutor(autor);
        topico.setCurso(curso);
        topico.setActivo(true);
        topico.setFechaCreacion(LocalDateTime.now());

        return entityManager.persist(topico);
    }

    private Usuario crearUsuarioTest(String email, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setCorreoElectronico(email);
        usuario.setNombre(nombre);
        usuario.setContrasena("password123");
        // usuario.setPerfiles();

        return entityManager.persist(usuario);
    }

    private Curso crearCursoTest(String nombre) {
        Curso curso = new Curso();
        curso.setNombre(nombre);
        curso.setCategoria(Categoria.PROGRAMACION);

        return entityManager.persist(curso);
    }
}