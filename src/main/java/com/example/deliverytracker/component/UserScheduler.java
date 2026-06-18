package com.example.deliverytracker.component;

import com.example.deliverytracker.image.service.ImageService;
import com.example.deliverytracker.user.entitiy.User;
import com.example.deliverytracker.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserScheduler {

    private final UserRepository userRepository;

    private final ImageService imageService;

    @Scheduled(cron="0 0 3 * * *")
    @Transactional
    public void purgeWithdrawUsers(){

        LocalDateTime cutoff =
                LocalDateTime.now()
                        .minusDays(30);

        List<User> users = userRepository.findByStatusAndWithdrawnAtBefore(User.Status.WITHDRAWN, cutoff);

        users.forEach(user->{
            imageService.delete(
                    user.getImageUrl()
            );

            userRepository.delete(user);

        });

    }
}
