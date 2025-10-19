# 🧮 Proyecto Inventario (Jakarta EE - CDI)

## 📋 Resumen de investigación

Este proyecto implementa un sistema de inventario básico utilizando Jakarta EE con el modelo CDI (Contexts and Dependency Injection).  
El objetivo es aplicar los diferentes **alcances (scopes)**, inyección de dependencias y el patrón **Producer** para manejar recursos como conexiones a base de datos.

El sistema permite:
- Registrar, listar, editar y eliminar productos.
- Validar los datos antes de persistirlos.
- Gestionar mensajes de éxito o error por solicitud.

---

## ⚙️ Scopes utilizados y su función en el flujo

| Scope | Anotación | Clase(s) donde se usa | Propósito |
|--------|------------|-----------------------|------------|
| **ApplicationScoped** | `@ApplicationScoped` | `ProductoFacade`, `ProductoDAO`, `DataSourceProducer` | Mantiene una sola instancia durante toda la vida de la aplicación. Ideal para servicios globales como el acceso a BD o lógica de negocio. |
| **RequestScoped** | `@RequestScoped` | `MensajeBean`, `Connection` (desde `DataSourceProducer`) | Dura solo durante una petición HTTP. Útil para objetos como mensajes o conexiones temporales. |
| **Dependent** | `@Dependent` | `ValidadorProducto` | Instanciado cada vez que se inyecta. Perfecto para validaciones ligeras. |
| **SessionScoped** | *(No implementado en esta versión)* | — | Podría usarse para mantener el usuario logueado o preferencias. |

---

## 🧠 Flujo de “Crear producto”

1. El usuario envía el formulario desde `view-productos.jsp`.
2. `ProductoServlet` recibe la solicitud y crea un objeto `Producto`.
3. El servlet llama a `ProductoFacade.crear(producto)`.
4. `ProductoFacade` valida los campos usando `ValidadorProducto` (`@Dependent`).
5. Si la validación pasa, delega en `ProductoDAO.insertar()` para persistir.
6. `ProductoDAO` utiliza una `Connection` inyectada por el **Producer** (DataSourceProducer).
7. Al finalizar la solicitud, el CDI ejecuta el método `close(@Disposes Connection con)` y libera el recurso.
8. `MensajeBean` (RequestScoped) envía el mensaje de confirmación.

---

## 🔌 Producer funcional

El Producer `DataSourceProducer` administra de forma centralizada el acceso a la base de datos:

```java
@ApplicationScoped
public class DataSourceProducer {

    @Resource(lookup = "jdbc/inventarioPool")
    private DataSource dataSource;

    @Produces
    @RequestScoped
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close(@Disposes Connection con) throws SQLException {
        if (con != null && !con.isClosed()) con.close();
    }
}
```

---

## 🧩 Capturas de funcionamiento

Ubicadas en `docs/capturas/`:

- `vista_listado.png` → Página principal de productos.
- `vista_creacion.png` → Formulario de nuevo producto.
- `vista_edicion.png` → Edición de producto existente.

---



## 📚 Créditos
Proyecto desarrollado con fines académicos  
**Jair Fabián Duarte Villamizar – UTS**  
*Derechos Reservados de Autor: jaifduarte@uts.edu.co*
