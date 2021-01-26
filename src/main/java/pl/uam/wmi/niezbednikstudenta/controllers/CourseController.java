package pl.uam.wmi.niezbednikstudenta.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uam.wmi.niezbednikstudenta.builders.ValidationErrorBuilder;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.CourseFilter;
import pl.uam.wmi.niezbednikstudenta.services.CourseService;
import pl.uam.wmi.niezbednikstudenta.services.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final UserService userService;
    private final CourseService courseService;

    public CourseController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping
    public void addCourse(HttpServletRequest request, @RequestBody Course course) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, true);
            courseService.save(course);
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
    @PostMapping("/join")
    public void joinToCourse(HttpServletRequest request, @RequestParam Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            courseService.addUserToCourse(user, courseId);
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
    @GetMapping("/is-member")
    public boolean checkUserIsMemberOfCourse(HttpServletRequest request, @RequestParam Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
             return courseService.isMemberOfCourse(user, courseId);
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
    @PostMapping("/all")
    public Page<Course> getAllCourses(HttpServletRequest request, @RequestBody(required = false) CourseFilter courseFilter, @ApiIgnore Pageable pageable) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return courseService.findAll(user, courseFilter, pageable);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/{courseId}/leave")
    public void leaveCourse(HttpServletRequest request, @PathVariable Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            courseService.removeUserFromCourse(user, courseService.findById(courseId));
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PutMapping
    public ResponseEntity updateCourse(HttpServletRequest request, @Valid @RequestBody Course course, @ApiIgnore Errors errors) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, true);

            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
            }

            return ResponseEntity.ok().body(courseService.updateCourse(course));
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
    @DeleteMapping("/{courseId}")
    public ResponseEntity deleteCourseByiD(HttpServletRequest request, @PathVariable Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, true);

            return ResponseEntity.ok().body(courseService.deleteCourseById(courseId));
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
    @GetMapping("/{courseId}")
    public ResponseEntity getCourseById(HttpServletRequest request, @PathVariable Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return new ResponseEntity(courseService.findById(courseId, user), HttpStatus.OK);
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
    @GetMapping("/{courseId}/members")
    public ResponseEntity getMembers(HttpServletRequest request, @PathVariable Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return new ResponseEntity(courseService.getAllMembersFromCourse(courseId), HttpStatus.OK);
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
    @PostMapping("/my-courses")
    public ResponseEntity getAllUserCourses(HttpServletRequest request, @RequestBody(required = false)CourseFilter courseFilter, @ApiIgnore Pageable pageable) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return new ResponseEntity(courseService.getUserCourses(user, courseFilter, pageable), HttpStatus.OK);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
