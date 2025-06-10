package ar.org.hospitalcuencaalta.servicio_contrato.repositorio;


import ar.org.hospitalcuencaalta.servicio_contrato.modelo.ContratoLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoLaboralRepository extends JpaRepository<ContratoLaboral, Long> {
    void deleteByEmpleadoId(Long empleadoId);
}
