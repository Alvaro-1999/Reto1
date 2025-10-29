package gymapp.service;

import java.io.IOException;
import java.util.List;

import gymapp.model.domain.History;
import gymapp.model.resource.HistoryResource;

public class HistoryService implements ServiceInterface<History> {

    private final HistoryResource historyResource;

    public HistoryService() throws IOException {
        this.historyResource = new HistoryResource();
    }

    @Override
    public void save(History history) throws Exception {
        historyResource.save(history);
    }

    @Override
    public History find(History history) throws Exception {
        return historyResource.find(history);
    }

    @Override
    public List<History> findAll() throws Exception {
        return historyResource.findAll();
    }

    @Override
    public void update(History history) throws Exception {
        historyResource.update(history);
    }

    @Override
    public void delete(History history) throws Exception {
        historyResource.delete(history);
    }

    /**
     * Devuelve el historial filtrado.
     * Por ahora simplemente devuelve todos los registros,
     * pero aquí podrías aplicar filtros (por fecha, progreso, etc.).
     */
    public List<History> getFilteredHistory() throws Exception {
        return findAll();
    }
}
