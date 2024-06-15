package app.training.repository;

import app.training.model.TrainingSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSectionRepository extends JpaRepository<TrainingSection, Long> {
}
