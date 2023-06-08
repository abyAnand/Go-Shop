package io.ecommerce.GoShop.scheduledtask;

import io.ecommerce.GoShop.Otp.OtpRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataDeletionService {

    private final OtpRepository repository;

    public DataDeletionService(OtpRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0/5 * * * *") // Runs every 5 minutes
    public void deleteData() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime thresholdTime = currentTime.minusMinutes(5);

        repository.deleteByCreatedDateLessThan(thresholdTime);
    }
}