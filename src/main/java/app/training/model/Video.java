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
@SQLDelete(sql = "UPDATE videos SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "videos")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "video_relative_path")
    private String videoRelativePath;
    @Transient
    private URL video;
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
