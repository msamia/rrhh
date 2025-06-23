import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EmpleadoRegistry;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EvaluacionDesempeno;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.EvaluacionDesempenoRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.EvaluacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EvaluacionDetalleMapper;

import org.springframework.test.util.ReflectionTestUtils;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EvaluacionMapper;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.YearMonthMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.BeforeEach;


import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionDesempenoRepository repo;
    @Mock
    private KafkaTemplate<String, Object> kafka;
    @Spy
    private EvaluacionMapper mapper = Mappers.getMapper(EvaluacionMapper.class);
    @Spy
    private EvaluacionDetalleMapper detalleMapper = Mappers.getMapper(EvaluacionDetalleMapper.class);
    @Spy
    private YearMonthMapper yearMonthMapper = Mappers.getMapper(YearMonthMapper.class);
    @InjectMocks
    private EvaluacionService service;

    @BeforeEach
    void setupMappers() {
        ReflectionTestUtils.setField(mapper, "yearMonthMapper", yearMonthMapper);
        ReflectionTestUtils.setField(detalleMapper, "yearMonthMapper", yearMonthMapper);
    }


    @Test
    void create_mapsAndPersistsEntityAndPublishesEvent() {
        EvaluacionDto dto = EvaluacionDto.builder()
                .periodo("2024-05")
                .puntaje(80)
                .comentarios("Bueno")
                .empleadoId(3L)
                .evaluadorId(4L)
                .build();

        when(repo.save(any())).thenAnswer(inv -> {
            EvaluacionDesempeno e = inv.getArgument(0);
            return EvaluacionDesempeno.builder()
                    .id(1L)
                    .periodo(e.getPeriodo())
                    .puntaje(e.getPuntaje())
                    .comentarios(e.getComentarios())
                    .empleado(e.getEmpleado())
                    .evaluador(e.getEvaluador())
                    .build();
        });

        EvaluacionDto result = service.create(dto);

        ArgumentCaptor<EvaluacionDesempeno> captor = ArgumentCaptor.forClass(EvaluacionDesempeno.class);
        verify(repo).save(captor.capture());
        EvaluacionDesempeno saved = captor.getValue();
        assertThat(saved.getPeriodo()).isEqualTo(YearMonth.parse(dto.getPeriodo()));
        assertThat(result.getId()).isEqualTo(1L);
        verify(kafka).send(eq("servicioEntrenamiento.evaluated"), eq(result));
    }

    @Test
    void findAll_returnsMappedDtos() {
        EvaluacionDesempeno e1 = EvaluacionDesempeno.builder().id(1L).periodo(YearMonth.of(2024, 1)).build();
        EvaluacionDesempeno e2 = EvaluacionDesempeno.builder().id(2L).periodo(YearMonth.of(2024, 2)).build();
        when(repo.findAll()).thenReturn(List.of(e1, e2));

        List<EvaluacionDto> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPeriodo()).isEqualTo("2024-01");
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void getDetalle_returnsDetailedDto() {
        EmpleadoRegistry emp = EmpleadoRegistry.builder().id(5L).nombre("Ana").apellido("Lopez").legajo("B1").build();
        EmpleadoRegistry eval = EmpleadoRegistry.builder().id(6L).nombre("Luis").apellido("Gomez").legajo("B2").build();
        EvaluacionDesempeno entity = EvaluacionDesempeno.builder()
                .id(1L)
                .periodo(YearMonth.of(2024, 3))
                .empleado(emp)
                .evaluador(eval)
                .build();
        when(repo.findById(1L)).thenReturn(Optional.of(entity));

        EvaluacionDetalleDto dto = service.getDetalle(1L);

        assertThat(dto.getPeriodo()).isEqualTo("2024-03");
        EmpleadoRegistryDto empDto = dto.getEmpleado();
        assertThat(empDto.getId()).isEqualTo(emp.getId());
        assertThat(dto.getEvaluador().getId()).isEqualTo(eval.getId());
    }
}
