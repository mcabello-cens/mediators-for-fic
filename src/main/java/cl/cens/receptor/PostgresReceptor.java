package cl.cens.receptor;

import cl.cens.receptor.repository.BundleRepository;
import cl.cens.receptor.util.DataSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class PostgresReceptor {
    private static final Logger logger = LoggerFactory.getLogger(PostgresReceptor.class);

    private static BundleRepository repository;
        public static void init(String url, String user, String password) {
        DataSource ds = DataSourceProvider.getDataSource(url, user, password);
        repository = new BundleRepository(ds);
    }

    public static void insertData(boolean envioCorrecto, String responseResource) {
        repository.insert(envioCorrecto, responseResource);
    }

    public static void updateData(int idTupla, boolean envioCorrecto, String responseResource) {
        repository.update(idTupla, envioCorrecto, responseResource);
    }

    public static String helloWorld() {
            logger.info("Hola Mundo desde PostgresReceptor class");
            return "Hola Mundo desde PostgresReceptor class";
    }
}
