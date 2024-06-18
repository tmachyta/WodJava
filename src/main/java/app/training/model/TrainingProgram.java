package app.training.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE training_programs SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "training_programs")
@Data
@ToString(exclude = "sections")
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String about;
    @Column(name = "image_data")
    private byte[] imageData;
    private LocalDate date;
    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainingSection> sections;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
