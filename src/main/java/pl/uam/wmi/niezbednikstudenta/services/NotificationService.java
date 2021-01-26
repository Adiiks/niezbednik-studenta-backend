package pl.uam.wmi.niezbednikstudenta.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.NotificationDTO;
import pl.uam.wmi.niezbednikstudenta.entities.Notification;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.repositories.NotificationRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;

        try {

            InputStream in = getClass().getClassLoader().getResourceAsStream("firebase.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAdmin(Map<String, String> data) {

        Message message = Message.builder()
                .putAllData(data)
                .setTopic("admin")
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Sent message: " + response);
    }

    public void sendToUser(Map<String, String> data, String token) {

        if (token != null) {

            Message message = Message.builder()
                    .putAllData(data)
                    .setToken(token)
                    .build();

            String response = null;
            try {
                response = FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
            System.out.println("Sent message: " + response);
        }
    }



    public void subscribeToTopic(String topic, String clientToken) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopicAsync(Collections.singletonList(clientToken), topic).get();
            System.out
                    .println(response.getSuccessCount() + " tokens were subscribed successfully");
        }
        catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Page<NotificationDTO> getNotificationsForUser(User user, Pageable pageable) {

        Page<Notification> notificationsFromDb = notificationRepository.findAllByUsers(user, pageable);

        List<NotificationDTO> notificationsToReturn = new ArrayList<>();
        notificationsFromDb.getContent().forEach(notification -> notificationsToReturn.add(new NotificationDTO(
                notification.getId(),
                notification.getContent(),
                notification.getCreatedTime(),
                notification.getType(),
                notification.getIdOfObjectInvolve(),
                notification.getContentEn()
        )));

        return new PageImpl<>(notificationsToReturn, notificationsFromDb.getPageable(), notificationsFromDb.getTotalElements());
    }

    @Transactional
    public void deleteUserNotification(User user, Long notificationId) throws NotFoundException {

        Notification notificationFromDb = notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException("Notification not found. Wrong id"));

        notificationFromDb.removeUser(user);

        notificationRepository.save(notificationFromDb);
    }
}
