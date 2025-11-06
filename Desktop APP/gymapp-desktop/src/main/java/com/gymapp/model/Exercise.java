package com.gymapp.model;

import com.google.cloud.firestore.DocumentReference;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Exercise implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private int rest;
    private DocumentReference workoutId;

    private List<Set> sets;

    public Exercise() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRest() { return rest; }
    public void setRest(int rest) { this.rest = rest; }

    public DocumentReference getWorkoutId() { return workoutId; }
    public void setWorkoutId(DocumentReference workoutId) { this.workoutId = workoutId; }

    public List<Set> getSets() { return sets; }
    public void setSets(List<Set> sets) { this.sets = sets; }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, rest, workoutId, sets);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Exercise other = (Exercise) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(name, other.name)
            && Objects.equals(description, other.description)
            && rest == other.rest
            && Objects.equals(workoutId, other.workoutId)
            && Objects.equals(sets, other.sets);
    }

    @Override
    public String toString() {
        return "Exercise [id=" + id + ", name=" + name + ", description=" + description
            + ", rest=" + rest + ", workoutId=" + workoutId + ", sets=" + sets + "]";
    }
}
