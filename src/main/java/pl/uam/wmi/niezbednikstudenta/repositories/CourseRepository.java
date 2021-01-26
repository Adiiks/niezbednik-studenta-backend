package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.User;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    List<Course> findAllByCourseInformationCoordinators(Coordinator coordinator);

    Page<Course> findAllByUsers(User user, Pageable pageable);
}
