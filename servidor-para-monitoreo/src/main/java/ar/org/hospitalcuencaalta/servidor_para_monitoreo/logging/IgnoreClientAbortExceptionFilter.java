package ar.org.hospitalcuencaalta.servidor_para_monitoreo.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Filtro para ignorar excepciones causadas por cierres prematuros de la conexi\u00f3n
 * ("Broken pipe" o "Connection reset by peer"). Estas excepciones suelen
 * producirse cuando el cliente cancela la suscripci\u00f3n SSE y no representan un
 * fallo real del servidor.
 */
public class IgnoreClientAbortExceptionFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event == null) {
            return FilterReply.NEUTRAL;
        }
        if (event.getThrowableProxy() != null) {
            String msg = event.getThrowableProxy().getMessage();
            if (msg != null && (msg.contains("Se ha anulado una conexi\u00f3n")
                    || msg.contains("Broken pipe")
                    || msg.contains("Connection reset by peer"))) {
                return FilterReply.DENY;
            }
        }
        return FilterReply.NEUTRAL;
    }
}
