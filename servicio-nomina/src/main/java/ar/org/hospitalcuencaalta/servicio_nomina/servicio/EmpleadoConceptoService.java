package ar.org.hospitalcuencaalta.servicio_nomina.servicio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import ar.org.hospitalcuencaalta.servicio_nomina.modelo.EmpleadoConcepto;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.ConceptoLiquidacionRepository;
import ar.org.hospitalcuencaalta.servicio_nomina.repositorio.EmpleadoConceptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoConceptoService {
    @Autowired
    private EmpleadoConceptoRepository repo;
    @Autowired
    private ConceptoLiquidacionRepository conceptoRepo;

    public void asignarConcepto(Long empleadoId, Long conceptoId) {
        ConceptoLiquidacion concepto = conceptoRepo.findById(conceptoId).orElseThrow();
        EmpleadoConcepto ec = EmpleadoConcepto.builder()
                .empleadoId(empleadoId)
                .concepto(concepto)
                .build();
        repo.save(ec);
    }
}
