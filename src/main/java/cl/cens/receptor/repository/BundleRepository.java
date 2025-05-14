package cl.cens.receptor.repository;

import cl.cens.receptor.FhirReceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BundleRepository {
    private static final Logger logger = LoggerFactory.getLogger(BundleRepository.class);
    private final DataSource dataSource;

    public BundleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(boolean envioCorrecto, String responseResource) {
        String sql = "INSERT INTO public.bundle_priorizar (envio_correcto, response_resource) VALUES (?, ?::jsonb)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, envioCorrecto);
            stmt.setString(2, responseResource);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al insertar en bundle");
            throw new RuntimeException("Error al insertar en bundle", e);
        }
    }

    public void update(int idTupla, boolean envioCorrecto, String responseResource) {
        String sql = "UPDATE bundle_priorizar SET envio_correcto = ?, response_resource = ?::jsonb WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, envioCorrecto);
            stmt.setString(2, responseResource);
            stmt.setInt(3, idTupla);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al actualizar en bundle");
            throw new RuntimeException("Error al actualizar en bundle", e);
        }
    }
}
