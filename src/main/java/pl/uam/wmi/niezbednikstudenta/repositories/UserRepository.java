package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
