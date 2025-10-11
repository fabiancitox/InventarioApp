<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="https://cdn-icons-png.freepik.com/256/10334/10334941.png" rel="icon">
    <title>Inventario productos</title>
</head>
<body>
<h2>Productos</h2>

<!-- TODO: mostrar mensaje de error si existe en requestScope -->
<c:if test="${not empty mensajeBean.textoError}">
    <div style="color: red;">
            ${mensajeBean.textoError}
    </div>
</c:if>
<!-- TODO: mostrar mensaje de info si existe en requestScope -->
<c:if test="${not empty mensajeBean.textoInfo}">
    <div style="color: green;">
            ${mensajeBean.textoInfo}
    </div>
</c:if>

<table border="1">
    <thead>
    <tr>
        <th>Código</th>
        <th>Nombre</th>
        <th>Categoría</th>
        <th>Precio</th>
        <th>Stock</th>
        <th>Activo</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="producto" items="${productos}">
        <tr>
            <td>${producto.codigo}</td>
            <td>${producto.nombre}</td>
            <td>${producto.categoria}</td>
            <td>${producto.precio}</td>
            <td>${producto.stock}</td>
            <td>${producto.activo ? 'Si' : 'No'}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h3>Agregar producto</h3>
<form method="post" action="${pageContext.request.contextPath}/productos">
    Código: <input name="codigo"/>
    Nombre: <input name="nombre"/>
    Categoría:
    <select name="categoria">
        <option>Electronicos</option>
        <option>Accesorios</option>
        <option>Muebles</option>
        <option>Ropa</option>
    </select>
    Precio: <input name="precio" type="number" step="0.01"/>
    Stock: <input name="stock" type="number"/>
    Activo: <input name="activo" type="checkbox" checked/>
    <button type="submit">Guardar</button>
</form>
</body>
</html>
