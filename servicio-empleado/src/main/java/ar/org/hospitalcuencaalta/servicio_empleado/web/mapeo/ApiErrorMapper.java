// ApiErrorMapper.java
package ar.org.hospitalcuencaalta.servicio_empleado.web.mapeo;

import ar.org.hospitalcuencaalta.servicio_empleado.excepcion.ApiError;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.ApiErrorDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiErrorMapper {
    ApiErrorDto toDto(ApiError error);
}
