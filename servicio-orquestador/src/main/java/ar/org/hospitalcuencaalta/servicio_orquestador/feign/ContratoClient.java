package ar.org.hospitalcuencaalta.servicio_orquestador.feign;

import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.ContratoLaboralDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "servicio-contrato", fallback = ContratoClientFallback.class)
public interface ContratoClient {
    @PostMapping("/api/contratos")
    ContratoLaboralDto create(@RequestBody ContratoLaboralDto dto);

    @PutMapping("/api/contratos/{id}")
    ContratoLaboralDto update(@PathVariable("id") Long id, @RequestBody ContratoLaboralDto dto);

    @DeleteMapping("/api/contratos/{id}")
    void delete(@PathVariable("id") Long id);
}
