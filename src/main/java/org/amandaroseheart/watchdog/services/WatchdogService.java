package org.amandaroseheart.watchdog.services;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.MessageDigest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import us.codecraft.xsoup.Xsoup;

@Service
public class WatchdogService {

  public String calculateMd5(String uri, String xPath) throws Exception {
    String md5 = null;
    Document document = Jsoup.parse(getPage(uri));
    String element = Xsoup.compile(xPath).evaluate(document).get();
    md5 = getMd5(element);
    return md5;
  }

  private String getPage(final String uri) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .build();
    HttpResponse<String> response =
        client.send(request, BodyHandlers.ofString());
    return response.body();
  }

  private String getMd5(final String input) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] messageDigest = md.digest(input.getBytes());
    BigInteger no = new BigInteger(1, messageDigest);
    String hashtext = no.toString(16);
    while (hashtext.length() < 32) {
      hashtext = "0" + hashtext;
    }
    return hashtext;
  }

}
