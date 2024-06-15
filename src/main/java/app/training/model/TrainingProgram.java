package app.training.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE training_programs SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "training_programs")
@Data
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String about;
    @Column(name = "image_data")
    private byte[] imageData;
    private LocalDate date;
    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    //@JsonIgnore
    private Set<TrainingSection> sections;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
