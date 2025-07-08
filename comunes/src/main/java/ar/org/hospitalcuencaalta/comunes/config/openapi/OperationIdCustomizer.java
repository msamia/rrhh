package ar.org.hospitalcuencaalta.comunes.config.openapi;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * Adds a unique operationId using the controller name to avoid values like
 * update_1 in the OpenAPI documentation.
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
