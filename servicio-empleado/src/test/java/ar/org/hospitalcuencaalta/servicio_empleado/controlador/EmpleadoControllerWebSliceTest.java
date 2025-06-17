package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.EmpleadoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo.ApiErrorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web-slice tests for EmpleadoController covering PUT and DELETE endpoints.
 */
@WebMvcTest(EmpleadoController.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
class EmpleadoControllerWebSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmpleadoService empleadoService;

    @MockitoBean
    private ApiErrorMapper apiErrorMapper;

    @Test
    void updateEmpleado_shouldReturnUpdatedDto() throws Exception {
        EmpleadoDto dto = EmpleadoDto.builder()
                .id(1L)
                .nombre("Ana")
                .apellido("Martinez")
                .documento("1234")
                .fechaIngreso(LocalDate.parse("2025-01-01"))
                .build();

        when(empleadoService.update(eq(1L), any(EmpleadoDto.class))).thenReturn(dto);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/empleados/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(empleadoService, times(1)).update(eq(1L), any(EmpleadoDto.class));
    }

    @Test
    void deleteEmpleado_shouldReturnOk() throws Exception {
        doNothing().when(empleadoService).delete(1L);

        mockMvc.perform(delete("/api/empleados/{id}", 1L))
                .andExpect(status().isOk());

        verify(empleadoService, times(1)).delete(1L);
    }
}
