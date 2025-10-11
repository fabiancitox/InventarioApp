package uts.inventario.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import uts.inventario.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SqlNoDataSourceInspection")
@Named("productoDao")
@ApplicationScoped
public class ProductoDAO {

    /**
     * <p>Esta es la instancia inyectada desde el {@link uts.inventario.configuration.DataSourceProducer}</p>
     */
    @Inject
    private Connection con;

    /**
     * Obtiene la lista completa de productos almacenados en la base de datos.
     *
     * <p>Este método ejecuta una consulta SQL que selecciona todos los registros
     * de la tabla <strong>productos</strong>. Por cada fila del resultado, se crea
     * un objeto {@link Producto} que se añade a una lista.</p>
     *
     * @return una lista con todos los productos encontrados; si no existen registros, retorna una lista vacía.
     * @throws SQLException si ocurre un error al comunicarse con la base de datos.
     */
    public List<Producto> listar() throws SQLException {
        //Crear lista de prodcutos
        List<Producto> productos = new ArrayList<>();
        //Consulta a realizar
        String sql = "SELECT * FROM productos";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getBoolean("activo")
                );
                productos.add(producto);
            }
        }
        return productos;
    }

    /**
     * Busca un producto por su código.
     *
     * @param codigo el código del producto a buscar
     * @return un Optional que contiene el producto si se encuentra, o vacío si no existe
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    public Optional<Producto> buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM productos WHERE codigo = ? LIMIT 1";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, codigo.trim());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getBoolean("activo")
                    );
                    return Optional.of(producto);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Verifica si existe un producto con el código indicado.
     *
     * @param codigo código del producto a buscar
     * @return {@code true} si el producto existe, {@code false} en caso contrario
     * @throws SQLException si ocurre un error al ejecutar la consulta
     */
    public Boolean existePorCodigo(String codigo) throws SQLException {
        String sql = "SELECT EXISTS(SELECT 1 FROM productos WHERE codigo = ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); // Siempre devuelve una fila
                return rs.getBoolean(1); // Primera columna (índice 1) contiene true o false
            }
        }
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param p el producto que se desea registrar
     * @throws SQLException si ocurre un error al ejecutar la inserción
     */
    public void insertar(Producto p) throws SQLException {
        //Consulta a realizar
        String sql = "INSERT INTO productos(codigo, nombre, categoria, precio, stock, activo) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            //Insertar datos a la consulta
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setBoolean(6, p.getActivo());
            //Ejecutar consultas
            ps.executeUpdate();
        }
    }

    /**
     * Elimina un producto de la base de datos según su ID.
     *
     * <p>Ejecuta una sentencia SQL que elimina el registro correspondiente
     * al campo <strong>id</strong> en la tabla <strong>productos</strong>.
     * Si no existe un producto con el ID indicado, la operación no afecta ninguna fila.
     *
     * @param id identificador del producto a eliminar.
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL.
     */
    public void eliminarPorId(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
