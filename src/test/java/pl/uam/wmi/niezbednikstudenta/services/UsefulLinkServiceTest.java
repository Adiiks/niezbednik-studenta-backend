package pl.uam.wmi.niezbednikstudenta.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.uam.wmi.niezbednikstudenta.entities.UsefulLink;
import pl.uam.wmi.niezbednikstudenta.repositories.UsefulLinkRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class UsefulLinkServiceTest {

    @Mock
    UsefulLinkRepository usefulLinkRepository;

    @InjectMocks
    UsefulLinkService usefulLinkService;

    UsefulLink usefulLink;

    @BeforeEach
    void setUp() {
        usefulLink = new UsefulLink();
    }

    @Test
    void save() {

        String name = "name";
        String url = "url";

        usefulLink.setName(name);
        usefulLink.setUrl(url);

        when(usefulLinkRepository.save(any(UsefulLink.class))).thenReturn(usefulLink);

        UsefulLink usefulLinkSaved = usefulLinkService.save(usefulLink);

        assertNotNull(usefulLinkSaved);
        assertEquals(name, usefulLinkSaved.getName());
        assertEquals(url, usefulLinkSaved.getUrl());
    }

    @Test
    void findAll() {

        List<UsefulLink> usefulLinks = new ArrayList<>();
        usefulLinks.add(usefulLink);

        when(usefulLinkRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(usefulLinks));

        assertEquals(1, usefulLinkService.findAll(Pageable.unpaged()).getContent().size());
    }
}