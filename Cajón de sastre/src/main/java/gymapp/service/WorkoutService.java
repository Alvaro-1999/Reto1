package gymapp.service;

import java.io.IOException;
import java.util.List;

import gymapp.model.domain.Workout;
import gymapp.model.resource.WorkoutResource;
import gymapp.utils.UserSession;

public class WorkoutService implements ServiceInterface<Workout> {

    private final WorkoutResource workoutResource;

    public WorkoutService() throws IOException {
        this.workoutResource = new WorkoutResource();
    }

    @Override
    public void save(Workout workout) throws Exception {
        workoutResource.save(workout);
    }

    @Override
    public Workout find(Workout workout) throws Exception {
        return workoutResource.find(workout);
    }

    @Override
    public List<Workout> findAll() throws Exception {
        return workoutResource.findAll();
    }

    @Override
    public void update(Workout workout) throws Exception {
        workoutResource.update(workout);
    }

    @Override
    public void delete(Workout workout) throws Exception {
        workoutResource.delete(workout);
    }

    /**
     * Filtra los workouts según el nivel del usuario logueado.
     * Convierte los niveles de String a int para poder compararlos.
     */
    public List<Workout> getFilteredWorkouts(List<Workout> workouts) {
        String userLevelStr = UserSession.getInstance().getUser().getLevel();
        int parsedLevel = 1;
        try {
            parsedLevel = Integer.parseInt(userLevelStr);
        } catch (NumberFormatException e) {
            // si no se puede parsear, se queda en nivel 1
        }

        final int userLevel = parsedLevel; // variable final para usar en la lambda

        workouts.removeIf(workout -> {
            try {
                int workoutLevel = Integer.parseInt(workout.getLevel());
                return workoutLevel > userLevel;
            } catch (NumberFormatException e) {
                return true; // si el nivel no es válido, se descarta
            }
        });

        return workouts;
    }
}
