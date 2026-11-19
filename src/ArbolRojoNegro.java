import java.util.ArrayList;
import java.util.List;

public class ArbolRojoNegro<T extends Comparable<T>> {
    private final NodoRN<T> NIL;
        private NodoRN<T> raiz;
        private List<String> registroOperaciones = new ArrayList<>();

        public ArbolRojoNegro() {
            NIL = new NodoRN<>(null, NodoRN.NEGRO);
            raiz = NIL;
        }

        public List<String> getRegistroOperaciones() {
            return registroOperaciones;
        }

        public void limpiarRegistro() {
            registroOperaciones.clear();
        }

        public int obtenerAltura() {
            return alturaRec(raiz);
        }

        private int alturaRec(NodoRN<T> n) {
            if (n == NIL) return 0;
            return 1 + Math.max(alturaRec(n.izquierdo), alturaRec(n.derecho));
        }

        private void rotacionIzquierda(NodoRN<T> x) {
            NodoRN<T> y = x.derecho;
            x.derecho = y.izquierdo;
            if (y.izquierdo != NIL) y.izquierdo.padre = x;
            y.padre = x.padre;
            if (x.padre == null) raiz = y;
            else if (x == x.padre.izquierdo) x.padre.izquierdo = y;
            else x.padre.derecho = y;
            y.izquierdo = x;
            x.padre = y;
        }

        private void rotacionDerecha(NodoRN<T> y) {
            NodoRN<T> x = y.izquierdo;
            y.izquierdo = x.derecho;
            if (x.derecho != NIL) x.derecho.padre = y;
            x.padre = y.padre;
            if (y.padre == null) raiz = x;
            else if (y == y.padre.derecho) y.padre.derecho = x;
            else y.padre.izquierdo = x;
            x.derecho = y;
            y.padre = x;
        }

        public void insertar(T dato) {
            NodoRN<T> nuevo = new NodoRN<>(dato, NodoRN.ROJO);
            nuevo.izquierdo = NIL;
            nuevo.derecho = NIL;

            NodoRN<T> y = null;
            NodoRN<T> x = raiz;

            while (x != NIL) {
                y = x;
                int cmp = nuevo.dato.compareTo(x.dato);
                if (cmp < 0) x = x.izquierdo;
                else if (cmp > 0) x = x.derecho;
                else return; // Ignorar duplicados
            }

            nuevo.padre = y;
            if (y == null) raiz = nuevo;
            else if (nuevo.dato.compareTo(y.dato) < 0) y.izquierdo = nuevo;
            else y.derecho = nuevo;

            if (nuevo.padre == null) {
                nuevo.color = NodoRN.NEGRO;
                return;
            }
            if (nuevo.padre.padre == null) return;

            repararInsercion(nuevo);
        }

        private void repararInsercion(NodoRN<T> k) {
            while (k.padre != null && k.padre.color == NodoRN.ROJO) {
                if (k.padre == k.padre.padre.derecho) {
                    NodoRN<T> tio = k.padre.padre.izquierdo;
                    if (tio.color == NodoRN.ROJO) {
                        registroOperaciones.add("Recoloreación (tío rojo) en " + k.dato);
                        tio.color = NodoRN.NEGRO;
                        k.padre.color = NodoRN.NEGRO;
                        k.padre.padre.color = NodoRN.ROJO;
                        k = k.padre.padre;
                    } else {
                        if (k == k.padre.izquierdo) {
                            registroOperaciones.add("Rotación Derecha en " + k.padre.dato);
                            k = k.padre;
                            rotacionDerecha(k);
                        }
                        registroOperaciones.add("Recoloreación y Rotación Izquierda en " + k.padre.padre.dato);
                        k.padre.color = NodoRN.NEGRO;
                        k.padre.padre.color = NodoRN.ROJO;
                        rotacionIzquierda(k.padre.padre);
                    }
                } else {
                    NodoRN<T> tio = k.padre.padre.derecho;
                    if (tio.color == NodoRN.ROJO) {
                        registroOperaciones.add("Recoloreación (tío rojo) en " + k.dato);
                        tio.color = NodoRN.NEGRO;
                        k.padre.color = NodoRN.NEGRO;
                        k.padre.padre.color = NodoRN.ROJO;
                        k = k.padre.padre;
                    } else {
                        if (k == k.padre.derecho) {
                            registroOperaciones.add("Rotación Izquierda en " + k.padre.dato);
                            k = k.padre;
                            rotacionIzquierda(k);
                        }
                        registroOperaciones.add("Recoloreación y Rotación Derecha en " + k.padre.padre.dato);
                        k.padre.color = NodoRN.NEGRO;
                        k.padre.padre.color = NodoRN.ROJO;
                        rotacionDerecha(k.padre.padre);
                    }
                }
                if (k == raiz) break;
            }
            raiz.color = NodoRN.NEGRO;
        }

