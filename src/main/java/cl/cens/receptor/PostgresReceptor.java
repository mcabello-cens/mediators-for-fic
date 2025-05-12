package cl.cens.receptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresReceptor {

    public static void insertData(String url, String user, String password, boolean envioCorrecto, String responseResource) throws Exception {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO public.bundle_priorizar (envio_correcto, response_resource) VALUES (?, ?::jsonb)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setBoolean(1, envioCorrecto);
                stmt.setString(2, responseResource);
                stmt.executeUpdate();
                System.out.println("Datos insertados correctamente.");
            }
        } catch (Exception e) {
            System.err.println("Error al insertar datos: " + e.getMessage());
            throw e;
        }
    }


    public static void updateData(String url, String user, String password, int idTupla, boolean envioCorrecto, String responseResource) throws Exception {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE bundle_priorizar SET envio_correcto = ?, response_resource = ?::jsonb WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setBoolean(1, envioCorrecto);
                stmt.setString(2, responseResource);
                stmt.setInt(3, idTupla);
                int filas = stmt.executeUpdate();
                System.out.println("Datos actualizados. Filas afectadas: " + filas);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar datos: " + e.getMessage());
            throw e;
        }
    }

    public static void testConnection(String host, String port, String user, String password, String database) {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

        try {
            // Cargar el driver (opcional desde Java 6+)
            Class.forName("org.postgresql.Driver");

            // Intentar la conexión
            Connection conn = DriverManager.getConnection(url, user, password);

            System.out.println("¡Conexión exitosa a PostgreSQL!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver JDBC de PostgreSQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar a PostgreSQL:");
            e.printStackTrace();
        }
    }

    public static String helloWorld() {
        System.out.println("¡Hola Mundo!");
        return "Hola Mundo desde PostgresMQMediator";
    }
}
