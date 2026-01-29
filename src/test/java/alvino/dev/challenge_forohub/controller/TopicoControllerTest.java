package alvino.dev.challenge_forohub.controller;

import alvino.dev.challenge_forohub.domain.curso.Categoria;
import alvino.dev.challenge_forohub.domain.curso.Curso;
import alvino.dev.challenge_forohub.domain.curso.CursoRepository;
import alvino.dev.challenge_forohub.domain.perfil.Perfil;
import alvino.dev.challenge_forohub.domain.perfil.PerfilRepository;
import alvino.dev.challenge_forohub.domain.topico.*;
import alvino.dev.challenge_forohub.domain.usuario.Usuario;
import alvino.dev.challenge_forohub.domain.usuario.UsuarioRepository;
import alvino.dev.challenge_forohub.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // Usa application-test.yml (BD H2)
@Transactional  // Rollback automático después de cada test
class TopicoControllerTest {

    @Autowired  // ← Todo es REAL, no mocks
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;
    private Usuario autorTest;
    private Curso cursoTest;

    @BeforeEach
    void setUp() {
        // Esto se ejecuta antes de cada test

        // Limpiar BD (por si acaso)
        topicoRepository.deleteAll();
        usuarioRepository.deleteAll();
        cursoRepository.deleteAll();
        perfilRepository.deleteAll();

        // Crear PERFIL/ROL primero
        Perfil perfilModerador = new Perfil();
        perfilModerador.setNombre("ROLE_MODERADOR");  // ✅ Nombre del rol
        perfilModerador = perfilRepository.save(perfilModerador);

        // Crear un curso
        cursoTest = new Curso();
        cursoTest.setNombre("Java Spring Boot");
        cursoTest.setCategoria(Categoria.BACK_END);
        cursoTest = cursoRepository.save(cursoTest);  // Guarda en BD H2

        // Crear un usuario
        autorTest = new Usuario();
        autorTest.setCorreoElectronico("test@dev.com");
        autorTest.setNombre("Test User");
        autorTest.setContrasena(passwordEncoder.encode("password123"));
        autorTest.setPerfiles(List.of(perfilModerador));
        autorTest = usuarioRepository.save(autorTest);  // Guarda en BD H2

        // Generar token JWT real
        jwtToken = "Bearer " + tokenService.generateToken(autorTest);
    }

    // ==========| Crear tópico con datos válidos
    @Test
    @DisplayName("POST /topicos - Debería crear un tópico exitosamente (201)")
    void crear_topico_con_datos_validos() throws Exception {
        // Given (preparar datos)
        DatosRegistroTopico datos = new DatosRegistroTopico(
                "¿Cómo usar Spring Security?",
                "Necesito ayuda con la configuración",
                autorTest.getId(),
                cursoTest.getId()
        );

        // When & Then (ejecutar y verificar)
        mockMvc.perform(post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(datos))
                        .header("Authorization", jwtToken))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value(datos.titulo()))
                .andExpect(jsonPath("$.mensaje").value(datos.mensaje()))
                .andExpect(jsonPath("$.status").value("NO_RESPONDIDO"));

        // ESTO PRUEBA:
        // El controller recibe la petición
        // Spring Security valida el token
        // Las validaciones de @Valid pasan
        // El service crea el tópico
        // El repository guarda en BD
        // Se retorna la respuesta correcta
    }

    // ==========| Datos inválidos
    @Test
    @DisplayName("POST /topicos - Debería retornar 400 con datos inválidos")
    void crear_topico_con_datos_invalidos() throws Exception {
        // Given
        DatosRegistroTopico datosInvalidos = new DatosRegistroTopico(
                "",
                "",
                null,
                null
        );

        // When & Then
        mockMvc.perform(post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(datosInvalidos))
                        .header("Authorization", jwtToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());  // Mensaje de error

        // ESTO PRUEBA:
        // Las validaciones de @Valid funcionan
        // Spring retorna 400 automáticamente
        // El service NO se ejecuta (validación falla antes)
    }

    // ==========| Sin autenticación
    @Test
    @DisplayName("POST /topicos - Debería retornar 403 sin token")
    void crear_topico_sin_token() throws Exception {
        // Given
        DatosRegistroTopico datos = new DatosRegistroTopico(
                "Título",
                "Mensaje",
                autorTest.getId(),
                cursoTest.getId()
        );

        // When & Then
        mockMvc.perform(post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(datos)))
                // Sin header Authorization
                .andExpect(status().isForbidden());  // Espera 403

        // ESTO PRUEBA:
        // Spring Security bloquea peticiones sin token
    }

    // ==========| Listar tópicos
    @Test
    @DisplayName("GET /topicos - Debería listar todos los tópicos")
    void listar_topicos() throws Exception {
        // Given
        crearTopicoEnBD("Título 1", "Mensaje 1");
        crearTopicoEnBD("Título 2", "Mensaje 2");

        // When & Then
        mockMvc.perform(get("/topicos")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].titulo").value("Título 1"))
                .andExpect(jsonPath("$.content[1].titulo").value("Título 2"));
    }

    // ==========| Obtener por ID
    @Test
    @DisplayName("GET /topicos/{id} - Debería retornar tópico por ID")
    void obtener_topico_por_id() throws Exception {
        // Given
        Topico topico = crearTopicoEnBD("Título Test", "Mensaje Test");

        // When & Then
        mockMvc.perform(get("/topicos/{id}", topico.getId())
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(topico.getId()))
                .andExpect(jsonPath("$.titulo").value("Título Test"));
    }

    // ==========| Actualizar tópico
    @Test
    @DisplayName("PUT /topicos/{id} - Debería actualizar tópico")
    void actualizar_topico() throws Exception {
        // Given
        Topico topico = crearTopicoEnBD("Título Original", "Mensaje Original");

        DatosActualizarTopico datosActualizar = new DatosActualizarTopico(
                "Título Actualizado",
                "Mensaje Actualizado",
                EstadoTopico.RESUELTO
        );

        // When & Then
        mockMvc.perform(put("/topicos/{id}", topico.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(datosActualizar))
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título Actualizado"))
                .andExpect(jsonPath("$.status").value("RESUELTO"));
    }

    // ==========| Eliminar tópico
    @Test
    @DisplayName("DELETE /topicos/{id} - Debería eliminar (soft delete) tópico")
    void eliminar_topico() throws Exception {
        // Given
        Topico topico = crearTopicoEnBD("Título a eliminar", "Mensaje del topico");

        // When & Then
        mockMvc.perform(delete("/topicos/{id}", topico.getId())
                        .header("Authorization", jwtToken))
                .andExpect(status().isNoContent());

        // Verificar que fue soft delete (activo = false)
        Topico topicoEliminado = topicoRepository.findById(topico.getId()).get();
        assert !topicoEliminado.getActivo();
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Topico crearTopicoEnBD(String titulo, String mensaje) {
        Topico topico = new Topico();
        topico.setTitulo(titulo);
        topico.setMensaje(mensaje);
        topico.setStatus(EstadoTopico.NO_RESPONDIDO);
        topico.setAutor(autorTest);
        topico.setCurso(cursoTest);
        topico.setActivo(true);
        return topicoRepository.save(topico);
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}