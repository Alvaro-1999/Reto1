package gymapp.service;

import java.io.IOException;
import java.util.List;

import gymapp.model.domain.Exercise;
import gymapp.model.resource.ExerciseResource;

public class ExerciseService implements ServiceInterface<Exercise> {

    private final ExerciseResource exerciseResource;

    public ExerciseService() throws IOException {
        this.exerciseResource = new ExerciseResource();
    }

    @Override
    public void save(Exercise exercise) throws Exception {
        exerciseResource.save(exercise);
    }

    @Override
    public Exercise find(Exercise exercise) throws Exception {
        return exerciseResource.find(exercise);
    }

    @Override
    public List<Exercise> findAll() throws Exception {
        return exerciseResource.findAll();
    }

    @Override
    public void update(Exercise exercise) throws Exception {
        exerciseResource.update(exercise);
    }

    @Override
    public void delete(Exercise exercise) throws Exception {
        exerciseResource.delete(exercise);
    }
}
