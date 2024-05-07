package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

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

        // String encodedResponse = this.client.call( something )
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse response = mapper.readValue(SingletonClient.getTestData(Calendar.getInstance().getTime()), LoginResponse.class);

        return response;
    }

    public static SingletonClient getInstance() throws ParseException, JsonProcessingException {
        if (SingletonClient.instance == null ||
                SingletonClient.instance.expires_at.before(Calendar.getInstance().getTime())) {
            SingletonClient.instance = new SingletonClient();
        }
        return SingletonClient.instance;
    }

    public String getExpiresAt() {
        return SingletonClient.dateFormatter.format(this.expires_at);
    }
    public String getInfoString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Token: ");
        sb.append(this.token);
        sb.append(" Expires at: ");
        sb.append(this.getExpiresAt());
        return sb.toString();
    }

    private static String getTestData(Date date) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int suff = random.nextInt();
        String token = "ZnVja2loYXRlamF2YWZ1Y2s=" + Integer.toString(suff);
        sb.append("{\"token\":\"");
        sb.append(token);
        sb.append("\", \"expires_at\":\"");

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 5);
        date = calendar.getTime();

        sb.append(SingletonClient.dateFormatter.format(date));
        sb.append("\"}");
        return sb.toString();
    }
    public static void testSingletonClient(int iterations) throws ParseException, JsonProcessingException, InterruptedException {
        System.out.println(SingletonClient.getInstance().getInfoString());
        for (int i=0; i<iterations; i++) {
            System.out.println(SingletonClient.getInstance().getInfoString());
            Thread.sleep(1000);
        }

    }
}
