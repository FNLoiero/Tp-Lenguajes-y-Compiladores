package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.model.NodoArbol;

public class IntermediateCodeGenerator implements FileGenerator {

    private static NodoArbol arbolSintactico;

    public static void setArbolSintactico(NodoArbol arbol) {
        arbolSintactico = arbol;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        if (arbolSintactico != null) {
            fileWriter.write("ÁRBOL SINTÁCTICO GENERADO:\n");
            fileWriter.write("==========================\n\n");
            //imprimirArbolVisual(fileWriter, arbolSintactico, 0);                       
            imprimirArbolLineal(fileWriter, arbolSintactico);
        } else {
            fileWriter.write("No se generó árbol sintáctico.");
        }
    }


    ///Mantener método para poder hacer debugging de manera visual.
    ///Sustituir en el método Generate para que se imprima de esta manera
    /// NO ES LA FORMA FINAL EN LA QUE SE DEBE IMPRIMIR POR LO QUE EN LA 
    /// VERSIÖN SUBIDA Generate SIEMPRE DEBE USAR EL MÉTODO imprimirArbolLineal
    private void imprimirArbolVisual(FileWriter fileWriter, NodoArbol nodo, int nivel) throws IOException {
        if (nodo == null) {
            return;
        }

        // Imprimir indentación según el nivel
        for (int i = 0; i < nivel; i++) {
            fileWriter.write("  ");
        }

        // Imprimir el nodo
        fileWriter.write("├─ " + nodo.getValor() + "\n");

        // Imprimir hijos recursivamente
        if (nodo.getLeft() != null) {
            imprimirArbolVisual(fileWriter, nodo.getLeft(), nivel + 1);
        }

        if (nodo.getRight() != null) {
            imprimirArbolVisual(fileWriter, nodo.getRight(), nivel + 1);
        }
    }

    private void imprimirArbolLineal(FileWriter fileWriter, NodoArbol nodo) throws IOException {
        if (nodo == null) {
            return;
        }

        // Recorrer en postorden (izquierda, derecha, padre)
        imprimirArbolLinealRecursivo(fileWriter, nodo);
    }

    private void imprimirArbolLinealRecursivo(FileWriter fileWriter, NodoArbol nodo) throws IOException {
        if (nodo == null) {
            return;
        }

        // Si es una hoja, solo imprimir el valor
        if (nodo.esHoja()) {
            fileWriter.write(nodo.getValor() + " ");
            return;
        }

        // Recursividad izquierda
        if (nodo.getLeft() != null) 
            imprimirArbolLinealRecursivo(fileWriter, nodo.getLeft());
        

        // Recursividad derecha
        if (nodo.getRight() != null) 
            imprimirArbolLinealRecursivo(fileWriter, nodo.getRight());        

        //Nodo principal
        fileWriter.write(nodo.getValor() + " ");
    }
}
