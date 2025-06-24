package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.EmpleadoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService svc;

    @PostMapping
    public EmpleadoDto create(@RequestBody EmpleadoDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    public List<EmpleadoDto> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public EmpleadoDto getById(@PathVariable Long id) {
        return svc.findById(id);
    }

    @GetMapping("/documento/{documento}")
    public EmpleadoDto getByDocumento(@PathVariable String documento) {
        return svc.findByDocumento(documento);
    }

    @PutMapping("/{id}")
    public EmpleadoDto update(@PathVariable Long id, @RequestBody EmpleadoDto dto) {
        return svc.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}

