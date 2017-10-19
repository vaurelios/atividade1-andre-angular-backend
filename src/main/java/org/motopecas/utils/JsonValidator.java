package org.motopecas.utils;

import com.google.common.base.Strings;

import javax.json.JsonObject;
import java.util.ArrayList;

public class JsonValidator {
  ArrayList<String> required_fields;

  public JsonValidator() {
    required_fields = new ArrayList<>();
  }

  public JsonValidator require(String field) {
    required_fields.add(field);

    return this;
  }

  public boolean validate(JsonObject json) throws JsonValidatorMissingFieldException {
    for (String field : required_fields) {
      if (Strings.isNullOrEmpty(json.getString(field,""))) {
        throw new JsonValidatorMissingFieldException(field);
      }
    }

    return true;
  }
}
