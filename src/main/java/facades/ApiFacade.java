package facades;

import com.google.gson.JsonArray;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.StarWarsDTO;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ApiFacade {

    private static ApiFacade instance;

    //Private Constructor to ensure Singleton
    private ApiFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ApiFacade getApiFacade() {
        if (instance == null) {
            instance = new ApiFacade();
        }
        return instance;
    }

    private String getSwappi(String urlApi) throws MalformedURLException, IOException {
        URL url = new URL(urlApi);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("User-Agent", "server"); //remember if you are using SWAPI
        String jsonStr;
        try (Scanner scan = new Scanner(con.getInputStream())) {
            jsonStr = null;
            if (scan.hasNext()) {
                jsonStr = scan.nextLine();
            }
        }
        return jsonStr;
    }
    
    public List<StarWarsDTO> getAllDataInParallelWithQueueAndDTO() throws ProtocolException, IOException, InterruptedException, ExecutionException {
        List<StarWarsDTO> results = new ArrayList();
        List<String> URLS = new ArrayList();
        URLS.add("https://swapi.co/api/people/1/");
        URLS.add("https://swapi.co/api/planets/1/");
        URLS.add("https://swapi.co/api/vehicles/7/");
        URLS.add("https://swapi.co/api/films/1/");
        URLS.add("https://swapi.co/api/starships/2/");

        Queue<Future<JsonObject>> queue = new ArrayBlockingQueue(URLS.size());

        ExecutorService workingJack = Executors.newCachedThreadPool();
        for (String url : URLS) {
            Future<JsonObject> future;
            future = workingJack.submit(() -> {
                JsonObject jsonObject = new JsonParser().parse(getSwappi(url)).getAsJsonObject();
                return jsonObject;
            });
            queue.add(future);
        }
        while (!queue.isEmpty()) {
            Future<JsonObject> cpo = queue.poll();
            if (cpo.isDone()) {
                try {
                    results.add(new StarWarsDTO(
                            cpo.get().get("name").getAsString(),
                            cpo.get().get("url").getAsString()
                    ));

                } catch (InterruptedException interruptedException) {
                    System.out.println("interruptedException: " + interruptedException);
                } catch (ExecutionException executionException) {
                    System.out.println("executionException: " + executionException);
                } catch (NullPointerException ex) {
                    System.out.println("NullPointerException: " + ex);
                }
            } else {
                queue.add(cpo);
            }
        }
        workingJack.shutdown();
        return results;
    }
    
    public List<String> getAllDataInParallelWithQueue() throws ProtocolException, IOException, InterruptedException, ExecutionException {
        List<String> results = new ArrayList();
        List<String> URLS = new ArrayList();
        URLS.add("https://swapi.co/api/people/1/");
        URLS.add("https://swapi.co/api/planets/1/");
        URLS.add("https://swapi.co/api/vehicles/7/");
        URLS.add("https://swapi.co/api/films/1/");
        URLS.add("https://swapi.co/api/starships/2/");

        Queue<Future<JsonObject>> queue = new ArrayBlockingQueue(URLS.size());

        ExecutorService workingJack = Executors.newCachedThreadPool();
        for (String url : URLS) {
            Future<JsonObject> future;
            future = workingJack.submit(() -> {
                JsonObject jsonObject = new JsonParser().parse(getSwappi(url)).getAsJsonObject();
                return jsonObject;
            });
            queue.add(future);
        }
        while (!queue.isEmpty()) {
            Future<JsonObject> cpo = queue.poll();
            if (cpo.isDone()) {
                try {
                    JsonObject json = new JsonObject();
                    json.addProperty("name", cpo.get().get("name").getAsString());
                    json.addProperty("url", cpo.get().get("url").getAsString());
                    results.add(json.toString());
                } catch (InterruptedException interruptedException) {
                    System.out.println("interruptedException: " + interruptedException);
                } catch (ExecutionException executionException) {
                    System.out.println("executionException: " + executionException);
                } catch (NullPointerException ex) {
                    System.out.println("NullPointerException: " + ex);
                }
            } else {
                queue.add(cpo);
            }
        }
        workingJack.shutdown();
        return results;
    }

}
