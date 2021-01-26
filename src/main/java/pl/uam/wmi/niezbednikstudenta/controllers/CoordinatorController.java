package pl.uam.wmi.niezbednikstudenta.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uam.wmi.niezbednikstudenta.builders.ValidationErrorBuilder;
import pl.uam.wmi.niezbednikstudenta.dtos.CoordinatorDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.exceptions.DeleteException;
import pl.uam.wmi.niezbednikstudenta.filter.CoordinatorFilter;
import pl.uam.wmi.niezbednikstudenta.services.CoordinatorService;
import pl.uam.wmi.niezbednikstudenta.services.CourseService;
import pl.uam.wmi.niezbednikstudenta.services.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    private final UserService userService;
    private final CourseService courseService;

    public CoordinatorController(CoordinatorService coordinatorService, UserService userService, CourseService courseService) {
        this.coordinatorService = coordinatorService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @PostMapping("/all")
    public Page<CoordinatorDTO> getAllCoordinators(Pageable pageable, @RequestBody(required = false) CoordinatorFilter coordinatorFilter) {
        Page<Coordinator> coordinators = coordinatorService.findAll(pageable, coordinatorFilter);
        return new PageImpl<CoordinatorDTO>(CoordinatorDTO.convertToListDTO(coordinators.getContent()), coordinators.getPageable(), coordinators.getTotalElements());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping
    public void addCoordinator(HttpServletRequest request, @RequestBody Coordinator coordinator) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, true);
            coordinatorService.save(coordinator);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @GetMapping("/{coordinatorId}/courses")
    public List<Course> getAllCoordinatorsCourses(HttpServletRequest request, @PathVariable Long coordinatorId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return courseService.findAllByCoordinator(coordinatorService.findById(coordinatorId), user);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PutMapping
    public ResponseEntity updateCoordinator(HttpServletRequest request, @Valid @RequestBody Coordinator coordinator, @ApiIgnore Errors errors) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, true);

            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
            }

            return ResponseEntity.ok().body(coordinatorService.update(coordinator));
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @DeleteMapping("/{coordinatorId}")
    public ResponseEntity<Long> deleteCoordinator(HttpServletRequest request, @PathVariable Long coordinatorId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, true);
            return ResponseEntity.ok().body(coordinatorService.delete(coordinatorId).getId());
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (DeleteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
