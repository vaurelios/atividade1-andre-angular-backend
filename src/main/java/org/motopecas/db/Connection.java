package org.motopecas.db;

import com.mongodb.MongoClient;
import org.motopecas.utils.Utils;
import org.springframework.data.mongodb.core.MongoTemplate;

public class Connection {
  private static Connection instance = null;
  private MongoClient client;

  public static Connection getInstance() {
    if (instance == null)
    instance = new Connection();

    return instance;
  }

  private Connection() {
    String host = Utils.valueOr(System.getenv("MONGODB_HOST"), "localhost");
    String port = Utils.valueOr(System.getenv("MONGODB_PORT"), "27017");

    client = new MongoClient(host, Integer.parseInt(port));
  }

  public MongoTemplate getTemplate() {
    String db = Utils.valueOr(System.getenv("MONGODB_DB"), "motopecas");

    return new MongoTemplate(client, db);
  }
}
