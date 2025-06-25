package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ContratoProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.ContratoLaboralDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.ContratoProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ContratoEventListener {
    @Autowired
    private ContratoProjectionRepository repo;
    @Autowired
    private ContratoProjectionMapper mapper;

    @KafkaListener(topics = "servicioContrato.contrato.created")
    public void onCreated(ContratoLaboralDto dto) {
        repo.save(mapper.toContrato(dto));
    }

    @KafkaListener(topics = "servicioContrato.contrato.updated")
    public void onUpdated(ContratoLaboralDto dto) {
        repo.save(mapper.toContrato(dto));
    }

    @KafkaListener(topics = "servicioContrato.contrato.deleted")
    public void onDeleted(Object payload) {
        Long id = null;
        if (payload instanceof Long l) {
            id = l;
        } else if (payload instanceof ContratoLaboralDto dto) {
            id = dto.getId();
        }
        if (id != null) {
            repo.deleteById(id);
        }
    }
}