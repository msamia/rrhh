package ar.org.hospitalcuencaalta.servicio_nomina.aop;

import feign.FeignException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Aspecto sencillo que intercepta llamadas a clientes Feign y traduce
 * las FeignException en ResponseStatusException para que el controlador
 * pueda responder con códigos adecuados.
 */
@Aspect
@Component
public class FeignErrorAspect {

    @Around("execution(* ar.org.hospitalcuencaalta.servicio_nomina.feign..*(..))")
    public Object translateFeignExceptions(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (FeignException.NotFound nf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nf.getMessage(), nf);
        } catch (FeignException fe) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Servicio remoto no disponible", fe);
        }
    }
}
