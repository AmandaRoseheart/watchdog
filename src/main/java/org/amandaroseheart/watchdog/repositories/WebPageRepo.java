package org.amandaroseheart.watchdog.repositories;

import java.util.ArrayList;
import java.util.List;
import org.amandaroseheart.watchdog.domain.WebPage;
import org.springframework.stereotype.Repository;

@Repository
public class WebPageRepo {

  private List<WebPage> webPages = new ArrayList<>();

  public void save(WebPage webPage) {
    this.webPages.add(webPage);
  }

  public void delete(String md5) {
    this.webPages.removeIf(webPage -> webPage.getMd5().equals(md5));
    System.out.println("size: " + webPages.size());
  }

  public List<WebPage> getAll() {
    return webPages;
  }

}
