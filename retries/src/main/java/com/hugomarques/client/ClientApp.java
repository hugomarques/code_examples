package com.hugomarques.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.function.Function;

public class ClientApp {

  private static int NUM_ERRORS = 0;

  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

  public static void main(String[] args) throws URISyntaxException, InterruptedException {

    final HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI("http://localhost:8080"))
        .GET()
        .build();

    Function<HttpRequest, HttpResponse> service = (HttpRequest x) -> callService(x);

    while (NUM_ERRORS < 3) {
      try {
        var response = service.apply(request);
        NUM_ERRORS = 0;
        System.out.println("--------------------------------------------");
        System.out.println(Thread.currentThread().getName());
        System.out.println(response.statusCode());
        System.out.println(response.body());
        System.out.println("--------------------------------------------");
      } catch (RuntimeException ex) {
        System.out.println(ex.getMessage());
        NUM_ERRORS++;
      }
      Thread.sleep(500);
    }
  }

  private static HttpResponse<String> callService(HttpRequest request) {
    try {
      System.out.println("Executing request at" + LocalDateTime.now());
      final HttpResponse<String> response =
          HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 500)
        throw new RuntimeException("Failed to call API");
      return response;
    } catch (IOException e) {
      System.out.println("Operation failed, exception IO");
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      System.out.println("Operation failed, exception Interrupted");
      throw new RuntimeException(e);
    }
  }
}
