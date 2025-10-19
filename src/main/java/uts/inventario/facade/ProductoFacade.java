package uts.inventario.facade;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import uts.inventario.model.Producto;
import uts.inventario.persistence.ProductoDAO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


@Named("productoFacade")
@ApplicationScoped
public class ProductoFacade {

    @Inject
    private ValidadorProducto validadorProducto;

    @Inject
    private MensajeBean mensajeBean;

    @Inject
    private ProductoDAO dao;

    private void validar(Producto p) throws Exception {
        validadorProducto.validarCodigo(p.getCodigo());
        validadorProducto.validarNombre(p.getNombre());
        validadorProducto.validarPrecio(p.getPrecio());
        validadorProducto.validarStock(p.getStock());
    }

    public List<Producto> listar() throws Exception {
        try {
            return dao.listar();
        } catch (SQLException e) {
            mensajeBean.setTextoError("Error al listar productos");
            return Collections.emptyList();
        }
    }

    public void crear(Producto p) throws Exception {
        try {
            validar(p);
        } catch (Exception e) {
            mensajeBean.setTextoError(e.getMessage());
            return;
        }

        try {
            //Verificar que el codigo de producto no este duplicado
            if (dao.existePorCodigo(p.getCodigo())) {
                mensajeBean.setTextoError("Codigo ya registrado");
                return;
            }
            //Si el codigo no esta registrado inserta
            dao.insertar(p);
            mensajeBean.setTextoInfo("Producto creado con éxito");
        } catch (SQLException e) {
            mensajeBean.setTextoError("Error al insertar el producto en la base de datos");
        }
    }

    public void eliminar(String codigo) throws Exception {
    try {
        if (codigo == null || codigo.isEmpty()) {
            mensajeBean.setTextoError("Código de producto no válido");
            return;
        }

        if (!dao.existePorCodigo(codigo)) {
            mensajeBean.setTextoError("No existe un producto con el código especificado");
            return;
        }

        
        dao.eliminarPorId(dao.buscarPorCodigo(codigo).get().getId());
        mensajeBean.setTextoInfo("Producto eliminado correctamente");
    } catch (SQLException e) {
        mensajeBean.setTextoError("Error al eliminar el producto de la base de datos");
        throw e;
    }
}

    public void editar(Producto p) throws Exception {
        try {
            validar(p);
        } catch (Exception e) {
            mensajeBean.setTextoError(e.getMessage());
            return;
        }
        try {
            //Editar el producto
            dao.editar(p);
            mensajeBean.setTextoInfo("Producto editado con éxito");
        } catch (SQLException e) {
            mensajeBean.setTextoError("Error al insertar el producto en la base de datos");
        }

    }

    public Producto buscarPorCodigo(String codigo) throws Exception {
        try {
            return dao.buscarPorCodigo(codigo).orElse(null);
        } catch (SQLException e) {
            mensajeBean.setTextoError("Error al buscar el producto en la base de datos");
            return null;
        }
    }

}
