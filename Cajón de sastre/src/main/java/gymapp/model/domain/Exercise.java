package gymapp.model.domain;

import java.io.Serializable;
import java.util.Objects;

public class Exercise implements Serializable {

    private static final long serialVersionUID = 1006238012581469025L;

    private String id;             // id del ejercicio
    private String name;           // nombre del ejercicio
    private String repetitions;    // repeticiones (String en Firestore)
    private String series;         // número de series (String en Firestore)
    private String rest;           // descanso en segundos (String en Firestore)
    private String timePerSeries;  // duración de cada serie en segundos (String en Firestore)
    private String imageUrl;       // URL de la imagen

    // --- Getters y Setters ---
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRepetitions() {
        return repetitions;
    }
    public void setRepetitions(String repetitions) {
        this.repetitions = repetitions;
    }

    public String getSeries() {
        return series;
    }
    public void setSeries(String series) {
        this.series = series;
    }

    public String getRest() {
        return rest;
    }
    public void setRest(String rest) {
        this.rest = rest;
    }

    public String getTimePerSeries() {
        return timePerSeries;
    }
    public void setTimePerSeries(String timePerSeries) {
        this.timePerSeries = timePerSeries;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, repetitions, series, rest, timePerSeries, imageUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Exercise other = (Exercise) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(name, other.name)
            && Objects.equals(repetitions, other.repetitions)
            && Objects.equals(series, other.series)
            && Objects.equals(rest, other.rest)
            && Objects.equals(timePerSeries, other.timePerSeries)
            && Objects.equals(imageUrl, other.imageUrl);
    }

    @Override
    public String toString() {
        return "Exercise [id=" + id + ", name=" + name + ", repetitions=" + repetitions
            + ", series=" + series + ", rest=" + rest
            + ", timePerSeries=" + timePerSeries + ", imageUrl=" + imageUrl + "]";
    }
}
