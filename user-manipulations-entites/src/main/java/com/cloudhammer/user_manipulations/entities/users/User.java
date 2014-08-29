package com.cloudhammer.user_manipulations.entities.users;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.GenericGenerator;

/**
 * Entity for the User
 */
@Entity
@Table(name = "users")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "userType")
@XmlRootElement(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = -4710296641743737247L;

	@Column(updatable = false, nullable = false)
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@NotNull
	@Column(length = 50, nullable = false)
	private String name;
	@NotNull
	@Column(length = 100, nullable = false, unique = true)
	private String email;
	@NotNull
	@Column(length = 32, nullable = false)
	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}

		User user = (User) o;

		if (email != null ? !email.equals(user.email) : user.email != null) {
			return false;
		}
		if (id != null ? !id.equals(user.id) : user.id != null) {
			return false;
		}
		if (name != null ? !name.equals(user.name) : user.name != null) {
			return false;
		}
		if (password != null ? !password.equals(user.password) : user.password != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}
