package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.UsefulLink;
import pl.uam.wmi.niezbednikstudenta.repositories.UsefulLinkRepository;

import java.util.List;

@Service
public class UsefulLinkService {

    private final UsefulLinkRepository usefulLinkRepository;

    public UsefulLinkService(UsefulLinkRepository usefulLinkRepository) {
        this.usefulLinkRepository = usefulLinkRepository;
    }

    public UsefulLink save(UsefulLink usefulLink) {
        usefulLink.setId(null);
        return usefulLinkRepository.save(usefulLink);
    }

    public Page<UsefulLink> findAll(Pageable pageable) {
        return usefulLinkRepository.findAll(pageable);
    }

    public void delete(Long usefulLinkId) throws NotFoundException {
        if (usefulLinkRepository.existsById(usefulLinkId))
            usefulLinkRepository.deleteById(usefulLinkId);
        else
            throw new NotFoundException("Wrong id. Cannot delete object which doesn't exist");
    }
}