        public void eliminar(T dato) {
            NodoRN<T> z = raiz;
            while (z != NIL) {
                int cmp = dato.compareTo(z.dato);
                if (cmp == 0) break;
                z = (cmp < 0) ? z.izquierdo : z.derecho;
            }
            if (z == NIL) return;

            NodoRN<T> y = z;
            NodoRN<T> x;
            boolean colorOriginalY = y.color;

            if (z.izquierdo == NIL) {
                x = z.derecho;
                trasplantar(z, z.derecho);
            } else if (z.derecho == NIL) {
                x = z.izquierdo;
                trasplantar(z, z.izquierdo);
            } else {
                y = minimo(z.derecho);
                colorOriginalY = y.color;
                x = y.derecho;
                if (y.padre == z) {
                    x.padre = y;
                } else {
                    trasplantar(y, y.derecho);
                    y.derecho = z.derecho;
                    y.derecho.padre = y;
                }
                trasplantar(z, y);
                y.izquierdo = z.izquierdo;
                y.izquierdo.padre = y;
                y.color = z.color;
            }

            if (colorOriginalY == NodoRN.NEGRO) {
                repararEliminacion(x);
            }
        }

        private void trasplantar(NodoRN<T> u, NodoRN<T> v) {
            if (u.padre == null) raiz = v;
            else if (u == u.padre.izquierdo) u.padre.izquierdo = v;
            else u.padre.derecho = v;
            v.padre = u.padre;
        }

        private void repararEliminacion(NodoRN<T> x) {
            while (x != raiz && x.color == NodoRN.NEGRO) {
                if (x == x.padre.izquierdo) {
                    NodoRN<T> w = x.padre.derecho;
                    if (w.color == NodoRN.ROJO) {
                        registroOperaciones.add("Eliminación Caso 1: Recolorear y Rotación Izquierda en " + x.padre.dato);
                        w.color = NodoRN.NEGRO;
                        x.padre.color = NodoRN.ROJO;
                        rotacionIzquierda(x.padre);
                        w = x.padre.derecho;
                    }
                    if (w.izquierdo.color == NodoRN.NEGRO && w.derecho.color == NodoRN.NEGRO) {
                        registroOperaciones.add("Eliminación Caso 2: Recolorear hermano " + w.dato + " a Rojo");
                        w.color = NodoRN.ROJO;
                        x = x.padre;
                    } else {
                        if (w.derecho.color == NodoRN.NEGRO) {
                            registroOperaciones.add("Eliminación Caso 3: Rotación Derecha en " + w.dato);
                            w.izquierdo.color = NodoRN.NEGRO;
                            w.color = NodoRN.ROJO;
                            rotacionDerecha(w);
                            w = x.padre.derecho;
                        }
                        registroOperaciones.add("Eliminación Caso 4: Recolorear y Rotación Izquierda en " + x.padre.dato);
                        w.color = x.padre.color;
                        x.padre.color = NodoRN.NEGRO;
                        w.derecho.color = NodoRN.NEGRO;
                        rotacionIzquierda(x.padre);
                        x = raiz;
                    }
                } else {
                    NodoRN<T> w = x.padre.izquierdo;
                    if (w.color == NodoRN.ROJO) {
                        registroOperaciones.add("Eliminación Caso 1: Recolorear y Rotación Derecha en " + x.padre.dato);
                        w.color = NodoRN.NEGRO;
                        x.padre.color = NodoRN.ROJO;
                        rotacionDerecha(x.padre);
                        w = x.padre.izquierdo;
                    }
                    if (w.derecho.color == NodoRN.NEGRO && w.izquierdo.color == NodoRN.NEGRO) {
                        registroOperaciones.add("Eliminación Caso 2: Recolorear hermano " + w.dato + " a Rojo");
                        w.color = NodoRN.ROJO;
                        x = x.padre;
                    } else {
                        if (w.izquierdo.color == NodoRN.NEGRO) {
                            registroOperaciones.add("Eliminación Caso 3: Rotación Izquierda en " + w.dato);
                            w.derecho.color = NodoRN.NEGRO;
                            w.color = NodoRN.ROJO;
                            rotacionIzquierda(w);
                            w = x.padre.izquierdo;
                        }
                        registroOperaciones.add("Eliminación Caso 4: Recolorear y Rotación Derecha en " + x.padre.dato);
                        w.color = x.padre.color;
                        x.padre.color = NodoRN.NEGRO;
                        w.izquierdo.color = NodoRN.NEGRO;
                        rotacionDerecha(x.padre);
                        x = raiz;
                    }
                }
            }
            x.color = NodoRN.NEGRO;
        }

