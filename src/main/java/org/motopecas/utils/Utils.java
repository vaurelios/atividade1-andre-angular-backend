package org.motopecas.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class Utils {
	public static <T> T valueOr(T a, T b) {
		if (a == null)
			return b;

		return a;
	}

	public static JsonObject readJson(BufferedReader br) throws JsonParsingException {
		JsonReader reader = Json.createReader(br);

		return reader.readObject();
  }

  public static void responseSendJson(HttpServletResponse response, String json, int status) throws IOException {
    response.setStatus(status);
	  response.setContentType("application/json");
    response.getWriter().write(json);
    response.getWriter().flush();
  }

  public static void responseSendJson(HttpServletResponse response, String json) throws IOException {
	  responseSendJson(response, json, 200);
  }
}
