package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ConceptoLiquidacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.ConceptoLiquidacionDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.ConceptoLiquidacionProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConceptoLiquidacionEventListener {
    @Autowired
    private ConceptoLiquidacionProjectionRepository repo;
    @Autowired
    private ConceptoLiquidacionProjectionMapper mapper;

    @KafkaListener(topics = "servicioNomina.added")
    public void onCreated(ConceptoLiquidacionDto dto) {
        repo.save(mapper.toConcepto(dto));
    }
}
