package ar.org.hospitalcuencaalta.servicio_entrenamiento.controlador;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.Capacitacion;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.CapacitacionRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EmpleadoRegistry;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.EmpleadoRegistryRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:entrenamiento;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false",
        "eureka.client.enabled=false",
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.kafka.consumer.group-id=test",
        "spring.kafka.listener.auto-startup=false"
})
@AutoConfigureMockMvc
@TestConstructor(autowireMode = AutowireMode.ALL)
class CapacitacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CapacitacionRepository repo;

    @Autowired
    private EmpleadoRegistryRepository empleadoRepo;

    @MockitoBean
    private KafkaTemplate<String, Object> kafka;

    @Test
    void crearCapacitacion_debePersistirEnBDyPublicarEvento() throws Exception {
        EmpleadoRegistry empleado = EmpleadoRegistry.builder()
                .id(1L)
                .legajo("A1")
                .nombre("Juan")
                .apellido("Perez")
                .build();
        empleadoRepo.save(empleado);

        CapacitacionDto dto = CapacitacionDto.builder()
                .nombreCurso("Curso Java")
                .institucion("UBA")
                .estado("planificada")
                .empleadoId(empleado.getId())
                .build();

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/capacitaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertThat(repo.count()).isEqualTo(1);
        Capacitacion entidad = repo.findAll().get(0);
        assertThat(entidad.getNombreCurso()).isEqualTo("Curso Java");
        assertThat(entidad.getEmpleadoId()).isEqualTo(empleado.getId());

        verify(kafka, times(1)).send(eq("servicioEntrenamiento.scheduled"), any());
    }
}
