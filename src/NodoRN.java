 public class NodoRN<T extends Comparable<T>> {
        public static final boolean ROJO = true;
        public static final boolean NEGRO = false;

        T dato;
        boolean color;
        NodoRN<T> izquierdo, derecho, padre;

        public NodoRN(T dato, boolean color) {
            this.dato = dato;
            this.color = color;
        }
    }

