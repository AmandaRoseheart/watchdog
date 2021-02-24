import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.MessageDigest;
import java.util.Timer;
import java.util.TimerTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

public class Demo {

  public static void main(String[] args) throws Exception {
    String uri = "https://steamcommunity.com/app/1382330/discussions/";
    String xPath = "/html/body/div[1]/div[7]/div[6]/div[1]/div/div[2]/div[1]/div[3]/div[3]/div/div[4]/a";

    String referenceMd5 = calculateMd5(uri, xPath);

    Timer myTimer = new Timer();
    TimerTask myTask = new TimerTask() {
      @Override
      public void run() {
        try {
          String currentMd5 = calculateMd5(uri, xPath);
          print(Boolean.toString(referenceMd5.equals(currentMd5)));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    myTimer.scheduleAtFixedRate(myTask, 0l, 1 * (60 * 1000));

  }

  private static String calculateMd5(String uri, String xPath)
      throws Exception {
    Document document = Jsoup.parse(getPage(uri));
    String element = Xsoup.compile(xPath).evaluate(document).get();
    return getMd5(element);
  }

  private static String getPage(final String uri) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .build();
    HttpResponse<String> response =
        client.send(request, BodyHandlers.ofString());
    return response.body();
  }

  private static String getMd5(final String input) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] messageDigest = md.digest(input.getBytes());
    BigInteger no = new BigInteger(1, messageDigest);
    String hashtext = no.toString(16);
    while (hashtext.length() < 32) {
      hashtext = "0" + hashtext;
    }
    return hashtext;
  }

  private static void print(String message) {
    System.out.println(message);
  }

}
