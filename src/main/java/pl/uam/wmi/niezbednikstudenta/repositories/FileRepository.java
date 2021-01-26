package pl.uam.wmi.niezbednikstudenta.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long>, JpaSpecificationExecutor<File> {

    Page<File> findAllByCourse(Course course, Pageable pageable);
}
