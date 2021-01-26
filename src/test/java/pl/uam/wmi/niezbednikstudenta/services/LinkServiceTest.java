package pl.uam.wmi.niezbednikstudenta.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.uam.wmi.niezbednikstudenta.entities.Link;
import pl.uam.wmi.niezbednikstudenta.repositories.LinkRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class LinkServiceTest {

    @Mock
    LinkRepository linkRepository;

    @InjectMocks
    LinkService linkService;

    Link link;

    @BeforeEach
    void setUp() {

        link = new Link();
    }

    @Test
    void save() {

        String url = "link";
        link.setUrl(url);

        when(linkRepository.save(any(Link.class))).thenReturn(link);

        Link savedLink = linkService.save(link);

        assertNotNull(savedLink);
        assertEquals(url, savedLink.getUrl());
    }
}