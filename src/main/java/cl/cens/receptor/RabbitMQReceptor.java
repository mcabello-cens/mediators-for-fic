package cl.cens.receptor;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQReceptor {

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
        System.out.println("¡Hola Mundo!");
        return "Hola Mundo desde RabbitMQMediator";
    }
}
