package gymapp.model.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import gymapp.model.Firebase;
import gymapp.model.domain.History;
import gymapp.utils.Constants;
import gymapp.utils.UserSession;

public class HistoryResource implements ResourceInterface<History> {

    private final Firestore db;

    public HistoryResource() throws IOException {
        this.db = Firebase.getInstance().getDb();
    }

    @Override
    public void save(History history) throws Exception {
        String login = UserSession.getInstance().getUser().getLogin();
        DocumentReference userRef = db.collection(Constants.USER_COLLECTION)
                                      .document(login);

        // Guardamos un nuevo histórico con ID automático
        userRef.collection(Constants.HISTORY_COLLECTION)
               .add(history)
               .get();
    }

    @Override
    public History find(History t) throws Exception {
        // Si tu clase History no tiene id y no guardas el ID del documento,
        // este método no aplica. Podríamos implementarlo si decides almacenar el ID.
        return null;
    }

    @Override
    public List<History> findAll() throws Exception {
        List<History> ret = new ArrayList<>();
        String login = UserSession.getInstance().getUser().getLogin();

        DocumentReference userRef = db.collection(Constants.USER_COLLECTION)
                                      .document(login);

        // Ordenamos por fecha descendente (más reciente primero)
        ApiFuture<QuerySnapshot> historyQuery = userRef.collection(Constants.HISTORY_COLLECTION)
                                                       .orderBy("date", Query.Direction.DESCENDING)
                                                       .get();

        List<QueryDocumentSnapshot> docs = historyQuery.get().getDocuments();
        for (QueryDocumentSnapshot doc : docs) {
            History h = new History();
            h.setName(doc.getString("name"));
            h.setLevel(doc.getString("level"));
            h.setTime(doc.getString("time"));
            h.setEstimatedTime(doc.getString("estimatedTime"));
            h.setCompletionProgress(doc.getString("completionProgress"));
            h.setDate(doc.getString("date"));
            ret.add(h);
        }
        return ret;
    }

    @Override
    public void update(History history) throws Exception {
        // Para actualizar necesitas el ID del documento del histórico.
        // Si no lo guardas en History, puedes:
        // - Buscar por (name + date) si son únicos, obtener el doc y hacer set/update.
        // - O modificar tu modelo para incluir un campo id (opcional).
    }

    @Override
    public void delete(History history) throws Exception {
        // Igual que update: necesitas el ID del documento para borrar.
        // Si decides incluir id en History, aquí harías:
        // db.collection(Constants.USER_COLLECTION).document(login)
        //   .collection(Constants.HISTORY_COLLECTION).document(history.getId()).delete().get();
    }
}
