package cl.cens.receptor;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQReceptor {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQReceptor.class);

    public static String leerMensaje(
            String host,
            int port,
            String username,
            String password,
            String queueName,
            boolean durable,
            boolean exclusive,
            boolean autoDelete,
            Map<String, Object> arguments
    ) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declarar la cola (solo si no existe)
            channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);

            // Obtener un mensaje (modo polling)
            GetResponse response = channel.basicGet(queueName, true);
            if (response != null) {
                return new String(response.getBody(), StandardCharsets.UTF_8);
            } else {
                return null;
            }

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String helloWorld() {
        logger.info("Hola Mundo desde RabbitMqReceptor class");
        return "Hola Mundo desde RabbitMqReceptor class";
    }
}
