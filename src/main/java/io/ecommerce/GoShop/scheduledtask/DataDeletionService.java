package io.ecommerce.GoShop.scheduledtask;

import io.ecommerce.GoShop.Auth.Otp.OtpRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Service
public class DataDeletionService {

    private final OtpRepository repository;

    public DataDeletionService(OtpRepository repository) {
        this.repository = repository;
    }

//    @Scheduled(cron = "0 0/5 * * * *") // Runs every 5 minutes
//    public void deleteData() {
//        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(5);
//        Timestamp timestamp = Timestamp.valueOf(thresholdTime);
//
//        repository.deleteByCreatedDateBefore(timestamp);
//    }
}