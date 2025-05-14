package cl.cens.receptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class TokenMinsal {
    private static final Logger logger = LoggerFactory.getLogger(TokenMinsal.class);

    public static String getToken(String url, String user, String password) {
        String authString = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        // Crear cliente HTTP
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", authHeader);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // Payload
            StringEntity payload = new StringEntity("grant_type=client_credentials");
            post.setEntity(payload);

            // Ejecutar
            HttpResponse response = client.execute(post);
            String json = EntityUtils.toString(response.getEntity());

            logger.info("Respuesta cruda del servidor (Apache): " + json);

            // Parsear JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(json, Map.class);
            String token = (String) map.get("access_token");

            logger.info("Access token obtenido: " + token);
            return token;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String helloWorld() {
        logger.info("Hola Mundo desde TokenMinsal class");
        return "Hola Mundo desde TokenMinsal class";
    }
}
