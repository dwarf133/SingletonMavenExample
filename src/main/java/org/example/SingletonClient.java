package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SingletonClient {

    private static SingletonClient instance = null;
    private Date expires_at;
    private String token;
    private final String login = System.getenv("API_LOGIN");
    private final String password = System.getenv("API_PASSWORD");
    private final String apiUrl = System.getenv("API_URL");
    private final String loginEndpoint = "login";
    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateFormat dateFormatter = new SimpleDateFormat(SingletonClient.dateFormat);
    public record LoginResponse(String token, String expires_at) {}
    private SingletonClient() throws JsonProcessingException, ParseException {
        LoginResponse response = SingletonClient.login();
        this.token = response.token;
        this.expires_at = SingletonClient.dateFormatter.parse(response.expires_at);
    }

    private static LoginResponse login() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        LoginResponse response = mapper.readValue("{\"token\":\"dfsdfsdfsdfsdfsdfsdgsdfh\", \"expires_at\":\"2024-04-24T00:00:00\"}", LoginResponse.class);

        return response; //new LoginResponse("token", "sdsadas");
    }

    public static SingletonClient getInstance() throws ParseException, JsonProcessingException {
        if (SingletonClient.instance == null ||
                SingletonClient.instance.expires_at.after(Calendar.getInstance().getTime())) {
            SingletonClient.instance = new SingletonClient();
        }
        return SingletonClient.instance;
    }

    public String getExpiresAt() {
        return SingletonClient.dateFormatter.format(this.expires_at);
    }
}
