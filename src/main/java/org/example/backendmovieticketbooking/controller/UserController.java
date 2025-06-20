package org.example.backendmovieticketbooking.controller;

import jakarta.mail.MessagingException;
import org.example.backendmovieticketbooking.entitie.Users;
import org.example.backendmovieticketbooking.service.EmailService;
import org.example.backendmovieticketbooking.service.TheaterService;
import org.example.backendmovieticketbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users") // Correctly define the base path
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TheaterService theaterService;

    @Autowired
    private EmailService emailService;

//    Get Available Seats
    @GetMapping("/getAvailableSeats")
    public ResponseEntity<Integer> getAvailableSeats(@RequestParam(required = false) String theaterName,
                                                     @RequestParam(required = false) String date,
                                                     @RequestParam(required = false) String timing) {
        System.out.println("getAvailableSeats called");
        System.out.println("Theater ID: " + theaterName);
        System.out.println("Date: " + date);
        System.out.println("Timing: " + timing);
        int availableSeats = theaterService.getSeatsAvailable(theaterName, date, timing);
        return ResponseEntity.ok(availableSeats);
    }

//    Add User
    @PostMapping("")
    public Users addUser(@RequestBody Users user) throws MessagingException { // Use @RequestBody to map JSON to User
        System.out.println("addUser called");
        int seatsNeeded = user.getNoOfSeatsBooked();
        int availableSeats = theaterService.getSeatsAvailable(user.getTheaterSelected(), user.getSelectedDate(), user.getSelectedTime());
        int totalSeats = theaterService.getTotalSeats(user.getTheaterSelected());
        int seatsBooked = totalSeats - availableSeats;

        if (seatsNeeded > availableSeats) {
            throw new RuntimeException("enough seats Not available");
        }
        if (seatsNeeded > 10) {
            throw new RuntimeException("Can only book 10 seats at a time");
        }
        theaterService.setSeatsAvailable(user.getTheaterSelected(),availableSeats, seatsNeeded, user.getSelectedDate(), user.getSelectedTime());
        String seats = theaterService.seatAllocation(seatsNeeded, availableSeats, seatsBooked);
        user.setSeatsAllocated(seats);
        userService.addUser(user);
        System.out.println(sendConfirmation(user.getEmail(), user));
        return user;
    }

//    Get All Users
    @GetMapping("")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }


//    Send Mail Confirmation
    public String sendConfirmation(String email, Users user) throws MessagingException {
        String subject = "Confirmation Email";
        String htmlContent = "<html>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;\">"
                + "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                + "<div style=\"text-align: center; margin-bottom: 20px;\">"
                + "<img src=\"https://pluspng.com/img-png/ford-logo-png-ford-logo-png-file-event-projection-1464x550.png\" style=\"max-width: 200px; height: auto;\" />"
                + "</div>"
                + "<h1 style=\"color: #333;\">Booking Confirmation</h1>"
                + "<p style=\"color: #555;\">Dear " + user.getUserName() + ",</p>"
                + "<p style=\"color: #555;\">Thank you for booking with us! Here are your booking details:</p>"
                + "<ul style=\"color: #555; list-style-type: none; padding: 0;\">"
                + "<li style=\"margin-bottom: 10px;\"><strong>Movie Selected:</strong> " + user.getMovieSelected() + "</li>"
                + "<li style=\"margin-bottom: 10px;\"><strong>Theater Selected:</strong> " + user.getTheaterSelected() + "</li>"
                + "<li style=\"margin-bottom: 10px;\"><strong>Show Time:</strong> " + user.getSelectedTime() + "</li>"
                + "<li style=\"margin-bottom: 10px;\"><strong>Show Date:</strong> " + user.getSelectedDate() + "</li>"
                + "<li style=\"margin-bottom: 10px;\"><strong>Number of Seats Booked:</strong> " + user.getNoOfSeatsBooked() + "</li>"
                + "<li style=\"margin-bottom: 10px;\"><strong>Seats Allocated:</strong> " + user.getSeatsAllocated() + "</li>"
                + "</ul>"
                + "<p style=\"color: #555;\">We hope you enjoy the movie!</p>"
                + "<p style=\"color: #555;\">Best regards,</p>"
                + "<p style=\"color: #555;\">Ford Movies</p>"
                + "</div>"
                + "</body>"
                + "</html>";


        emailService.sendConfirmationEmail(email, subject, htmlContent);

        return "Confirmation email sent to " + email;
    }
}