        private NodoRN<T> minimo(NodoRN<T> nodo) {
            while (nodo.izquierdo != NIL) nodo = nodo.izquierdo;
            return nodo;
        }

        public int[] buscarConConteo(T dato) {
            NodoRN<T> actual = raiz;
            int visitados = 0;
            while (actual != NIL) {
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
        private void inordenRec(NodoRN<T> n, List<T> res) {
            if (n != NIL) { inordenRec(n.izquierdo, res); res.add(n.dato); inordenRec(n.derecho, res); }
        }

        public List<T> preorden() {
            List<T> res = new ArrayList<>();
            preordenRec(raiz, res);
            return res;
        }
        private void preordenRec(NodoRN<T> n, List<T> res) {
            if (n != NIL) { res.add(n.dato); preordenRec(n.izquierdo, res); preordenRec(n.derecho, res); }
        }

        public List<T> postorden() {
            List<T> res = new ArrayList<>();
            postordenRec(raiz, res);
            return res;
        }
        private void postordenRec(NodoRN<T> n, List<T> res) {
            if (n != NIL) { postordenRec(n.izquierdo, res); postordenRec(n.derecho, res); res.add(n.dato); }
        }

        public void imprimirArbol() {
            imprimirRec(raiz, "", true);
        }
        private void imprimirRec(NodoRN<T> nodo, String prefijo, boolean esUltimo) {
            if (nodo != NIL) {
                String c = (nodo.color == NodoRN.ROJO) ? "R" : "N";
                System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + nodo.dato + " [" + c + "]");
                imprimirRec(nodo.izquierdo, prefijo + (esUltimo ? "    " : "│   "), false);
                imprimirRec(nodo.derecho, prefijo + (esUltimo ? "    " : "│   "), true);
            }
        }


        public boolean verificarPropiedades() {
            if (raiz != NIL && raiz.color != NodoRN.NEGRO) return false; // Raíz debe ser negra
            return verificarRec(raiz, null, null) != -1;
        }

        private int verificarRec(NodoRN<T> nodo, T min, T max) {
            if (nodo == NIL) return 1;
            if ((min != null && nodo.dato.compareTo(min) <= 0) || (max != null && nodo.dato.compareTo(max) >= 0)) {
                return -1; // Viola BST
            }
            if (nodo.color == NodoRN.ROJO) {
                if (nodo.izquierdo.color == NodoRN.ROJO || nodo.derecho.color == NodoRN.ROJO) {
                    return -1;
                }
            }
            int bhIzq = verificarRec(nodo.izquierdo, min, nodo.dato);
            int bhDer = verificarRec(nodo.derecho, nodo.dato, max);
            if (bhIzq == -1 || bhDer == -1 || bhIzq != bhDer) {
                return -1;
            }
            return bhIzq + (nodo.color == NodoRN.NEGRO ? 1 : 0);
        }
    }
