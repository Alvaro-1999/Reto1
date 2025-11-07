package com.gymapp.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String login;
    private String name;
    private String lastName;
    private String mail;
    private String password;
    private String birthDate;
    private int level;
    private String userType;
   
    public User() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

  
    @Override
    public int hashCode() {
        return Objects.hash(id, login, name, lastName, mail, password, birthDate, level, userType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return level == other.level
            && Objects.equals(id, other.id)
            && Objects.equals(login, other.login)
            && Objects.equals(name, other.name)
            && Objects.equals(lastName, other.lastName)
            && Objects.equals(mail, other.mail)
            && Objects.equals(password, other.password)
            && Objects.equals(birthDate, other.birthDate)
            && Objects.equals(userType, other.userType)
          ;
    }

    @Override
    public String toString() {
        return "User [id=" + id 
                + ", login=" + login 
                + ", name=" + name 
                + ", lastName=" + lastName
                + ", mail=" + mail 
                + ", password=" + password 
                + ", birthDate=" + birthDate
                + ", level=" + level 
                + ", userType=" + userType 
                + "]";
    }

}
