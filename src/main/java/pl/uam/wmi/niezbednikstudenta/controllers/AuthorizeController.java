package pl.uam.wmi.niezbednikstudenta.controllers;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.uam.wmi.niezbednikstudenta.exceptions.LoginException;
import pl.uam.wmi.niezbednikstudenta.services.AuthorizeService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/authorization")
public class AuthorizeController {

    private final AuthorizeService authorizeService;

    public AuthorizeController(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;
    }

    @GetMapping("/request-token")
    public JSONObject getAuthorizeUrlWithToken(@RequestParam(required = false, defaultValue = "oob") String oauthCallback) {

        try {

            return authorizeService.callRequestToken(oauthCallback);

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InterruptedException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during request token", e);
        }
    }

    @GetMapping("/access-token")
    public JSONObject getAccessToken(@RequestParam String oauthToken, @RequestParam String oauthTokenSecret, @RequestParam String oauthVerifier) {

        try {

            return authorizeService.callAccessToken(oauthToken, oauthTokenSecret, oauthVerifier);

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InterruptedException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during calling access token", e);
        } catch (LoginException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logOut(@RequestParam String oauthToken, @RequestParam String oauthTokenSecret) {

        try {

            return authorizeService.logOut(oauthToken, oauthTokenSecret);

        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InterruptedException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during logout", e);
        }
    }
}
