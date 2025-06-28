package ar.org.hospitalcuencaalta.servicio_nomina.servicio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.ConceptoLiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.ConceptoLiquidacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_nomina.web.mapeo.ConceptoLiquidacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConceptoLiquidacionService {
    @Autowired
    private ConceptoLiquidacionRepository repo;
    @Autowired
    private ConceptoLiquidacionMapper mapper;
    @Autowired
    private ConceptoLiquidacionDetalleMapper detalleMapper;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    public ConceptoLiquidacionDto create(ConceptoLiquidacionDto dto) {
        ConceptoLiquidacion e = mapper.toEntity(dto);
        ConceptoLiquidacion saved =
                repo.findByCodigoAndTipoCalculo(e.getCodigo(), e.getTipoCalculo())
                        .orElseGet(() -> repo.save(e));
        ConceptoLiquidacionDto out = mapper.toDto(saved);
        kafka.send("servicioNomina.added", out);
        return out;
    }

    public List<ConceptoLiquidacionDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public ConceptoLiquidacionDetalleDto getDetalle(Long id) {
        return detalleMapper.toDetalleDto(repo.findById(id).orElseThrow());
    }
}

