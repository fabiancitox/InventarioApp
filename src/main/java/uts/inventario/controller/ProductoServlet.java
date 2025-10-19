package uts.inventario.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uts.inventario.facade.ProductoFacade;
import uts.inventario.model.Producto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    @Inject
    private ProductoFacade facade;

    private List<Producto> lista = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            lista = facade.listar();
            req.setAttribute("productos", lista);
            req.getRequestDispatcher("view-productos.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("error", "Error al listar");
            req.getRequestDispatcher("view-productos.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("delete".equals(action)) {
                eliminarProducto(req, res);
            } else if ("edit".equals(action)) {
                String codigo = req.getParameter("codigo");
                Producto producto = facade.buscarPorCodigo(codigo);
                if (producto != null) {
                    req.setAttribute("productos", lista);
                    req.setAttribute("productoEditar", facade.buscarPorCodigo(codigo));
                    req.getRequestDispatcher("view-productos.jsp").forward(req, res);
                }
                req.setAttribute("mensajeBean.textoError", "El codigo no existe.");
                req.getRequestDispatcher("view-productos.jsp").forward(req, res);
            } else if ("update".equals(action)) {
                editarProducto(req, res);
            } else {
                crearProducto(req, res);
            }
        } catch (Exception e) {
            req.setAttribute("productos", lista);
            req.setAttribute("mensajeBean.textoError", "Error interno, intente nuevamente.");
            req.getRequestDispatcher("view-productos.jsp").forward(req, res);
        }
    }

    private void editarProducto(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // Recibo parámetros
        String codigo = req.getParameter("codigo");
        String id = req.getParameter("id");
        String nombre = req.getParameter("nombre");
        String categoria = req.getParameter("categoria");
        double precio = Double.parseDouble(req.getParameter("precio"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        boolean activo = req.getParameter("activo") != null;

        // Creación del objeto producto
        Producto producto = new Producto();
        producto.setId(Integer.parseInt(id));
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setActivo(activo);

        // Guardar producto
        facade.editar(producto);
        lista = facade.listar();
        req.setAttribute("productos", lista);
        req.setAttribute("mensajeBean.textoInfo", "Producto editado correctamente.");
        req.getRequestDispatcher("view-productos.jsp").forward(req, res);
    }

    private void crearProducto(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, Exception {
        // Recibo parámetros
        String codigo = req.getParameter("codigo");
        String nombre = req.getParameter("nombre");
        String categoria = req.getParameter("categoria");
        double precio = Double.parseDouble(req.getParameter("precio"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        boolean activo = req.getParameter("activo") != null;

        // Creación del objeto producto
        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setActivo(activo);

        // Guardar producto
        facade.crear(producto);
        lista = facade.listar();
        req.setAttribute("productos", lista);
        req.setAttribute("mensajeBean.textoInfo", "Producto agregado correctamente.");
        req.getRequestDispatcher("view-productos.jsp").forward(req, res);
    }
    
    private void eliminarProducto(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String codigo = req.getParameter("codigo");

        
        if (codigo == null || codigo.isEmpty()) {
            req.setAttribute("error", "Codigo de producto no valido");
            req.getRequestDispatcher("view-productos.jsp").forward(req, res);
            return;
        }

        try {
            facade.eliminar(codigo);
            lista = facade.listar();
            req.setAttribute("productos", lista);
            req.setAttribute("mensajeBean.textoInfo", "Producto eliminado correctamente.");
        } catch (Exception e) {
            req.setAttribute("productos", lista);
            req.setAttribute("mensajeBean.textoError", "Error al eliminar el producto: " + e.getMessage());
        }

        req.getRequestDispatcher("view-productos.jsp").forward(req, res);
    }

}
