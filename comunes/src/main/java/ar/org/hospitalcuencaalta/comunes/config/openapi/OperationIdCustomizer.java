package ar.org.hospitalcuencaalta.comunes.config.openapi;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * Agrega un operationId único que utiliza el nombre del controlador para evitar valores
 * como update_1 en la documentación de OpenAPI.
 */
@Component
public class OperationIdCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();
        String controllerName = handlerMethod.getBeanType().getSimpleName()
                .replace("Controller", "");
        operation.setOperationId(methodName + controllerName);
        return operation;
    }
}
