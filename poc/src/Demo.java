import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.MessageDigest;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.JaccardSimilarity;

public class Demo {

  public static void main(String[] args) throws Exception {
    String uri1 = "https://steamcommunity.com/app/1289340/discussions/";
    String uri2 = "https://steamcommunity.com/app/1289340/discussions/"; //"https://chess.com";

    String page1 = getPage(uri1);
    String hash1 = getMd5(page1);
    print(hash1);

    String page2 = getPage(uri2);
    String hash2 = getMd5(page2);
    print(hash2);

    print("Hashes are equal: " + hash1.equals(hash2));
    print("CosineDistance: " + computeCosineSimilarity(page1, page2) * 100);
    print("JaccardSimilarity: " + computeJaccardSimilarity(page1, page2) * 100);

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

  private static double computeCosineSimilarity(String stringA, String stringB) {
    return 1 - new CosineDistance().apply(stringA, stringB);
  }

  private static double computeJaccardSimilarity(String stringA, String stringB) {
    return new JaccardSimilarity().apply(stringA, stringB);
  }

  private static void print(String message) {
    System.out.println(message);
  }

}
