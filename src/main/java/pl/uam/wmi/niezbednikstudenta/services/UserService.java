package pl.uam.wmi.niezbednikstudenta.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javassist.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.UserDTO;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.exceptions.ValidException;
import pl.uam.wmi.niezbednikstudenta.filter.UserFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.UserRepository;
import pl.uam.wmi.niezbednikstudenta.specification.UserSpecification;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class UserService {

    @Value("${base.uri.usos}")
    private String baseUri;

    @Value("${consumer.key}")
    private String consumerKey;

    @Value("${consumer.secret}")
    private String consumerSecret;

    private static final String ENC = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static Base64 base64 = new Base64();

    private final UserRepository userRepository;
    private final AuthorizeService authorizeService;

    public UserService(UserRepository userRepository, AuthorizeService authorizeService) {
        this.userRepository = userRepository;
        this.authorizeService = authorizeService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<UserDTO> findAll(Pageable pageable, UserFilter userFilter) {

        Page<User> usersFounded = (userFilter == null) ? userRepository.findAll(pageable) : userRepository.findAll(new UserSpecification(userFilter), pageable);

        List<UserDTO> users = new ArrayList<>();
        usersFounded.getContent().forEach(user -> users.add(new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.isAdmin()
        )));

        return new PageImpl<>(users, usersFounded.getPageable(), usersFounded.getTotalElements());
    }

    public User findUserIfNotSave(User user) {

        Optional<User> optionalUser = userRepository.findById(user.getId());

        if (optionalUser.isPresent())
            return optionalUser.get();
        else
            return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    private User callToAccessUserFromUsos(String oauthToken, String oauthTokenSecret) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InterruptedException {

        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("fields", "id|first_name|last_name|email"));
        queryParams.add(new BasicNameValuePair("oauth_consumer_key", consumerKey));
        queryParams.add(new BasicNameValuePair("oauth_nonce", "" + (int) (Math.random() * 100000000)));
        queryParams.add(new BasicNameValuePair("oauth_signature_method", "HMAC-SHA1"));
        queryParams.add(new BasicNameValuePair("oauth_timestamp", "" + (System.currentTimeMillis() / 1000)));
        queryParams.add(new BasicNameValuePair("oauth_token", oauthToken));
        queryParams.add(new BasicNameValuePair("oauth_version", "1.0"));
        String oauth_signature = authorizeService.createSignature("GET", URLEncoder.encode(baseUri + "/users/user", ENC),
                URLEncoder.encode(URLEncodedUtils.format(queryParams, ENC), ENC), oauthTokenSecret);

        queryParams.add(new BasicNameValuePair("oauth_signature", oauth_signature));
        String url = baseUri + "/users/user?" + URLEncodedUtils.format(queryParams, ENC);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        User user = null;
        if (response.statusCode() == 200) {
            user = convertStringToUser(response.body());
        }

        return user;
    }

    private User convertStringToUser(String userInString) {

        JsonObject userJson = new Gson().fromJson(userInString, JsonObject.class);
        Long userId = userJson.get("id").getAsLong();
        String userName = userJson.get("first_name").getAsString();
        String userSurname = userJson.get("last_name").getAsString();

        return new User(userId, userName, userSurname, null);
    }

    public User getAndCheckIfUserHasPermission(String token, String secretToken, boolean checkIfIsAdmin) throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException, IOException {

        User user = callToAccessUserFromUsos(token, secretToken);

        if (user == null)
            throw new SecurityException("You have to log in");

        user = findUserIfNotSave(user);

        if (checkIfIsAdmin) {
            if (user.isAdmin() == false)
                throw new SecurityException("You don't have permission");
        }

        return user;
    }

    @Transactional
    public void giveOrRemoveAdmin(Long userId) throws NotFoundException {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found. Wrong id"));

        if (user.isAdmin())
            user.setAdmin(false);
        else
            user.setAdmin(true);

        userRepository.save(user);
    }

    @Transactional
    public void saveTokenFirebaseToUser(User user, String tokenFirebase) {
        user.setTokenFirebase(tokenFirebase);
        userRepository.save(user);
    }

    public void saveEmail(User user, String email) throws ValidException {

        if (isEmailValid(email)) {
            user.setEmail(email);
            userRepository.save(user);
        }
        else
            throw new ValidException("Email is not valid");
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    @Transactional
    public void setUserReadNotifications(User user, boolean isRead) {
        user.setUserReadNotifications(isRead);
        userRepository.save(user);
    }

    public UserDTO findUserById(Long userId) throws NotFoundException {

        User userFromDb = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found. Wrong id"));

        return new UserDTO(
                userFromDb.getId(),
                userFromDb.getName(),
                userFromDb.getSurname(),
                userFromDb.isAdmin(),
                userFromDb.getEmail());
    }
}
