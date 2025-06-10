package ar.org.hospitalcuencaalta.servicio_nomina.servicio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.LiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.LiquidacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.LiquidacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiquidacionService {
    @Autowired
    private LiquidacionRepository repo;
    @Autowired
    private LiquidacionMapper mapper;
    @Autowired
    private LiquidacionDetalleMapper detalleMapper;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    public LiquidacionDto create(LiquidacionDto dto) {
        Liquidacion e = mapper.toEntity(dto);
        Liquidacion saved = repo.save(e);
        kafka.send("servicioNomina.nomina.generated", saved.getId());
        return mapper.toDto(saved);
    }

    public List<LiquidacionDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public LiquidacionDetalleDto getDetalle(Long id) {
        return detalleMapper.toDetalleDto(repo.findById(id).orElseThrow());
    }
}
