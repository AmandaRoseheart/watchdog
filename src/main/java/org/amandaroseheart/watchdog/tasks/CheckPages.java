package org.amandaroseheart.watchdog.tasks;

import java.util.ArrayList;
import java.util.List;
import org.amandaroseheart.watchdog.domain.WebPage;
import org.amandaroseheart.watchdog.repositories.WebPageRepo;
import org.amandaroseheart.watchdog.services.MailService;
import org.amandaroseheart.watchdog.services.WatchdogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckPages {

  @Autowired
  WebPageRepo repo;

  @Autowired
  WatchdogService watchdogService;

  @Autowired
  MailService mailService;

  @Scheduled(cron = "0 0 3 * * ?")
  public void execute() {
    System.out.println("Task started");
    List<String> toBeDeleted = new ArrayList<>();
    for (WebPage webPage : repo.getAll()) {
      String md5then = webPage.getMd5();
      String md5now = null;
      try {
        md5now = watchdogService
            .calculateMd5(webPage.getUrl(), webPage.getXpath());
        if (!md5then.equals(md5now)) {
          final String message = String
              .format("Watchdog detected that %s has changed!",
                  webPage.getUrl());
          mailService.sendMail(message);
          toBeDeleted.add(webPage.getMd5());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    toBeDeleted.stream().forEach(md5 -> repo.delete(md5));
  }
}
