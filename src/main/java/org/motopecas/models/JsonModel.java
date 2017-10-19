package org.motopecas.models;

import javax.json.Json;
import javax.json.JsonObject;

public class JsonModel {
  public static JsonObject createError(String msg) {
    return Json.createObjectBuilder()
        .add("error", Json.createObjectBuilder().add("message", msg).build())
        .build();
  }

  public static JsonObject createFullError(String msg, String detail) {
    return Json.createObjectBuilder()
        .add("error", Json.createObjectBuilder()
            .add("message", msg)
            .add("detail", detail).build())
        .build();
  }

  public static JsonObject createMessage(String msg) {
    return Json.createObjectBuilder()
        .add("message", msg).build();
  }
}
