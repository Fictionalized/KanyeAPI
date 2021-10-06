package me.fictionalized.kanye.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Cleanup;
import lombok.SneakyThrows;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

public class Request {

    public static final Gson GSON = new Gson();
    public static final Type TYPE_TOKEN = new TypeToken<Map<String, String>>() {}.getType();

    public final static String ENDPOINT_URL = "https://api.kanye.rest/";

    @SneakyThrows
    public String getQuote() {
        final URL url = new URL(ENDPOINT_URL);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.addRequestProperty("Content-Type", "applications/json");
        connection.addRequestProperty("User-Agent", "KanyeAPI");

        connection.setDoOutput(true);
        connection.setRequestMethod("GET");

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.setInstanceFollowRedirects(false);

        @Cleanup
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        final StringBuilder content = new StringBuilder();

        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null) {
            content.append(inputLine);
        }

        Map<String, String> map = GSON.fromJson(content.toString(), TYPE_TOKEN);
        return map.getOrDefault("quote", "No quote given");
    }

}
