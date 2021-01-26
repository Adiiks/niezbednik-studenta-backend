package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.CourseDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.PrivateLessonsCreateDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.PrivateLessonsDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.UserDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.Notification;
import pl.uam.wmi.niezbednikstudenta.entities.PrivateLessons;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.PrivateLessonsFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.PrivateLessonsRepository;
import pl.uam.wmi.niezbednikstudenta.specification.PrivateLessonsSpecification;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PrivateLessonsService {

    private final PrivateLessonsRepository privateLessonsRepository;
    private final CourseService courseService;
    private final NotificationService notificationService;

    public PrivateLessonsService(PrivateLessonsRepository privateLessonsRepository, CourseService courseService, NotificationService notificationService) {
        this.privateLessonsRepository = privateLessonsRepository;
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @Transactional
    public void savePrivateLessons(PrivateLessonsCreateDTO privateLessonsDTO, User user) throws NotFoundException {

        Course course = courseService.findById(privateLessonsDTO.getCourse().getId());

        PrivateLessons privateLessons = new PrivateLessons(privateLessonsDTO.getContent(), privateLessonsDTO.getType());

        course.addPrivateLessons(privateLessons);
        user.addPrivateLessons(privateLessons);

        PrivateLessons privateLessonsFromDb = privateLessonsRepository.save(privateLessons);

        Set<User> members = course.getUsers();
        members.remove(privateLessonsFromDb.getAuthor());

        Map<String, String> dataForNotification = new HashMap<>();
        dataForNotification.put("messagePl", "Utworzono korepetycje w przedmiocie " + course.getName());
        dataForNotification.put("messageEn", "Created new private lessons in course " + course.getName());
        dataForNotification.put("id", privateLessonsFromDb.getId().toString());

        members.forEach(member -> notificationService.sendToUser(dataForNotification, member.getTokenFirebase()));

        String contentNotification ="Użytkownik " + privateLessonsFromDb.getAuthor().getName() + " " + privateLessonsFromDb.getAuthor().getSurname() +
                " utworzył korepetycje w " + course.getName();
        String contentEnNotification ="User " + privateLessonsFromDb.getAuthor().getName() + " " + privateLessonsFromDb.getAuthor().getSurname() +
                " created private lessons in " + course.getName();
        Notification notification = new Notification(contentNotification, contentEnNotification, "KOREPETYCJE-UTWORZENIE", course.getId());
        members.forEach(member -> notification.addUser(member));
        notificationService.saveNotification(notification);
    }

    public Page<PrivateLessonsDTO> getAllPrivateLessons(Pageable pageable, PrivateLessonsFilter privateLessonsFilter, User user) {

        Page<PrivateLessons> privateLessons = (privateLessonsFilter == null) ? privateLessonsRepository.findAll(pageable) :
                privateLessonsRepository.findAll(new PrivateLessonsSpecification(privateLessonsFilter), pageable);

        List<PrivateLessonsDTO> privateLessonsToReturn = new ArrayList<>();
        privateLessons.getContent().forEach(privateLessonsIterator -> {
            User author = privateLessonsIterator.getAuthor();
            Course course = privateLessonsIterator.getCourse();
            PrivateLessonsDTO privateLessonsDTO = new PrivateLessonsDTO(
                    privateLessonsIterator.getId(),
                    new UserDTO(
                            author.getId(),
                            author.getName(),
                            author.getSurname(),
                            author.isAdmin()
                    ),
                    new CourseDTO(
                            course.getId(),
                            course.getName()
                    ),
                    privateLessonsIterator.getType(),
                    privateLessonsIterator.getContent(),
                    privateLessonsIterator.getCreatedDate(),
                    privateLessonsIterator.isEdited()
            );

            if (privateLessonsIterator.getUserInterested().contains(user))
                privateLessonsDTO.setUserInterested(true);

            privateLessonsToReturn.add(privateLessonsDTO);
        });

        return new PageImpl<>(privateLessonsToReturn, privateLessons.getPageable(), privateLessons.getTotalElements());
    }

    @Transactional
    public Long deletePrivateLessonsById(Long privateLessonsId, User user) throws NotFoundException, SecurityException {

        PrivateLessons privateLessons = privateLessonsRepository.findById(privateLessonsId).orElseThrow(() -> new NotFoundException("Private lessons not found. Wrong id"));

        if (privateLessons.getAuthor().equals(user) || user.isAdmin()) {

            privateLessons.getAuthor().removePrivateLessons(privateLessons);
            privateLessons.getCourse().removePrivateLessons(privateLessons);

            privateLessonsRepository.deleteById(privateLessonsId);

            return privateLessons.getId();
        }
        else
            throw new SecurityException("Forbidden");
    }

    @Transactional
    public PrivateLessonsDTO editPrivateLessons(PrivateLessonsCreateDTO privateLessonsDTO, Long privateLessonsId, User user) throws NotFoundException {

        PrivateLessons privateLessonsFromDb = privateLessonsRepository.findById(privateLessonsId).orElseThrow(() -> new NotFoundException("Private lessons not found. Wrong id"));
        Course course = courseService.findById(privateLessonsDTO.getCourse().getId());

        if (privateLessonsFromDb.getAuthor().equals(user)) {

            privateLessonsFromDb.setContent(privateLessonsDTO.getContent());
            privateLessonsFromDb.setType(privateLessonsDTO.getType());
            privateLessonsFromDb.setEdited(true);

            if (!privateLessonsFromDb.getCourse().equals(course)) {

                privateLessonsFromDb.getCourse().removePrivateLessons(privateLessonsFromDb);
                course.addPrivateLessons(privateLessonsFromDb);
            }

            privateLessonsFromDb = privateLessonsRepository.save(privateLessonsFromDb);
            course = privateLessonsFromDb.getCourse();
            User author = privateLessonsFromDb.getAuthor();

            return new PrivateLessonsDTO(
                    privateLessonsFromDb.getId(),
                    new UserDTO(
                            author.getId(),
                            author.getName(),
                            author.getSurname(),
                            author.isAdmin()
                    ),
                    new CourseDTO(
                            course.getId(),
                            course.getName()
                    ),
                    privateLessonsFromDb.getType(),
                    privateLessonsFromDb.getContent(),
                    privateLessonsFromDb.getCreatedDate(),
                    privateLessonsFromDb.isEdited()
            );
        }
        else
            throw new SecurityException("Forbidden");
    }

    @Transactional
    public void markOrUnmarkUserInterested(User user, Long privateLessonsId) throws NotFoundException {

        PrivateLessons privateLessonsFromDb = privateLessonsRepository.findById(privateLessonsId).orElseThrow(() -> new NotFoundException("Private lessons not found. Wrong id"));

        if (privateLessonsFromDb.getUserInterested().contains(user))
            privateLessonsFromDb.removeUserInterested(user);
        else {

            privateLessonsFromDb.addUserInterested(user);

            User author = privateLessonsFromDb.getAuthor();
            String contentForNotification = "Użytkownik " + user.getName() + " " + user.getSurname() + " jest zainteresowany twoim ogłoszeniem w przedmiocie "
                    + privateLessonsFromDb.getCourse().getName();
            String contentEnForNotification = "User " + user.getName() + " " + user.getSurname() + " is interested your notice in course  "
                    + privateLessonsFromDb.getCourse().getName();
            Notification notificationForAuthor = new Notification(contentForNotification, contentEnForNotification, "KOREPETYCJE-ZAINTERESOWANIE ", user.getId());

            if (!author.getNotifications().contains(notificationForAuthor)) {

                Map<String, String> dataForNotification = new HashMap<>();
                dataForNotification.put("message", "Użytkownik " + user.getName() + " " + user.getSurname() + " jest zainteresowany twoim ogłoszeniem w przedmiocie "
                        + privateLessonsFromDb.getCourse().getName());
                notificationService.sendToUser(dataForNotification, author.getTokenFirebase());

                notificationForAuthor.addUser(author);
                notificationService.saveNotification(notificationForAuthor);
            }
        }

        privateLessonsRepository.save(privateLessonsFromDb);
    }

    public boolean checkIfUserInterested(User user, Long privateLessonsId) throws NotFoundException {

        PrivateLessons privateLessonsFromDb = privateLessonsRepository.findById(privateLessonsId).orElseThrow(() -> new NotFoundException("Private lessons not found. Wrong id"));

        if (privateLessonsFromDb.getUserInterested().contains(user))
            return true;
        else
            return false;
    }
}
