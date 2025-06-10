package ar.org.hospitalcuencaalta.servicio_consultas.evento;

import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.LicenciaProjectionRepository;
import ar.org.hospitalcuencaalta.servicio_consultas.web.dto.LicenciaDto;
import ar.org.hospitalcuencaalta.servicio_consultas.web.mapeos.LicenciaProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class LicenciaEventListener {
    @Autowired
    private LicenciaProjectionRepository repo;
    @Autowired
    private LicenciaProjectionMapper mapper;

    @KafkaListener(topics = "servicioContrato.licencia.created")
    public void onCreated(LicenciaDto dto) {
        repo.save(mapper.toLicencia(dto));
    }
}
