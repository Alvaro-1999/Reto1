package gymapp.model.domain;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private static final long serialVersionUID = -1507426916252974128L;

    private String login;     // ID del documento en Firestore
    private String name;      // nombre
    private String lastName;  // apellidos
    private String mail;      // correo
    private String password;  // contraseña
    private String birthDate; // fecha de nacimiento (guardada como String en Firestore)
    private String level;     // nivel del usuario (String en Firestore)

    public User() {
        this.level = "1"; // nivel inicial por defecto
    }

    // --- Getters y Setters ---
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

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    // --- equals & hashCode ---
    @Override
    public int hashCode() {
        return Objects.hash(login, name, lastName, mail, password, birthDate, level);
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
            && Objects.equals(level, other.level);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "User [login=" + login + ", name=" + name + ", lastName=" + lastName
            + ", mail=" + mail + ", password=" + password
            + ", birthDate=" + birthDate + ", level=" + level + "]";
    }

}
