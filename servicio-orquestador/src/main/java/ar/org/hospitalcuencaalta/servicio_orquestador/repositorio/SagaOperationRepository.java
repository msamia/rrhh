package ar.org.hospitalcuencaalta.servicio_orquestador.repositorio;

import ar.org.hospitalcuencaalta.servicio_orquestador.operacion.SagaOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SagaOperationRepository extends JpaRepository<SagaOperation, Long> {
    List<SagaOperation> findBySagaIdOrderByTimestampAsc(Long sagaId);
}
