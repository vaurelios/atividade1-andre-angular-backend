package org.motopecas.db;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  public User findByUsername(String username);
  public User findByEmail(String email);
}
