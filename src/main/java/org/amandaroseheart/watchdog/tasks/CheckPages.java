package org.amandaroseheart.watchdog.tasks;

import org.amandaroseheart.watchdog.domain.WebPage;
import org.amandaroseheart.watchdog.repositories.WebPageRepo;
import org.amandaroseheart.watchdog.services.WatchdogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckPages {

  @Autowired
  WebPageRepo repo;

  @Autowired
  WatchdogService service;

  @Scheduled(cron = "0 0 3 * * ?")
  public void execute() {
    System.out.println("Task started");
    for (WebPage webPage : repo.getAll()) {
      String md5then = webPage.getMd5();
      String md5now = null;
      try {
        md5now = service.calculateMd5(webPage.getUrl(), webPage.getXpath());
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (!md5then.equals(md5now)) System.out.println("Send mail");
    }
  }
}
