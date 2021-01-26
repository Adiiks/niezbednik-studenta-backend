package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.PrivateLessons;

@Repository
public interface PrivateLessonsRepository extends JpaRepository<PrivateLessons, Long>, JpaSpecificationExecutor<PrivateLessons> {
}
