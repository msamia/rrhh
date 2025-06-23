import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.Capacitacion;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EmpleadoRegistry;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.CapacitacionRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.CapacitacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.CapacitacionDetalleMapper;
import org.springframework.test.util.ReflectionTestUtils;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.CapacitacionMapper;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EmpleadoRegistryMapper;
    private CapacitacionMapper mapper;
    private CapacitacionDetalleMapper detalleMapper;
    private EmpleadoRegistryMapper empleadoRegistryMapper;

    void setup() {
        repo = mock(CapacitacionRepository.class);
        kafka = mock(KafkaTemplate.class);

        mapper = Mappers.getMapper(CapacitacionMapper.class);
        detalleMapper = Mappers.getMapper(CapacitacionDetalleMapper.class);
        empleadoRegistryMapper = Mappers.getMapper(EmpleadoRegistryMapper.class);


        service = new CapacitacionService();
        ReflectionTestUtils.setField(service, "repo", repo);
        ReflectionTestUtils.setField(service, "mapper", mapper);
        ReflectionTestUtils.setField(service, "detalleMapper", detalleMapper);
        ReflectionTestUtils.setField(service, "kafka", kafka);
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacitacionServiceTest {

    @Mock
    private CapacitacionRepository repo;
    @Mock
    private KafkaTemplate<String, Object> kafka;
    @Spy
    private CapacitacionMapper mapper = Mappers.getMapper(CapacitacionMapper.class);
    @Spy
    private CapacitacionDetalleMapper detalleMapper = Mappers.getMapper(CapacitacionDetalleMapper.class);
    @Spy
    private EmpleadoRegistryMapper empleadoRegistryMapper = Mappers.getMapper(EmpleadoRegistryMapper.class);
    @InjectMocks
    private CapacitacionService service;

    @BeforeEach
    void setupMappers() {
        ReflectionTestUtils.setField(detalleMapper, "empleadoRegistryMapper", empleadoRegistryMapper);
    }

    @Test
    void create_mapsAndPersistsEntityAndPublishesEvent() {
        CapacitacionDto dto = CapacitacionDto.builder()
                .nombreCurso("Java")
                .institucion("UBA")
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusDays(1))
                .estado("planificada")
                .empleadoId(5L)
                .build();

        when(repo.save(any())).thenAnswer(inv -> {
            Capacitacion c = inv.getArgument(0);
            return Capacitacion.builder()
                    .id(1L)
                    .nombreCurso(c.getNombreCurso())
                    .institucion(c.getInstitucion())
                    .fechaInicio(c.getFechaInicio())
                    .fechaFin(c.getFechaFin())
                    .estado(c.getEstado())
                    .empleadoId(c.getEmpleadoId())
                    .build();
        });

        CapacitacionDto result = service.create(dto);

        ArgumentCaptor<Capacitacion> captor = ArgumentCaptor.forClass(Capacitacion.class);
        verify(repo).save(captor.capture());
        Capacitacion saved = captor.getValue();
        assertThat(saved.getNombreCurso()).isEqualTo(dto.getNombreCurso());
        assertThat(saved.getEmpleadoId()).isEqualTo(dto.getEmpleadoId());
        assertThat(result.getId()).isEqualTo(1L);
        verify(kafka).send(eq("servicioEntrenamiento.scheduled"), eq(result));
    }

    @Test
    void findAll_returnsMappedDtos() {
        Capacitacion c1 = Capacitacion.builder().id(1L).nombreCurso("C1").empleadoId(2L).build();
        Capacitacion c2 = Capacitacion.builder().id(2L).nombreCurso("C2").empleadoId(3L).build();
        when(repo.findAll()).thenReturn(List.of(c1, c2));

        List<CapacitacionDto> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(c1.getId());
        assertThat(result.get(1).getNombreCurso()).isEqualTo("C2");
    }

    @Test
    void getDetalle_returnsDetailedDto() {
        EmpleadoRegistry empleado = EmpleadoRegistry.builder()
                .id(10L).legajo("A1").nombre("Juan").apellido("Perez").build();
        Capacitacion entity = Capacitacion.builder()
                .id(1L).nombreCurso("Curso").empleadoId(empleado.getId())
                .empleado(empleado)
                .build();
        when(repo.findById(1L)).thenReturn(Optional.of(entity));

        CapacitacionDetalleDto dto = service.getDetalle(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        EmpleadoRegistryDto empDto = dto.getEmpleado();
        assertThat(empDto.getId()).isEqualTo(empleado.getId());
        assertThat(empDto.getNombre()).isEqualTo(empleado.getNombre());
    }
}

