package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.NoticeDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.UserDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Notice;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.NoticeFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.NoticeRepository;
import pl.uam.wmi.niezbednikstudenta.specification.NoticeSpecification;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    public void saveNotice(NoticeDTO noticeDTO, User user) {
        noticeRepository.save(new Notice(noticeDTO.getContent(), user));
    }

    public Page<NoticeDTO> getAllNotices(Pageable pageable, NoticeFilter noticeFilter) {

        List<NoticeDTO> noticeDTOS = new ArrayList<>();

        Page<Notice> notices = (noticeFilter == null) ? noticeRepository.findAll(pageable) : noticeRepository.findAll(new NoticeSpecification(noticeFilter), pageable);
        notices.forEach(notice -> {
            User author = notice.getAuthor();
            noticeDTOS.add(new NoticeDTO(
                    notice.getId(),
                    notice.getContent(),
                    notice.getCreatedDate(),
                    new UserDTO(
                            author.getId(),
                            author.getName(),
                            author.getSurname()
                    ),
                    notice.isEdited()
            ));
        });

        return new PageImpl<NoticeDTO>(noticeDTOS, notices.getPageable(), notices.getTotalElements());
    }

    @Transactional
    public Long deleteNoticeById(User user, Long noticeId) throws NotFoundException, SecurityException {

        Notice noticeForDelete = noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundException("Notice not found. Wrong id"));

        if (noticeForDelete.getAuthor().equals(user) || user.isAdmin()) {
            noticeRepository.deleteById(noticeId);
            return noticeForDelete.getId();
        }
        else
            throw new SecurityException("You are not author of this notice");
    }

    @Transactional
    public NoticeDTO updateNotice(Long noticeId, NoticeDTO noticeDTO, User user) throws NotFoundException {

        Notice noticeToUpdate = noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundException("Notice not found. Wrong id"));

        if (noticeToUpdate.getAuthor().equals(user)) {

            noticeToUpdate.setContent(noticeDTO.getContent());
            noticeToUpdate.setEdited(true);
            noticeToUpdate = noticeRepository.save(noticeToUpdate);

            User author = noticeToUpdate.getAuthor();
            noticeDTO = new NoticeDTO(noticeToUpdate.getId(),
                    noticeToUpdate.getContent(),
                    noticeToUpdate.getCreatedDate(),
                    new UserDTO(
                            author.getId(),
                            author.getName(),
                            author.getSurname()
                    ),
                    noticeToUpdate.isEdited());

            return noticeDTO;
        }
        else
            throw new SecurityException("Cannot edit notice. You don't have permission");
    }
}
