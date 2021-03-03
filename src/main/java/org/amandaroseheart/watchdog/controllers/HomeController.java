package org.amandaroseheart.watchdog.controllers;

import org.amandaroseheart.watchdog.models.WebPageForm;
import org.amandaroseheart.watchdog.domain.WebPage;
import org.amandaroseheart.watchdog.repositories.WebPageRepo;
import org.amandaroseheart.watchdog.services.MailService;
import org.amandaroseheart.watchdog.services.WatchdogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

  @Autowired
  private WebPageRepo webPageRepo;

  @Autowired
  private WatchdogService watchdogService;

  @Autowired
  private MailService mailService;

  @GetMapping("/home")
  public String home(Model model) {
    model.addAttribute("page", new WebPageForm());
    model.addAttribute("feedback", "");
    return "home";
  }

  @PostMapping("/home")
  public String webpageSubmit(@ModelAttribute WebPageForm page, Model model) {
    String feedback = "Web page successfully stored";
    WebPage webPage = null;
    try {
      webPage = new WebPage(page.getUrl()
          , page.getXpath()
          , page.getEmail()
          , watchdogService.calculateMd5(page.getUrl(), page.getXpath()));
    } catch (Exception e) {
      feedback = "Something went wrong: " + e.getMessage();
      model.addAttribute("page", page);
      model.addAttribute("feedback", feedback);
      return "home";
    }
    webPageRepo.save(webPage);
    model.addAttribute("page", new WebPageForm());
    model.addAttribute("feedback", feedback);
    return "home";
  }

  @GetMapping("/testmail")
  public String testMail(Model model) {
    try {
      mailService.sendMail("Test message");
    } catch (Exception e) {
      e.printStackTrace();
    }
    model.addAttribute("page", new WebPageForm());
    model.addAttribute("feedback", "");
    return "home";
  }

}
