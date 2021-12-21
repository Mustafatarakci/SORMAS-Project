package de.symeda.sormas.rest.swagger;

import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.models.Operation;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Provides Swagger documentation support for SORMAS' custom annotations.
 */
public class SormasSwaggerExtensions extends AbstractOpenAPIExtension {

    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
        super.decorateOperation(operation, method, chain);

        if (operation.getTags() == null || operation.getTags().size() == 0) {
            // Add name of the declaring controller as tag for operation grouping
            operation.addTagsItem(this.getControllerLabel(method.getDeclaringClass()));
        }
    }


    /**
     * Generate a user-friendly name label for the given controller class.
     */
    public String getControllerLabel(Class<?> clazz) {
        return clazz.getSimpleName().replaceAll("Resource$", "").replaceAll("(?<!^)[A-Z]", " $0") + " Controller";
    }
}
