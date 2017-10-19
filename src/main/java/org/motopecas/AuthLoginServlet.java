package org.motopecas;

import com.google.common.hash.Hashing;
import org.motopecas.db.Connection;
import org.motopecas.db.User;
import org.motopecas.db.UserRepository;
import org.motopecas.models.JsonModel;
import org.motopecas.utils.JsonValidator;
import org.motopecas.utils.JsonValidatorException;
import org.motopecas.utils.Utils;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AuthLoginServlet", urlPatterns = "/auth/login", loadOnStartup = 1)
public class AuthLoginServlet extends HttpServlet {
  protected UserRepository users;

  public AuthLoginServlet() {
    MongoRepositoryFactory factory = new MongoRepositoryFactory(Connection.getInstance().getTemplate());
    users = factory.getRepository(UserRepository.class);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");

    users.findAll().forEach((User u) -> {
      try {
        response.getWriter().println(u.toString());
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    });
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      JsonObject loginReq = Utils.readJson(request.getReader());

      try {
        boolean valid = new JsonValidator().require("username").require("password").validate(loginReq);

        User user = users.findByUsername(loginReq.getString("username"));
        if (user != null) {
          String hash = Hashing.sha256().hashString(loginReq.getString("username"), StandardCharsets.UTF_8).toString();

          if (user.passHash == hash) {
            Utils.responseSendJson(response, JsonModel.);
          }
        }
        else
          Utils.responseSendJson(response, JsonModel.createError("No user found with this username").toString());
      } catch (JsonValidatorException e) {
        Utils.responseSendJson(response, e.toJson());
      }
    } catch (JsonParsingException e) {
      Utils.responseSendJson(response, JsonModel.createFullError("Invalid JSON", e.getMessage()).toString());
    }
  }
}
