package app.training.model;

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
    private TrainingSection trainingSection;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
