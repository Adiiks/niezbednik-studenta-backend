package pl.uam.wmi.niezbednikstudenta.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uam.wmi.niezbednikstudenta.builders.ValidationErrorBuilder;
import pl.uam.wmi.niezbednikstudenta.dtos.TicketDTO;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.services.TicketService;
import pl.uam.wmi.niezbednikstudenta.services.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping
    public ResponseEntity createTicket(HttpServletRequest request, @Valid @RequestBody TicketDTO ticketDTO, @ApiIgnore Errors errors) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);

            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
            }

            ticketService.saveTicket(ticketDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported."),
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity getAllTickets(HttpServletRequest request, @ApiIgnore Pageable pageable) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, true);
            return new ResponseEntity(ticketService.getAllTickets(pageable), HttpStatus.OK);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @DeleteMapping("/{ticketId}")
    public ResponseEntity deleteTicketById(HttpServletRequest request, @PathVariable Long ticketId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, true);
            return new ResponseEntity(ticketService.deleteTicketById(ticketId), HttpStatus.OK);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
