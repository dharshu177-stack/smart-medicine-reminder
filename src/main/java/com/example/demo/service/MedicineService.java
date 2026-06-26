package com.example.demo.service;

import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import java.util.List;
import java.time.LocalTime;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository repository;

    public Medicine saveMedicine(Medicine medicine) {
        return repository.save(medicine);
    }

    public List<Medicine> getAllMedicines() {
        return repository.findAll();
    }
    public Medicine getMedicineById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void updateMedicine(Medicine medicine) {
        repository.save(medicine);
    }
    public void deleteMedicine(Long id) {
        repository.deleteById(id);
    }
    public void markAsTaken(Long id) {

        Medicine medicine = repository.findById(id).orElse(null);

        if (medicine != null) {
            medicine.setStatus("Taken");
            repository.save(medicine);
        }
    }
    public List<Medicine> getMedicinesByUser(User user) {
        return repository.findByUser(user);
    }
    public long getTotalMedicines(User user) {
        return repository.findByUser(user).size();
    }

    public long getTakenMedicines(User user) {
        return repository.findByUser(user)
                .stream()
                .filter(m -> "Taken".equals(m.getStatus()))
                .count();
    }

    public long getPendingMedicines(User user) {
        return repository.findByUser(user)
                .stream()
                .filter(m -> "Pending".equals(m.getStatus()))
                .count();
    }
    public List<Medicine> searchMedicine(User user, String keyword) {
        return repository.findByUserAndMedicineNameContainingIgnoreCase(user, keyword);
    }
    public Medicine getNextMedicine(User user) {

        return repository.findFirstByUserAndReminderTimeGreaterThanEqualOrderByReminderTimeAsc(
                user,
                LocalTime.now().withSecond(0).withNano(0)
        );

    }
}