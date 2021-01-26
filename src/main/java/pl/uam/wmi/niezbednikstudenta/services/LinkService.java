package pl.uam.wmi.niezbednikstudenta.services;

import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.Link;
import pl.uam.wmi.niezbednikstudenta.repositories.LinkRepository;

import java.util.List;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Link save(Link link) {
        return linkRepository.save(link);
    }

    public void deleteAll(List<Link> linksToDelete) {
        linkRepository.deleteAll(linksToDelete);
    }
}
