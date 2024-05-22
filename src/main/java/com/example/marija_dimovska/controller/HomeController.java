package com.example.marija_dimovska.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class HomeController {

  private char[] correct = "________".toCharArray();
  private StringBuilder incorrectGuesses = new StringBuilder();

  @GetMapping("/")
  public String getHome() {
    return "home";
  }

  @GetMapping("/date")
  public String getDate(@RequestParam(required = false) String time, Model model, HttpServletRequest request) {
    Map<String, String[]> parameterMap = request.getParameterMap();
    for (String s : parameterMap.keySet()) {
      if (!s.equals("time")) {
        model.addAttribute("date",
                "INVALID OPERАTION - the service only functions either without any parameters or with the parameter \"time\", used with or without a value");
        return "date";
      }
    }
    if (time == null) {
      LocalDate currentDate = LocalDate.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String date = currentDate.format(formatter);
      model.addAttribute("date", date);
    } else {
      String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
      model.addAttribute("date", date);
    }
    return "date";
  }

  @RequestMapping(value = "/hangman", method = {RequestMethod.GET, RequestMethod.POST})
  public String getHangMan(@RequestParam(value = "operand1", required = false) String operand1, HttpServletRequest request, Model model) {
    if (request.getParameterNames() == null) {
      return "hangmanerror";
    }
    if (operand1 == null || operand1.trim().isEmpty() || operand1.length() > 1) {
      return "hangmanerror";
    }

    char[] grad = "СТРУМИЦА".toCharArray();
    operand1 = operand1.toUpperCase();
    boolean isCorrectGuess = false;

    for (int i = 0; i < grad.length; i++) {
      if (correct[i] == '_' && operand1.charAt(0) == grad[i]) {
        correct[i] = operand1.charAt(0);
        isCorrectGuess = true;
      }
    }

    if (!isCorrectGuess) {
      incorrectGuesses.append(operand1.charAt(0)).append(" ");
    }

    String correctString = new String(correct);
    if (correctString.equals(new String(grad))) {
      model.addAttribute("correct", "You guessed correctly - " + correctString);
      incorrectGuesses.setLength(0);
    } else {
      model.addAttribute("correct", new String(correct));
      if (!isCorrectGuess) {
        model.addAttribute("message", "Incorrect guess: " + operand1.charAt(0));
      }
    }

    model.addAttribute("incorrectGuesses", "Incorrect guesses: " + incorrectGuesses.toString());
    return "hangman";
  }
}
