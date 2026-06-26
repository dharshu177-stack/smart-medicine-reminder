package com.example.demo.controller;

import com.example.demo.model.Medicine;
import com.example.demo.model.User;
import com.example.demo.service.MedicineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        model.addAttribute("medicine", new Medicine());

        model.addAttribute("medicineList",
                medicineService.getMedicinesByUser(user));

        model.addAttribute("totalMedicines",
                medicineService.getTotalMedicines(user));

        model.addAttribute("takenMedicines",
                medicineService.getTakenMedicines(user));

        model.addAttribute("pendingMedicines",
                medicineService.getPendingMedicines(user));

        model.addAttribute("nextMedicine",
                medicineService.getNextMedicine(user));

        return "index";
    }

    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute Medicine medicine,
                               HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        medicine.setUser(user);

        medicineService.saveMedicine(medicine);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editMedicine(@PathVariable Long id,
                               Model model,
                               HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        Medicine medicine = medicineService.getMedicineById(id);

        model.addAttribute("user", user);
        model.addAttribute("medicine", medicine);

        return "edit";
    }

    @PostMapping("/update")
    public String updateMedicine(@ModelAttribute Medicine medicine,
                                 HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        medicine.setUser(user);

        medicineService.updateMedicine(medicine);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {

        medicineService.deleteMedicine(id);

        return "redirect:/";
    }

    @GetMapping("/taken/{id}")
    public String markAsTaken(@PathVariable Long id) {

        medicineService.markAsTaken(id);

        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchMedicine(@RequestParam String keyword,
                                 jakarta.servlet.http.HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("medicine", new Medicine());

        model.addAttribute("medicineList",
                medicineService.searchMedicine(user, keyword));

        model.addAttribute("totalMedicines",
                medicineService.getTotalMedicines(user));

        model.addAttribute("takenMedicines",
                medicineService.getTakenMedicines(user));

        model.addAttribute("pendingMedicines",
                medicineService.getPendingMedicines(user));

        return "index";
    }

    @GetMapping("/reminder")
    @ResponseBody
    public String reminder(HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            return "";
        }

        LocalTime currentTime = LocalTime.now()
                .withSecond(0)
                .withNano(0);

        List<Medicine> medicines = medicineService.getMedicinesByUser(user);

        for (Medicine medicine : medicines) {

            if (medicine.getReminderTime().equals(currentTime)
                    && !"Taken".equals(medicine.getStatus())) {

                return "Time to take "
                        + medicine.getMedicineName()
                        + " ("
                        + medicine.getDosage()
                        + ")";
            }
        }

        return "";
    }

}