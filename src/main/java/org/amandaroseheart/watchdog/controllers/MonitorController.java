package org.amandaroseheart.watchdog.controllers;

import java.util.ArrayList;
import java.util.List;
import org.amandaroseheart.watchdog.domain.WebPage;
import org.amandaroseheart.watchdog.models.WebPageView;
import org.amandaroseheart.watchdog.repositories.WebPageRepo;
import org.amandaroseheart.watchdog.services.WatchdogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MonitorController {

  @Autowired
  private WebPageRepo webPageRepo;

  @Autowired
  private WatchdogService watchdogService;

  @GetMapping("/monitor")
  public String monitor(Model model) {
    List<WebPageView> webPages = new ArrayList<>();
    for (WebPage page : webPageRepo.getAll()) {
      String md5then = page.getMd5();
      String md5now = null;
      try {
        md5now = watchdogService.calculateMd5(page.getUrl(), page.getXpath());
      } catch (Exception e) {
        e.printStackTrace();
      }
      webPages.add(new WebPageView(page.getUrl(), md5then, !md5now.equals(md5then)));
    }
    model.addAttribute("webPages", webPages);
    return "monitor";
  }

  @GetMapping("/delete/{md5}")
  public ModelAndView delete(@PathVariable String md5, Model model) {
    webPageRepo.delete(md5);
    return new ModelAndView("redirect:/home");
  }

}
