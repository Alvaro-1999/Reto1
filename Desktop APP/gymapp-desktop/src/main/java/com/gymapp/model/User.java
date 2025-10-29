package com.gymapp.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private String name;
    private String lastName;
    private String mail;
    private String password;
    private String birthDate;
    private int level;
    private String userType;

    public User() {}

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String isUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    @Override
    public int hashCode() {
        return Objects.hash(login, name, lastName, mail, password, birthDate, level, userType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(login, other.login)
            && Objects.equals(name, other.name)
            && Objects.equals(lastName, other.lastName)
            && Objects.equals(mail, other.mail)
            && Objects.equals(password, other.password)
            && Objects.equals(birthDate, other.birthDate)
            && level == other.level
            && userType == other.userType;
    }

    @Override
    public String toString() {
        return "User [login=" + login + ", name=" + name + ", lastName=" + lastName
            + ", mail=" + mail + ", password=" + password + ", birthDate=" + birthDate
            + ", level=" + level + ", userType=" + userType + "]";
    }
}
