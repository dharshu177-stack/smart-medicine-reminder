package com.example.demo.repository;

import com.example.demo.model.Medicine;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalTime;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    List<Medicine> findByUser(User user);
    List<Medicine> findByUserAndMedicineNameContainingIgnoreCase(User user, String medicineName);
    Medicine findFirstByUserAndReminderTimeGreaterThanEqualOrderByReminderTimeAsc(User user, LocalTime reminderTime);

}