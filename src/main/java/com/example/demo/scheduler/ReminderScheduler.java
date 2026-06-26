package com.example.demo.scheduler;

import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private MedicineRepository repository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void checkReminder() {

        LocalTime currentTime = LocalTime.now()
                .withSecond(0)
                .withNano(0);

        List<Medicine> medicines = repository.findAll();

        for (Medicine medicine : medicines) {

            if (medicine.getReminderTime().equals(currentTime)
                    && "Pending".equals(medicine.getStatus())) {

                System.out.println("================================");
                System.out.println("MEDICINE REMINDER");
                System.out.println("Medicine : " + medicine.getMedicineName());
                System.out.println("Dosage    : " + medicine.getDosage());
                System.out.println("Time      : " + medicine.getReminderTime());
                System.out.println("User      : " + medicine.getUser().getName());
                System.out.println("================================");

                // Send email reminder
                emailService.sendReminder(
                        medicine.getUser().getEmail(),
                        medicine.getMedicineName(),
                        medicine.getDosage(),
                        medicine.getReminderTime().toString()
                );

            }

        }

    }

}