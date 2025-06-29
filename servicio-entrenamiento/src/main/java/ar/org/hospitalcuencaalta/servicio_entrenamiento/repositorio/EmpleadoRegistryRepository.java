package ar.org.hospitalcuencaalta.servicio_entrenamiento.repositorio;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo.EmpleadoRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRegistryRepository extends JpaRepository<EmpleadoRegistry, Long> {
    boolean existsByIdAndDocumento(Long id, String documento);
}
