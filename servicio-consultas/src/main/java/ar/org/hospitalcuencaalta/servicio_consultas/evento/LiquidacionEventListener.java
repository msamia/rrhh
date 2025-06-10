package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.LiquidacionProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.LiquidacionDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.LiquidacionProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LiquidacionEventListener {
    @Autowired
    private LiquidacionProjectionRepository repo;
    @Autowired
    private LiquidacionProjectionMapper mapper;

    @KafkaListener(topics = "servicioNomina.nomina.generated")
    public void onCreated(LiquidacionDto dto) {
        repo.save(mapper.toLiquidacion(dto));
    }
}
