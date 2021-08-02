package com.example;

import java.io.Console;
import java.net.URLEncoder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class App
{

  public static void main( String[] args ) throws Exception
  {
    //Here will be your code

    // Host url
    String host = "https://imdb-internet-movie-database-unofficial.p.rapidapi.com/film/tt1375666";
    String charset = "UTF-8";
    // Headers for a request
    String x_rapidapi_host = "imdb-internet-movie-database-unofficial.p.rapidapi.com";
    String x_rapidapi_key = "b1e1ab1091mshcc9d4bcef44cbf0p1f1095jsn68c0743c6ce6";//Type here your key
    // Params
    String s = "Pulp";
    // Format query for preventing encoding problems
    String query = String.format("s=%s",
     URLEncoder.encode(s, charset));



    HttpResponse <String> response = Unirest.get(host)
      .header("x-rapidapi-key", x_rapidapi_key)
      .header("x-rapidapi-host", x_rapidapi_host)
      .asString();
    // System.out.println(response.getStatus());
    //   System.out.println(response.getHeaders().get("Content-Type"));
      System.out.println("FINISHED CALLING APP DONEEEEEEE");
  }
}