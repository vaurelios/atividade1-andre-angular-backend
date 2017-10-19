package org.motopecas.utils;

public class JsonValidatorMissingFieldException extends JsonValidatorException {
  public JsonValidatorMissingFieldException(String field) {
    super(String.format("The '%s' field is required but is missing from Json object", field));
  }
}
