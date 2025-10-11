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
        try {
            //Recibo parámetros
            String codigo = req.getParameter("codigo");
            String nombre = req.getParameter("nombre");
            String categoria = req.getParameter("categoria");
            double precio = Double.parseDouble(req.getParameter("precio"));
            int stock = Integer.parseInt(req.getParameter("stock"));
            boolean activo = req.getParameter("activo") != null;

            //creación del objeto producto
            Producto producto = new Producto();
            producto.setCodigo(codigo);
            producto.setNombre(nombre);
            producto.setCategoria(categoria);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setActivo(activo);

            facade.crear(producto); //Creación del producto
            lista = facade.listar(); //Consultar la base de datos
            req.setAttribute("productos", lista);
            req.getRequestDispatcher("view-productos.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("productos", lista);
            req.getRequestDispatcher("view-productos.jsp").forward(req, res);
        }

    }

}
