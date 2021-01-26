package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.StudiesMajor;

import java.util.Optional;

@Repository
public interface StudiesMajorRepository extends JpaRepository<StudiesMajor, Long> {

    Optional<StudiesMajor> findByName(String name);
}
