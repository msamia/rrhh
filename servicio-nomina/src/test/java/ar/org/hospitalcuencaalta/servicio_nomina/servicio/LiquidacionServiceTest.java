package ar.org.hospitalcuencaalta.servicio_nomina.servicio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.LiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.ConceptoLiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.EmpleadoConceptoRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.LiquidacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.LiquidacionMapper;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.EmpleadoRegistryRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.EmpleadoRegistryDto;
import feign.FeignException;
import feign.Request;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private EmpleadoRegistryRepository empleadoRegistryRepo;
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

        when(repo.findByPeriodoAndEmpleadoId("2024-05", 10L))
                .thenReturn(Optional.of(new Liquidacion()));

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ya existe una liquidacion");

        verify(repo, never()).save(any());
        verifyNoInteractions(empleadoClient);
    }

    @Test
    void create_whenEmpleadoNoExiste_throwsNotFound() {
        LiquidacionDto dto = LiquidacionDto.builder()
                .periodo("2024-06")
                .empleadoId(5L)
                .build();

        Request request = Request.create(Request.HttpMethod.GET,
                "/api/empleados/5",
                java.util.Collections.emptyMap(),
                null,
                java.nio.charset.StandardCharsets.UTF_8,
                null);
        when(empleadoClient.getById(5L))
                .thenThrow(new FeignException.NotFound("not found", request, null, null));

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("no existe");

        verify(repo, never()).save(any());
    }

    @Test
    void create_whenEmpleadoSeObtieneRemotamente_guardaLiquidacion() {
        LiquidacionDto dto = LiquidacionDto.builder()
                .periodo("2024-07")
                .empleadoId(7L)
                .build();

        EmpleadoRegistryDto empDto = EmpleadoRegistryDto.builder()
                .id(7L).documento("XD").nombre("Ana").apellido("Lopez").build();
        when(empleadoClient.getById(7L)).thenReturn(empDto);
        when(empleadoRegistryRepo.existsByIdAndDocumento(7L, "XD")).thenReturn(false);
        when(repo.findByPeriodoAndEmpleadoId("2024-07", 7L)).thenReturn(Optional.empty());
        Liquidacion saved = new Liquidacion();
        saved.setId(99L);
        when(repo.save(any())).thenReturn(saved);

        LiquidacionDto result = service.create(dto);

        verify(empleadoRegistryRepo).save(any());
        verify(repo).save(any());
        assertEquals(99L, result.getId());
    }
}
