# 🧮 Proyecto Inventario (Jakarta EE - CDI)

### ¿Qué es un Managed Bean y en qué se diferencia de un JavaBean?
Un **Managed Bean** es una clase Java controlada por el contenedor de la aplicación (como Jakarta EE o JavaServer Faces). Esto significa que su ciclo de vida, su instanciación y la inyección de dependencias son administradas automáticamente por el contenedor a través del uso de anotaciones como `@Named`, `@Inject` o los distintos *scopes* (alcances) disponibles.


Por otro lado, un **JavaBean** es simplemente una clase Java que sigue ciertas convenciones: debe tener un constructor público sin parámetros, propiedades privadas con sus respectivos métodos *getter* y *setter*, y debe ser serializable. Sin embargo, un JavaBean **no es gestionado automáticamente por el contenedor**, por lo que su creación y control deben hacerse manualmente dentro del código.


**En resumen:**
- Un *JavaBean* es un objeto simple con propiedades y métodos de acceso.
- Un *Managed Bean* es un *JavaBean* mejorado, gestionado por el contenedor mediante anotaciones, lo que permite aprovechar la inyección de dependencias y los diferentes ciclos de vida.

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

Ubicadas en el documento word 


---

## 📚 Créditos
Proyecto desarrollado con fines académicos  
**Jair Fabian Duarte Villamizar - deiby Fabian Prada Quintero - Armando Rafael Trespalacios – UTS**
