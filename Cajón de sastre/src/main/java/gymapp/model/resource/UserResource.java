package gymapp.model.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import gymapp.model.Firebase;
import gymapp.model.domain.User;
import gymapp.utils.Constants;
import gymapp.utils.DateUtils;
import gymapp.utils.UserSession;

public class UserResource implements ResourceInterface<User> {

	private final Firestore db;
	private final ObjectMapper mapper;

	public UserResource() throws IOException {
		this.db = Firebase.getInstance().getDb();
		this.mapper = new ObjectMapper();
	}

	@Override
	public void save(User user) throws Exception {
		Map<String, Object> map = mapper.convertValue(user, HashMap.class);
		map.remove("id");

		// Guardamos el login como ID del documento
		db.collection(Constants.USER_COLLECTION).document(user.getLogin()).set(map).get();
	}

	@Override
	public List<User> findAll() throws Exception {
		List<User> ret = new ArrayList<>();

		List<QueryDocumentSnapshot> userDocuments = db.collection(Constants.USER_COLLECTION).get().get().getDocuments();

		for (QueryDocumentSnapshot userDocument : userDocuments) {
			User u = mapper.convertValue(userDocument.getData(), User.class);
			ret.add(u);
		}

		return ret;
	}

	@Override
	public User find(User user) throws Exception {
		DocumentSnapshot doc = db.collection(Constants.USER_COLLECTION).document(user.getLogin()).get().get();

		if (!doc.exists())
			return null;

		// Crear un User “parcial” solo con login y password
		Map<String, Object> data = doc.getData();
		User foundUser = new User();
		foundUser.setLogin((String) data.get("login"));
		foundUser.setPassword((String) data.get("password"));

		return foundUser;
	}

	@Override
	public void update(User user) throws Exception {
		String login = UserSession.getInstance().getUser().getLogin();

		Map<String, Object> updates = new HashMap<>();
		updates.put("password", user.getPassword());
		updates.put("name", user.getName());
		updates.put("mail", user.getMail());
		updates.put("lastName", user.getLastName());
		updates.put("birthDate", user.getBirthDate());

		db.collection(Constants.USER_COLLECTION).document(login).update(updates).get();
	}

	@Override
	public void delete(User user) throws Exception {
		db.collection(Constants.USER_COLLECTION).document(user.getLogin()).delete().get();
	}
}
