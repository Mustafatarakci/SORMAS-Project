package de.symeda.sormas.rest.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtensions;

/**
 * SORMAS Swagger Configuration.
 * This class is the preferred spot for specifying API-related Swagger metadata via Swagger annotations,
 * as well as for Swagger extension registration.
 */
public class SwaggerConfig {

    static {
        // Real initialization routine
        registerExtensions();

        // Swagger uses a Jackson ObjectMapper in the process of type resolution; there are some
        // settings for that ObjectMapper and Swagger-related classes we need to adjust for the
        // Swagger Specification to be correct
        tweakConfig(Json.mapper());
    }

    public static void init() {
        // Solemnly there for triggering the static initializer
    }

    /**
     * Register Swagger Generator Extensions
     */
    private static void registerExtensions() {
        // rewrite controller strings
        OpenAPIExtensions.getExtensions().add(new SormasSwaggerExtensions());

        // include annotations in openAPI specs
        ModelConverters.getInstance().addConverter(new AttributeConverter(Json.mapper()));
    }

    /**
     * Set configuration parameters for Swagger-related classes.
     *
     * @param swaggerObjectMapper ObjectMapper instance used by Swagger
     */
    private static void tweakConfig(ObjectMapper swaggerObjectMapper) {
        // Do not use toString() on enum values
        swaggerObjectMapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        // Specify enumerations as separate schemas, instead of incorporating them into the
        // schemas for every field/property of their type
        ModelResolver.enumsAsRef = true;
    }

}


