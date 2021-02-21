import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.MessageDigest;

public class Demo {

  public static void main(String[] args) throws Exception {
//    String uri = "https://steamcommunity.com/app/579180/allnews/";
    String uri = "https://chess.com";

    String hash1 = getMd5(getPage(uri));
    print(hash1);
    String hash2 = getMd5(getPage(uri));
    print(hash2);
    print("Hashes are equal: " + hash1.equals(hash2));
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
