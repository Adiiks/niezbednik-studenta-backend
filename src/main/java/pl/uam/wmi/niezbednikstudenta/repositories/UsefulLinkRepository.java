package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.UsefulLink;

@Repository
public interface UsefulLinkRepository extends JpaRepository<UsefulLink, Long> {
}
