package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.CoordinatorDegree;

@Repository
public interface CoordinatorDegreeRepository extends JpaRepository<CoordinatorDegree, Long> {
}
