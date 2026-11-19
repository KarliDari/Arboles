# Implementación de Árboles Balanceados (AVL y Rojo-Negro) en Java

Proyecto para la materia de **Estructura de Datos** que implementa desde cero un **Árbol AVL** y un **Árbol Rojo-Negro** con operaciones de inserción, eliminación, búsqueda, recorridos y validación automática de propiedades.

## Estructura del proyecto
* `src/NodoAVL.java` y `src/ArbolAVL.java`: Implementación del árbol AVL con rotaciones simples y dobles.
* `src/NodoRN.java` y `src/ArbolRojoNegro.java`: Implementación del árbol Rojo-Negro con recoloreaciones y rotaciones.
* `src/MainPruebas.java`: Clase principal con las pruebas de inserción, eliminación, búsqueda y *benchmark* experimental.

## Cómo compilar y ejecutar
Desde la carpeta raíz del proyecto en una terminal:
```bash
javac src/*.java -d out
java -cp out MainPruebas