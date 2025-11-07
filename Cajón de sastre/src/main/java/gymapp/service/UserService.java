package gymapp.service;

import java.io.IOException;
import java.util.List;

import gymapp.model.domain.User;
import gymapp.model.resource.UserResource;

public class UserService implements ServiceInterface<User> {

    private final UserResource userResource;

    public UserService() throws IOException {
        this.userResource = new UserResource();
    }

    @Override
    public void save(User user) throws Exception {
        if (!isUserPresent(user)) {
            userResource.save(user);
        }
    }

    @Override
    public User find(User user) throws Exception {
        return userResource.find(user);
    }

    @Override
    public List<User> findAll() throws Exception {
        return userResource.findAll();
    }

    @Override
    public void update(User user) throws Exception {
        userResource.update(user);
    }

    @Override
    public void delete(User user) throws Exception {
        userResource.delete(user);
    }

    public boolean isUserPresent(User user) throws Exception {
        User found = find(user);
        return found != null && found.getLogin() != null;
    }

    public boolean checkCredentials(User user) throws Exception {
        if (isUserPresent(user)) {
            User existingUser = find(user);
            return existingUser.getLogin().equalsIgnoreCase(user.getLogin())
                && existingUser.getPassword().equals(user.getPassword());
        }
        return false;
    }

    /**
     * Crea un nuevo usuario con nivel inicial "1" y birthDate como String.
     */
    public User createUser(String name, String lastName, String login, String mail, String password, String birthDate) {
        User ret = new User();
        ret.setName(name);
        ret.setLastName(lastName);
        ret.setLevel("1"); // nivel inicial como String
        ret.setLogin(login);
        ret.setMail(mail);
        ret.setPassword(password);
        ret.setBirthDate(birthDate); // se guarda directamente como String
        return ret;
    }
}
