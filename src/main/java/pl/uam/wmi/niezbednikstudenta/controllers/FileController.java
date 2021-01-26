package pl.uam.wmi.niezbednikstudenta.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pl.uam.wmi.niezbednikstudenta.entities.File;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.FileFilter;
import pl.uam.wmi.niezbednikstudenta.services.FileService;
import pl.uam.wmi.niezbednikstudenta.services.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/{courseId}/file")
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @PostMapping("/upload")
    public ResponseEntity uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable Long courseId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);

            fileService.saveFile(file, courseId, user);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (IOException e) {
            throw  new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
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
                            "Multiple sort criteria are supported.")
    })
    @PostMapping("/all")
    public ResponseEntity getAllFiles(@PathVariable Long courseId, @ApiIgnore Pageable pageable, @RequestBody(required = false)FileFilter fileFilter) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(fileService.getAllFiles(courseId, pageable, fileFilter));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity getFile(@PathVariable Long fileId) {

        try {
            File file = fileService.getFileById(fileId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getData());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User's token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "secretToken", value = "User's secret token", required = true, dataType = "string", paramType = "header"),
    })
    @DeleteMapping("/{fileId}")
    public ResponseEntity deleteFileById(HttpServletRequest request, @PathVariable Long fileId) {

        String token = request.getHeader("token");
        String secretToken = request.getHeader("secretToken");

        try {
            User user = userService.getAndCheckIfUserHasPermission(token, secretToken, false);

            return ResponseEntity.status(HttpStatus.OK).body(fileService.deleteFileById(fileId, user));
        } catch (InterruptedException | NoSuchAlgorithmException | InvalidKeyException | SecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
