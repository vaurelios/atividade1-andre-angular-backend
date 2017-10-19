package org.motopecas.utils;

import org.motopecas.models.JsonModel;

public class JsonValidatorException extends Exception {
  public JsonValidatorException(String msg) {
    super(msg);
  }

  public String toJson() {
    return JsonModel.createError(this.getMessage()).toString();
  }
}
