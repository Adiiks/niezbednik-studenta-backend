package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.TicketDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Ticket;
import pl.uam.wmi.niezbednikstudenta.repositories.TicketRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final NotificationService notificationService;

    public TicketService(TicketRepository ticketRepository, NotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void saveTicket(TicketDTO ticketDTO) {

        Ticket ticketFromDb = ticketRepository.save(new Ticket(ticketDTO.getMessage(), ticketDTO.getType()));

        Map<String, String> dataForNotification = new HashMap<>();
        dataForNotification.put("messagePl", "Nowy ticket");
        dataForNotification.put("messageEn", "New ticket");
        dataForNotification.put("id", ticketFromDb.getId().toString());

        notificationService.sendToAdmin(dataForNotification);
    }

    public Page<TicketDTO> getAllTickets(Pageable pageable) {

        List<TicketDTO> ticketDTOS = new ArrayList<>();

        Page<Ticket> tickets = ticketRepository.findAll(pageable);
        tickets.getContent().forEach(ticket -> ticketDTOS.add(new TicketDTO(ticket.getId(), ticket.getMessage(), ticket.getType(), ticket.getCreatedDate())));

        return new PageImpl<>(ticketDTOS, tickets.getPageable(), tickets.getTotalElements());
    }

    @Transactional
    public Long deleteTicketById(Long ticketId) throws NotFoundException {

        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
            return ticketId;
        }
        else
            throw new NotFoundException("Ticket not found. Wrong id");
    }
}
