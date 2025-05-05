### **Documentación Técnica**

#### **Elección de Estructuras de Datos**

1. **Arreglos Estáticos (`asientos`, `PRECIOS`, `RANGOS`)**:

   - **`asientos: boolean[100]`**:

     - **Propósito**: Representar la disponibilidad de los 100 asientos del teatro.
     - **Justificación**:
       - Acceso directo por índice
       - Uso eficiente de memoria
       - Ideal para operaciones rápidas de verificación/actualización.

   - **`PRECIOS: double[]` y `RANGOS: int[][]`**:
     - **Propósito**: Almacenar precios y rangos de asientos por categoría.
     - **Justificación**:
       - Datos fijos que no cambian durante la ejecución.
       - Acceso rápido sin necesidad de estructuras dinámicas.

2. **Listas Dinámicas (`ventasAsiento`, `ventasCliente`, etc.)**:

   - **`ArrayList<Integer>` y `ArrayList<String>`**:
     - **Propósito**: Almacenar datos de ventas de forma dinámica.
     - **Justificación**:
       - Facilita operaciones de agregar/eliminar en tiempo constante

3. **Arreglos Paralelos para Descuentos (`TIPOS_DESCUENTO`, `VALORES_DESCUENTO`)**:
   - **Propósito**: Relacionar tipos de descuento con sus valores porcentuales.
   - **Justificación**:
     - Simplifica la aplicación de descuentos sin usar estructuras complejas.

---

#### **Optimizaciones Realizadas**

1. **Precálculo de Rangos (`RANGOS`)**:
   - **Descripción**: Los rangos de asientos (`VIP: 1-20`, etc.) se almacenan en un arreglo estático.
   - **Beneficio**: Evita recalcular los límites en cada operación, reduciendo la complejidad pero hace al programa poco escalable, la verdad no encontré una mejor solución, queria hacer 3 listas y una lista de listas, pero no pude.
2. **Validación de Entradas con `leerEnteroEnRango`**:

   - **Descripción**: Centraliza la lectura de números enteros dentro de un rango.
   - **Beneficio**: Reduce código duplicado y mejora la robustez ante entradas inválidas.

3. **Uso de `ArrayList` para Ventas**:
   - **Descripción**: Permite eliminar ventas en tiempo constante (`O(1)`) al mover el último elemento a la posición eliminada.
   - **Beneficio**: Evita desplazamientos costosos en arreglos estáticos.

---
