package app.training.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE exercises SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "exercises")
@Data
@ToString(exclude = "videos")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String about;
    @Column(name = "image_data", columnDefinition = "BLOB")
    private byte[] imageData;
    @ManyToOne
    @JoinColumn(name = "training_section_id")
    private TrainingSection trainingSection;
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Video> videos;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
