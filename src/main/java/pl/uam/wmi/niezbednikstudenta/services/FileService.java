package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.uam.wmi.niezbednikstudenta.dtos.ResponseFileDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.UserDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.File;
import pl.uam.wmi.niezbednikstudenta.entities.Notification;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.FileFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.FileRepository;
import pl.uam.wmi.niezbednikstudenta.specification.FileSpecification;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final CourseService courseService;
    private final NotificationService notificationService;

    public FileService(FileRepository fileRepository, CourseService courseService, NotificationService notificationService) {
        this.fileRepository = fileRepository;
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @Transactional
    public void saveFile(MultipartFile file, Long courseId, User user) throws IOException, NotFoundException {

        Course course = courseService.findById(courseId);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File fileToSave = new File(fileName, file.getContentType(), file.getBytes());

        course.addFileToMaterials(fileToSave);
        fileToSave.setAuthor(user);

        File fileFromDb = fileRepository.save(fileToSave);

        Set<User> membersOfCourse = course.getUsers();
        membersOfCourse.remove(fileFromDb.getAuthor());

        Map<String, String> dataForNotification = new HashMap<>();
        dataForNotification.put("messagePl", "Dodano nowy plik w przedmiocie " + course.getName());
        dataForNotification.put("messageEn", "Added new file in course " + course.getName());
        dataForNotification.put("id", fileFromDb.getId().toString());

        membersOfCourse.forEach(member -> notificationService.sendToUser(dataForNotification, member.getTokenFirebase()));

        String contentForNotification = "Użytkownik " + fileFromDb.getAuthor().getName() + " " + fileFromDb.getAuthor().getSurname() + " dodał nowy plik w " + course.getName();
        String contentEnForNotification = "User " + fileFromDb.getAuthor().getName() + " " + fileFromDb.getAuthor().getSurname() + " added new file in " + course.getName();
        Notification notification = new Notification(contentForNotification, contentEnForNotification, "PLIK", course.getId());
        membersOfCourse.forEach(member -> notification.addUser(member));
        notificationService.saveNotification(notification);
    }

    public File getFileById(Long fileId) throws NotFoundException {
        return fileRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found. Wrong id"));
    }

    public Page<ResponseFileDTO> getAllFiles(Long courseId, Pageable pageable, FileFilter fileFilter) throws NotFoundException {

        Course course = courseService.findById(courseId);

        Page<File> filesFromDB = (fileFilter == null) ? fileRepository.findAllByCourse(course, pageable) : fileRepository.findAll(new FileSpecification(fileFilter, course), pageable);
        List<ResponseFileDTO> files = filesFromDB.getContent().stream().map(file -> {

            User author = file.getAuthor();

            return new ResponseFileDTO(
                    file.getId(),
                    file.getName(),
                    file.getType(),
                    file.getData().length,
                    new UserDTO(
                            author.getId(),
                            author.getName(),
                            author.getSurname()
                    ),
                    file.getCreatedDate());
        }).collect(Collectors.toList());

        return new PageImpl<ResponseFileDTO>(files, filesFromDB.getPageable(), filesFromDB.getTotalElements());
    }

    public Long deleteFileById(Long fileId, User user) throws NotFoundException {

        File fileToDelete = fileRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found. Wrong id"));

        if (fileToDelete.getAuthor().equals(user) || user.isAdmin()) {

            fileRepository.delete(fileToDelete);
            return fileToDelete.getId();
        }
        else
            throw new SecurityException("Cannot delete file. You don't have permission");
    }
}
