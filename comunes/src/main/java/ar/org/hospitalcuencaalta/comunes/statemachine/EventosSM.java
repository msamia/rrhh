package ar.org.hospitalcuencaalta.comunes.statemachine;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

/**
 * Utilidad para enviar eventos a una {@link StateMachine} utilizando
 * la variante reactiva recomendada en Spring StateMachine 4.x.
 */
public final class EventosSM {

    private EventosSM() {
        // Utilidad
    }

    /**
     * Construye un {@link Message} con el evento recibido y lo envía
     * de forma reactiva a la máquina de estados.
     *
     * @param machine máquina de estados destino
     * @param evento  evento a despachar
     * @param <S>     tipo de estado
     * @param <E>     tipo de evento (enum)
     */
    public static <S, E extends Enum<E>> void enviar(StateMachine<S, E> machine,
                                                     E evento) {
        Message<E> msg = MessageBuilder.withPayload(evento).build();
        enviar(machine, msg);
    }

    /**
     * Envía un {@link Message} preconstruido de forma reactiva.
     */
    public static <S, E extends Enum<E>> void enviar(StateMachine<S, E> machine,
                                                     Message<E> message) {
        machine.sendEvent(Mono.just(message)).subscribe();
    }
}
