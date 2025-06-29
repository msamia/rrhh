package ar.org.hospitalcuencaalta.servicio_nomina.servicio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.LiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.ConceptoLiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.EmpleadoConceptoRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.LiquidacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.LiquidacionMapper;
import ar.org.hospitalcuencaalta.servicio_nomina.feign.EmpleadoClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiquidacionServiceTest {

    @Mock
    private LiquidacionRepository repo;
    @Mock
    private ConceptoLiquidacionRepository conceptoRepo;
    @Mock
    private EmpleadoConceptoRepository empleadoConceptoRepo;
    @Mock
    private EmpleadoClient empleadoClient;
    @Mock
    private KafkaTemplate<String, Object> kafka;
    @Spy
    private LiquidacionMapper mapper = Mappers.getMapper(LiquidacionMapper.class);
    @Spy
    private LiquidacionDetalleMapper detalleMapper = Mappers.getMapper(LiquidacionDetalleMapper.class);

    @InjectMocks
    private LiquidacionService service;

    @BeforeEach
    void setup() {
        // detalleMapper doesn't have other dependencies
    }

    @Test
    void create_whenDuplicatePeriodForEmployee_throwsBadRequest() {
        LiquidacionDto dto = LiquidacionDto.builder()
                .periodo("2024-05")
                .empleadoId(10L)
                .build();

        when(empleadoClient.getById(10L)).thenReturn(null);
        when(repo.findByPeriodoAndEmpleadoId("2024-05", 10L))
                .thenReturn(Optional.of(new Liquidacion()));

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ya existe una liquidacion");

        verify(repo, never()).save(any());
    }
}
