package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Consulta Empleados", description = "Consultas de proyección de empleados")
public class EmpleadoQueryController {
    @Autowired
    private EmpleadoProjectionRepository repo;

    @GetMapping
    @Operation(summary = "Listar empleados", description = "Obtiene la vista de todos los empleados")
    public List<EmpleadoProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Empleado por id", description = "Devuelve la proyección de un empleado")
    public EmpleadoProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
