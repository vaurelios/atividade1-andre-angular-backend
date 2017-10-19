package org.motopecas.db;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class User {
	@Id
	public String id;

	public String username;
	public String email;
	public String firstName;
	public String lastName;
	public String comprovantePessoaTipo;
	public String comprovantePessoa;
	public String rg;
	public Date bday;
	public String passHash;

	public User(String username, String email, String firstName, String lastName) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return String.format("User[id='%s', username='%s', email='%s', firstName='%s', lastName='%s', passHash='%s']",
				id, username, email, firstName, lastName, passHash);
	}
}
