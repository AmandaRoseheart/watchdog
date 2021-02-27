package org.amandaroseheart.watchdog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebPage {

  private String url;
  private String xpath;
  private String email;
  private String md5;

}
