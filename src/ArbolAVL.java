import java.util.ArrayList;
import java.util.List;

public class ArbolAVL<T extends Comparable<T>> {
    private NodoAVL<T> raiz;
    private List<String> registroOperaciones = new ArrayList<>();

    public List<String> getRegistroOperaciones() {
        return registroOperaciones;
    }

    public void limpiarRegistro() {
        registroOperaciones.clear();
    }

    public int obtenerAltura() {
        return altura(raiz);
    }

    private int altura(NodoAVL<T> n) {
        return (n == null) ? 0 : n.altura;
    }

    public int factorEquilibrio(NodoAVL<T> n) {
        return (n == null) ? 0 : altura(n.izquierdo) - altura(n.derecho);
    }

    private void actualizarAltura(NodoAVL<T> n) {
        n.altura = 1 + Math.max(altura(n.izquierdo), altura(n.derecho));
    }

    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.izquierdo;
        NodoAVL<T> T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.derecho;
        NodoAVL<T> T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }


    public void insertar(T dato) {
        raiz = insertarRec(raiz, dato);
    }

    private NodoAVL<T> insertarRec(NodoAVL<T> nodo, T dato) {
        if (nodo == null) return new NodoAVL<>(dato);

        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, dato);
        } else if (cmp > 0) {
            nodo.derecho = insertarRec(nodo.derecho, dato);
        } else {
            return nodo;
        }

        actualizarAltura(nodo);
        return balancear(nodo);
    }

    public void eliminar(T dato) {
        raiz = eliminarRec(raiz, dato);
    }

    private NodoAVL<T> eliminarRec(NodoAVL<T> nodo, T dato) {
        if (nodo == null) return null;

        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, dato);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, dato);
        } else {
            if (nodo.izquierdo == null || nodo.derecho == null) {
                nodo = (nodo.izquierdo != null) ? nodo.izquierdo : nodo.derecho;
            } else {
                NodoAVL<T> sucesor = minimoValor(nodo.derecho);
                nodo.dato = sucesor.dato;
                nodo.derecho = eliminarRec(nodo.derecho, sucesor.dato);
            }
        }

        if (nodo == null) return null;
        actualizarAltura(nodo);
        return balancear(nodo);
    }

    private NodoAVL<T> balancear(NodoAVL<T> nodo) {
        int fe = factorEquilibrio(nodo);

        // Caso Izquierda-Izquierda (LL)
        if (fe > 1 && factorEquilibrio(nodo.izquierdo) >= 0) {
            registroOperaciones.add("Rotación Simple Derecha (LL) en nodo " + nodo.dato);
            return rotacionDerecha(nodo);
        }
        // Caso Izquierda-Derecha (LR)
        if (fe > 1 && factorEquilibrio(nodo.izquierdo) < 0) {
            registroOperaciones.add("Rotación Doble Izquierda-Derecha (LR) en nodo " + nodo.dato);
            nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            return rotacionDerecha(nodo);
        }
        // Caso Derecha-Derecha (RR)
        if (fe < -1 && factorEquilibrio(nodo.derecho) <= 0) {
            registroOperaciones.add("Rotación Simple Izquierda (RR) en nodo " + nodo.dato);
            return rotacionIzquierda(nodo);
        }
        // Caso Derecha-Izquierda (RL)
        if (fe < -1 && factorEquilibrio(nodo.derecho) > 0) {
            registroOperaciones.add("Rotación Doble Derecha-Izquierda (RL) en nodo " + nodo.dato);
            nodo.derecho = rotacionDerecha(nodo.derecho);
            return rotacionIzquierda(nodo);
        }
        return nodo;
    }

    private NodoAVL<T> minimoValor(NodoAVL<T> nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    public int[] buscarConConteo(T dato) {
        NodoAVL<T> actual = raiz;
        int visitados = 0;
        while (actual != null) {
            visitados++;
            int cmp = dato.compareTo(actual.dato);
            if (cmp == 0) return new int[]{1, visitados};
            actual = (cmp < 0) ? actual.izquierdo : actual.derecho;
        }
        return new int[]{0, visitados};
    }


    public List<T> inorden() {
        List<T> res = new ArrayList<>();
        inordenRec(raiz, res);
        return res;
    }
    private void inordenRec(NodoAVL<T> n, List<T> res) {
        if (n != null) { inordenRec(n.izquierdo, res); res.add(n.dato); inordenRec(n.derecho, res); }
    }

    public List<T> preorden() {
        List<T> res = new ArrayList<>();
        preordenRec(raiz, res);
        return res;
    }
    private void preordenRec(NodoAVL<T> n, List<T> res) {
        if (n != null) { res.add(n.dato); preordenRec(n.izquierdo, res); preordenRec(n.derecho, res); }
    }

    public List<T> postorden() {
        List<T> res = new ArrayList<>();
        postordenRec(raiz, res);
        return res;
    }
    private void postordenRec(NodoAVL<T> n, List<T> res) {
        if (n != null) { postordenRec(n.izquierdo, res); postordenRec(n.derecho, res); res.add(n.dato); }
    }

    public void imprimirArbol() {
        imprimirRec(raiz, "", true);
    }
    private void imprimirRec(NodoAVL<T> nodo, String prefijo, boolean esUltimo) {
        if (nodo != null) {
            System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + nodo.dato + " (FE=" + factorEquilibrio(nodo) + ")");
            imprimirRec(nodo.izquierdo, prefijo + (esUltimo ? "    " : "│   "), false);
            imprimirRec(nodo.derecho, prefijo + (esUltimo ? "    " : "│   "), true);
        }
    }

    public boolean verificarPropiedades() {
        return verificarRec(raiz, null, null);
    }

    private boolean verificarRec(NodoAVL<T> nodo, T min, T max) {
        if (nodo == null) return true;
        if ((min != null && nodo.dato.compareTo(min) <= 0) || (max != null && nodo.dato.compareTo(max) >= 0)) {
            return false; // Viola propiedad BST
        }
        int fe = factorEquilibrio(nodo);
        if (fe < -1 || fe > 1) {
            return false; // Viola propiedad AVL
        }
        return verificarRec(nodo.izquierdo, min, nodo.dato) && verificarRec(nodo.derecho, nodo.dato, max);
    }
}