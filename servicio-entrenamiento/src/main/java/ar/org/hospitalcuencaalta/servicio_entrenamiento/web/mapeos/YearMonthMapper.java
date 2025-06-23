package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import org.mapstruct.Mapper;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Conversor para MapStruct: YearMonth <-> String (yyyy-MM)
 */
@Mapper(componentModel = "spring")
public interface YearMonthMapper {
    DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    default String map(YearMonth value) {
        return (value != null) ? value.format(FMT) : null;
    }

    default YearMonth map(String value) {
        return (value != null && !value.isBlank()) ? YearMonth.parse(value, FMT) : null;
    }
}
