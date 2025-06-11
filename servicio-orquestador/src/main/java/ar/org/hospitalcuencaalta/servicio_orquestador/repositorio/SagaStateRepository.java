package ar.org.hospitalcuencaalta.servicio_orquestador.repositorio;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.SagaState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SagaStateRepository extends JpaRepository<SagaState, String> {
}
