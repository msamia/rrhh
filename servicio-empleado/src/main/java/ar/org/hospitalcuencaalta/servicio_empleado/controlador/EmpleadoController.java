package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.EmpleadoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleados", description = "Operaciones CRUD de empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService svc;

    @PostMapping
    @Operation(summary = "Crear empleado", description = "Registra un nuevo empleado")
    public EmpleadoDto create(@RequestBody EmpleadoDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    @Operation(summary = "Listar empleados", description = "Devuelve todos los empleados registrados")
    public List<EmpleadoDto> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener por id", description = "Recupera un empleado por su identificador")
    public EmpleadoDto getById(@PathVariable Long id) {
        return svc.findById(id);
    }

    @GetMapping("/documento/{documento}")
    @Operation(summary = "Buscar por documento", description = "Busca un empleado utilizando su documento")
    public EmpleadoDto getByDocumento(@PathVariable String documento) {
        return svc.findByDocumento(documento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado", description = "Modifica los datos de un empleado existente")
    public EmpleadoDto update(@PathVariable Long id, @RequestBody EmpleadoDto dto) {
        return svc.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empleado", description = "Elimina el empleado indicado")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}

