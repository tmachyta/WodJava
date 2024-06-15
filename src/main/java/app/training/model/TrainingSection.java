package app.training.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE training_sections SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "training_sections")
@Data
public class TrainingSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "image_data")
    private byte[] imageData;
    @ManyToOne
    @JoinColumn(name = "training_program_id")
    @JsonIgnore
    private TrainingProgram trainingProgram;
    //@OneToMany(mappedBy = "trainingSection", cascade = CascadeType.ALL)
    //@JsonIgnore
    //private Set<Exercise> exercises;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
