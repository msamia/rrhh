package ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EvaluacionDesempeno;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio.EvaluacionDesempenoRepository;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EvaluacionDetalleMapper;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos.EvaluacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluacionService {
    @Autowired
    private EvaluacionDesempenoRepository repo;
    @Autowired
    private EvaluacionMapper mapper;
    @Autowired
    private EvaluacionDetalleMapper detalleMapper;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    public EvaluacionDto create(EvaluacionDto dto) {
        EvaluacionDesempeno e = mapper.toEntity(dto);
        EvaluacionDesempeno saved = repo.save(e);
        // publicar evento de evaluación en el tópico consumido por servicio-consultas
        kafka.send("servicioEntrenamiento.evaluated", saved.getId());
        return mapper.toDto(saved);
    }

    public List<EvaluacionDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public EvaluacionDetalleDto getDetalle(Long id) {
        return detalleMapper.toDto(repo.findById(id).orElseThrow());
    }
}

