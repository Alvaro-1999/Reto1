package com.gymapp.model;

import java.io.Serializable;
import java.util.Objects;

public class Workout implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String workoutName;
    private String video;
    private int tiempo;
    private int numEj;
    private int level;

    public Workout() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public int getTiempo() { return tiempo; }
    public void setTiempo(int tiempo) { this.tiempo = tiempo; }

    public int getNumEj() { return numEj; }
    public void setNumEj(int numEj) { this.numEj = numEj; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    @Override
    public int hashCode() {
        return Objects.hash(id, workoutName, video, tiempo, numEj, level);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Workout other = (Workout) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(workoutName, other.workoutName)
            && Objects.equals(video, other.video)
            && tiempo == other.tiempo
            && numEj == other.numEj
            && level == other.level;
    }

    @Override
    public String toString() {
        return "Workout [id=" + id + ", workoutName=" + workoutName + ", video=" + video
            + ", tiempo=" + tiempo + ", numEj=" + numEj + ", level=" + level + "]";
    }
}
