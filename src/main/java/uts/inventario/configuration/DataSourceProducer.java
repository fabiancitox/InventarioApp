package uts.inventario.configuration;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>Aquí están definidos todos los métodos necesarios para conectarse a la base de datos.</p>
 */
@ApplicationScoped
public class DataSourceProducer {
    /**
     * Recurso JNDI del pool de conexiones configurado en el servidor.
     */
    @Resource(lookup = "jdbc/inventarioPool")
    private DataSource dataSource;

    /**
     * Produce una instancia del {@link DataSource} para su inyección.
     *
     * <p>La anotación {@code  @Produces} permite decirle al CDI Cuando alguien
     * pida un objeto de este tipo, créalo usando este método. </p>
     *
     * @return el {@link DataSource} configurado en el servidor.
     */
    @Produces
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Produce una {@link Connection} por cada solicitud HTTP.
     *
     * <p>La anotación {@code  @Produces} permite decirle al CDI Cuando alguien
     * * pida un objeto de este tipo, créalo usando este método. </p>
     *
     * <p>El contenedor CDI invoca este método cuando un bean requiere una conexión
     * mediante {@code @Inject Connection con;}.
     *
     * @return una conexión obtenida del pool configurado.
     * @throws SQLException si ocurre un error al obtener la conexión.
     */
    @Produces
    @RequestScoped
    public Connection getConnection() throws SQLException {
        System.out.println("Creando la conexión");
        return dataSource.getConnection();
    }

    /**
     * Cierra (devuelve al pool) la conexión inyectada al finalizar la request.
     *
     * <p>El contenedor CDI invoca este método {@code @Disposes} automáticamente para liberar los recursos
     * una vez que termina el ciclo de vida de la conexión {@code @RequestScoped}.
     *
     * @param con la conexión a cerrar.
     * @throws SQLException si ocurre un error al cerrar la conexión.
     */
    public void close(@Disposes Connection con) throws SQLException {
        if (con != null && !con.isClosed()) {
            System.out.println("Cerrando conexión");
            con.close(); // Devuelve al pool
        }
    }
}
