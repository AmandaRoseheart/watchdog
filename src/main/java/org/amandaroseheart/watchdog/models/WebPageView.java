package org.amandaroseheart.watchdog.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebPageView {

  private String url;
  private String md5;
  private Boolean changed;

}
