package ar.org.hospitalcuencaalta.servicio_nomina.repositorio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.EmpleadoRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRegistryRepository extends JpaRepository<EmpleadoRegistry,Long> {
    boolean existsByIdAndDocumento(Long id, String documento);
    boolean existsByDocumento(String documento);
}
