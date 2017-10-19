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
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "AuthNewUserServlet", urlPatterns = "/auth/newUser", loadOnStartup = 1)
public class AuthNewUserServlet extends HttpServlet {
  protected UserRepository users;

  public AuthNewUserServlet() {
    MongoRepositoryFactory factory = new MongoRepositoryFactory(Connection.getInstance().getTemplate());
    users = factory.getRepository(UserRepository.class);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.getWriter().write("Not allowed!");
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try {
      JsonObject newUserReq = Utils.readJson(req.getReader());

      try {
        boolean valid = new JsonValidator()
            .require("username").require("email")
            .require("firstName").require("lastName")
            .require("comprovantePessoaTipo").require("comprovantePessoa")
            .require("rg").require("bday")
            .require("password").validate(newUserReq);

        User newUser = new User(
            newUserReq.getString("username"),
            newUserReq.getString("email"),
            newUserReq.getString("firstName"),
            newUserReq.getString("lastName"));

        newUser.comprovantePessoaTipo = newUserReq.getString("comprovantePessoaTipo");
        newUser.comprovantePessoa = newUserReq.getString("comprovantePessoa");
        newUser.rg = newUserReq.getString("rg");
        newUser.passHash = Hashing.sha256().hashString(
            newUserReq.getString("password"),
            StandardCharsets.UTF_8).toString();

        try {
          newUser.bday = new SimpleDateFormat("yyyy-MM-dd").parse(newUserReq.getString("bday"));
        } catch (ParseException e) {
          System.err.println("AuthNewUserServlet: cannot parse bday: " + e.getMessage());
        }

        users.save(newUser);

        Utils.responseSendJson(res, JsonModel.createMessage("Created!").toString());
      } catch (JsonValidatorException e) {
        Utils.responseSendJson(res, e.toJson(), 400);
      }
    } catch (JsonParsingException e) {
      Utils.responseSendJson(res, JsonModel.createFullError("Invalid JSON", e.getMessage()).toString(), 400);
    }
  }
}
