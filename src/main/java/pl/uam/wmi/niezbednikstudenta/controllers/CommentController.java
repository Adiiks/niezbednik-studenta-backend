package pl.uam.wmi.niezbednikstudenta.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.uam.wmi.niezbednikstudenta.builders.ValidationErrorBuilder;
import pl.uam.wmi.niezbednikstudenta.dtos.CommentUpdateDTO;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.services.CommentService;
import pl.uam.wmi.niezbednikstudenta.services.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/forum/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/{commentId}/add-like")
    public void addLike(HttpServletRequest request, @PathVariable Long commentId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            commentService.addOrRemoveLike(commentService.findById(commentId), user);
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
    @PostMapping("/{commentId}/accept-answer")
    public void acceptAnswer(HttpServletRequest request, @PathVariable Long commentId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            commentService.acceptAnswer(commentService.findById(commentId), user);
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
    @GetMapping("/{commentId}/is-user-like")
    public boolean checkUserLikeComment(HttpServletRequest request, @PathVariable Long commentId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return commentService.isUserLikeComment(commentService.findById(commentId), user);
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
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Long> deleteCommentById(HttpServletRequest request, @PathVariable Long commentId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);
            return new ResponseEntity<>(commentService.deleteCommentById(commentId, user), HttpStatus.OK);
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
    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(HttpServletRequest request, @PathVariable Long commentId, @Valid @RequestBody CommentUpdateDTO commentUpdate, @ApiIgnore Errors errors) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);

            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
            }

            return new ResponseEntity<>(commentService.updateComment(commentId, commentUpdate, user), HttpStatus.OK);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | IOException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
