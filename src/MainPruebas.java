import java.util.Random;

public class MainPruebas {
    public static void main(String[] args) {
        ArbolAVL<Integer> avl = new ArbolAVL<>();
        ArbolRojoNegro<Integer> rn = new ArbolRojoNegro<>();

        int[] secuenciaInsercion = {30, 20, 10, 25, 40, 50, 22, 5, 35, 45};

        System.out.println("=== PRUEBA 1: INSERCIÓN DE ELEMENTOS ===");
        for (int val : secuenciaInsercion) {
            avl.insertar(val);
            rn.insertar(val);
        }

        System.out.println("\n--- Árbol AVL Resultante (Altura: " + avl.obtenerAltura() + ") ---");
        avl.imprimirArbol();
        System.out.println("Operaciones AVL: " + avl.getRegistroOperaciones());
        System.out.println("Inorden AVL: " + avl.inorden());
        System.out.println("Propiedades AVL válidas: " + avl.verificarPropiedades());

        System.out.println("\n--- Árbol Rojo-Negro Resultante (Altura: " + rn.obtenerAltura() + ") ---");
        rn.imprimirArbol();
        System.out.println("Operaciones RN: " + rn.getRegistroOperaciones());
        System.out.println("Inorden RN: " + rn.inorden());
        System.out.println("Propiedades RN válidas: " + rn.verificarPropiedades());

        System.out.println("\n=== PRUEBA 2: ELIMINACIÓN DE ELEMENTOS ===");
        int[] secuenciaEliminacion = {10, 40, 30};
        for (int val : secuenciaEliminacion) {
            avl.limpiarRegistro();
            rn.limpiarRegistro();
            avl.eliminar(val);
            rn.eliminar(val);

            System.out.println("\n>> Tras eliminar " + val + ":");
            System.out.println("[AVL] Operaciones: " + avl.getRegistroOperaciones() + " | Válido: " + avl.verificarPropiedades());
            avl.imprimirArbol();
            System.out.println("[RN] Operaciones: " + rn.getRegistroOperaciones() + " | Válido: " + rn.verificarPropiedades());
            rn.imprimirArbol();
        }

        System.out.println("\n=== PRUEBA 3: BÚSQUEDA DE ELEMENTOS ===");
        int[] busquedas = {5, 22, 35, 100};
        for (int b : busquedas) {
            int[] resAvl = avl.buscarConConteo(b);
            int[] resRn = rn.buscarConConteo(b);
            System.out.printf("Buscar %3d -> AVL: %s (%d nodos visitados) | RN: %s (%d nodos visitados)%n",
                    b, resAvl[0] == 1 ? "Encontrado" : "No encontrado", resAvl[1],
                    resRn[0] == 1 ? "Encontrado" : "No encontrado", resRn[1]);
        }

        System.out.println("\n=== BENCHMARK EXPERIMENTAL (N = 100,000 elementos) ===");
        ejecutarBenchmark(100_000);
    }

    private static void ejecutarBenchmark(int n) {
        ArbolAVL<Integer> avl = new ArbolAVL<>();
        ArbolRojoNegro<Integer> rn = new ArbolRojoNegro<>();
        int[] datos = new Random(42).ints(n, 0, n * 10).toArray();

        long t0 = System.nanoTime();
        for (int d : datos) avl.insertar(d);
        long tInsAvl = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int d : datos) rn.insertar(d);
        long tInsRn = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int d : datos) avl.buscarConConteo(d);
        long tBusAvl = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int d : datos) rn.buscarConConteo(d);
        long tBusRn = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int i = 0; i < n / 2; i++) avl.eliminar(datos[i]);
        long tEliAvl = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int i = 0; i < n / 2; i++) rn.eliminar(datos[i]);
        long tEliRn = System.nanoTime() - t0;

        System.out.printf("Altura final (N=%d) -> AVL: %d | Rojo-Negro: %d%n", n, avl.obtenerAltura(), rn.obtenerAltura());
        System.out.printf("Inserción (ms) -> AVL: %.2f | RN: %.2f%n", tInsAvl / 1e6, tInsRn / 1e6);
        System.out.printf("Búsqueda  (ms) -> AVL: %.2f | RN: %.2f%n", tBusAvl / 1e6, tBusRn / 1e6);
        System.out.printf("Eliminación (ms) -> AVL: %.2f | RN: %.2f%n", tEliAvl / 1e6, tEliRn / 1e6);
    }
}
