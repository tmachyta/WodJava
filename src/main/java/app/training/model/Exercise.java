package app.training.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.net.URL;
import java.util.Objects;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE exercises SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "exercises")
@Data
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String about;

    @Column(name = "image_data", columnDefinition = "BLOB")
    private byte[] imageData;
    @Column(name = "video_relative_path")
    private String videoRelativePath;
    @Transient
    private URL video;
    @ManyToOne
    @JoinColumn(name = "training_section_id")
    @JsonIgnore
    private TrainingSection trainingSection;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Exercise exercise = (Exercise) o;
        return isDeleted == exercise.isDeleted
                && Objects.equals(id, exercise.id)
                && Objects.equals(name, exercise.name)
                && Objects.equals(about, exercise.about)
                && Objects.equals(imageData, exercise.imageData)
                && Objects.equals(videoRelativePath, exercise.videoRelativePath)
                && Objects.equals(video, exercise.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, about, imageData, videoRelativePath, video, isDeleted);
    }
}
