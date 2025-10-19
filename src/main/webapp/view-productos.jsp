<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Inventario de Productos</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
        <link rel="icon" href="https://cdn-icons-png.freepik.com/256/10334/10334941.png">
    </head>
    <body class="bg-gray-50 text-gray-800 min-h-screen flex flex-col items-center py-10">

        <!-- Encabezado -->
        <header class="mb-10 text-center">
            <h1 class="text-4xl font-bold text-gray-900 mb-2">Inventario de Productos</h1>
            <p class="text-gray-500 text-sm">Gestión eficiente y moderna de tus productos</p>
        </header>

        <!-- Mensajes de sistema -->
        <div class="w-11/12 md:w-3/4 lg:w-2/3 mb-6">
            <c:if test="${not empty mensajeBean.textoError}">
                <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded-md shadow-sm mb-4">
                    <p>${mensajeBean.textoError}</p>
                </div>
            </c:if>
            <c:if test="${not empty mensajeBean.textoInfo}">
                <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 rounded-md shadow-sm mb-4">
                    <p>${mensajeBean.textoInfo}</p>
                </div>
            </c:if>
        </div>

        <!-- Tabla de productos -->
        <section class="w-11/12 md:w-3/4 lg:w-2/3 bg-white rounded-2xl shadow-lg overflow-hidden mb-10">
            <table class="min-w-full border-collapse">
                <thead class="bg-gray-100 border-b">
                    <tr>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Código</th>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Nombre</th>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Categoría</th>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Precio</th>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Stock</th>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Activo</th>
                        <th class="text-left py-3 px-5 text-gray-600 font-semibold">Accion</th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="producto" items="${productos}">
                        <tr class="hover:bg-gray-50 border-b transition duration-200">
                            <td class="py-3 px-5">${producto.codigo}</td>
                            <td class="py-3 px-5 font-medium">${producto.nombre}</td>
                            <td class="py-3 px-5">${producto.categoria}</td>
                            <td class="py-3 px-5">$${producto.precio}</td>
                            <td class="py-3 px-5">${producto.stock}</td>
                            <td class="py-3 px-5">
                                <span class="px-3 py-1 text-xs rounded-full ${producto.activo ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}">
                                    ${producto.activo ? 'Sí' : 'No'}
                                </span>
                            </td>
                            <td class="py-3 px-5">
                                <form action="${pageContext.request.contextPath}/productos" method="post" onsubmit="return confirm('¿Deseas eliminar este producto?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="codigo" value="${producto.codigo}">
                                    <button type="submit" class="text-red-600 hover:text-red-800 font-medium">Eliminar</button>
                                </form>

                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>

        <!-- Formulario -->
        <section class="w-11/12 md:w-3/4 lg:w-2/3 bg-white rounded-2xl shadow-lg p-8">
            <h2 class="text-2xl font-semibold mb-6 text-gray-900">Agregar Producto</h2>
            <form method="post" action="${pageContext.request.contextPath}/productos" class="grid grid-cols-1 md:grid-cols-2 gap-6">

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Código</label>
                    <input name="codigo" class="w-full border border-gray-300 rounded-lg p-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
                    <input name="nombre" class="w-full border border-gray-300 rounded-lg p-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Categoría</label>
                    <select name="categoria" class="w-full border border-gray-300 rounded-lg p-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                        <option>Electronicos</option>
                        <option>Accesorios</option>
                        <option>Muebles</option>
                        <option>Ropa</option>
                    </select>
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Precio</label>
                    <input name="precio" type="number" step="0.01" class="w-full border border-gray-300 rounded-lg p-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Stock</label>
                    <input name="stock" type="number" class="w-full border border-gray-300 rounded-lg p-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
                </div>

                <div class="flex items-center gap-2 mt-6">
                    <input name="activo" type="checkbox" checked class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500">
                    <label class="text-sm text-gray-700">Activo</label>
                </div>

                <div class="md:col-span-2 flex justify-end mt-4">
                    <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-medium transition duration-200 shadow-sm">
                        Guardar
                    </button>
                </div>
            </form>
        </section>

    </body>
</html>
