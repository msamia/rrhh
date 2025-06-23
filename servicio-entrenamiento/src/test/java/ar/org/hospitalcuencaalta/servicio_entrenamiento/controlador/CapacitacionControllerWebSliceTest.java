package ar.org.hospitalcuencaalta.servicio_entrenamiento.controlador;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.CapacitacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CapacitacionController.class, properties = "eureka.client.enabled=false")
@TestConstructor(autowireMode = AutowireMode.ALL)
class CapacitacionControllerWebSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CapacitacionService service;

    @Test
    void crearCapacitacion_debeRetornarDto() throws Exception {
        CapacitacionDto dto = CapacitacionDto.builder().id(1L).nombreCurso("Java").build();
        when(service.create(any(CapacitacionDto.class))).thenReturn(dto);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/capacitaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(service, times(1)).create(any(CapacitacionDto.class));
    }

    @Test
    void listarCapacitaciones_debeRetornarLista() throws Exception {
        List<CapacitacionDto> lista = List.of(
                CapacitacionDto.builder().id(1L).build(),
                CapacitacionDto.builder().id(2L).build()
        );
        when(service.findAll()).thenReturn(lista);
        String esperado = objectMapper.writeValueAsString(lista);

        mockMvc.perform(get("/api/capacitaciones"))
                .andExpect(status().isOk())
                .andExpect(content().json(esperado));
    }

    @Test
    void obtenerDetalle_debeUsarServicio() throws Exception {
        CapacitacionDetalleDto detalle = CapacitacionDetalleDto.builder().id(1L).build();
        when(service.getDetalle(1L)).thenReturn(detalle);
        String esperado = objectMapper.writeValueAsString(detalle);

        mockMvc.perform(get("/api/capacitaciones/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(esperado));

        verify(service, times(1)).getDetalle(1L);
    }
}

