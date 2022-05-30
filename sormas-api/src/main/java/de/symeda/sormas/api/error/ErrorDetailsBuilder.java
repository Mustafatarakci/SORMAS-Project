package de.symeda.sormas.api.error;

public class ErrorDetailsBuilder {
    private String message;
    private String entity;

    public ErrorDetailsBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorDetailsBuilder setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public ErrorDetails build() {
        return new ErrorDetails(message, entity);
    }

}