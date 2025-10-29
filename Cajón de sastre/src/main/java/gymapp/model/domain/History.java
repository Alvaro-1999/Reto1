package gymapp.model.domain;

import java.io.Serializable;
import java.util.Objects;

public class History implements Serializable {

    private static final long serialVersionUID = -362419998920090751L;

    private String name;               // nombre del workout
    private String level;              // nivel (String en Firestore)
    private String estimatedTime;      // tiempo previsto
    private String time;               // tiempo total empleado
    private String completionProgress; // % completado
    private String date;               // fecha (ej. "11-11-2025")

    // --- Getters y Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getCompletionProgress() { return completionProgress; }
    public void setCompletionProgress(String completionProgress) { this.completionProgress = completionProgress; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // --- toString ---
    @Override
    public String toString() {
        return "History [name=" + name + ", level=" + level + ", estimatedTime=" + estimatedTime
                + ", time=" + time + ", completionProgress=" + completionProgress + ", date=" + date + "]";
    }

    // --- equals & hashCode ---
    @Override
    public int hashCode() {
        return Objects.hash(name, level, estimatedTime, time, completionProgress, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        History other = (History) obj;
        return Objects.equals(name, other.name)
            && Objects.equals(level, other.level)
            && Objects.equals(estimatedTime, other.estimatedTime)
            && Objects.equals(time, other.time)
            && Objects.equals(completionProgress, other.completionProgress)
            && Objects.equals(date, other.date);
    }
}
