package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.mapeos;

import org.springframework.stereotype.Component;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Conversor para MapStruct: YearMonth <-> String (yyyy-MM)
 */
@Component
public class YearMonthMapper {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    public String map(YearMonth value) {
        return (value != null) ? value.format(FMT) : null;
    }

    public YearMonth map(String value) {
        return (value != null && !value.isBlank()) ? YearMonth.parse(value, FMT) : null;
    }
}
