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
            imprimirArbol(fileWriter, arbolSintactico, 0);
        } else {
            fileWriter.write("No se generó árbol sintáctico.");
        }
    }

    private void imprimirArbol(FileWriter fileWriter, NodoArbol nodo, int nivel) throws IOException {
        if (nodo == null) {
            return;
        }

        // Imprimir indentación según el nivel
        for (int i = 0; i < nivel; i++) {
            fileWriter.write("  ");
        }

        // Imprimir el nodo
        fileWriter.write("├─ " + nodo.getValor() + " (" + nodo.getTipo() + ")\n");

        // Imprimir hijos recursivamente
        if (nodo.getLeft() != null) {
            fileWriter.write("  ");
            for (int i = 0; i < nivel; i++) {
                fileWriter.write("  ");
            }
            fileWriter.write("├─ LEFT:\n");
            imprimirArbol(fileWriter, nodo.getLeft(), nivel + 2);
        }

        if (nodo.getRight() != null) {
            fileWriter.write("  ");
            for (int i = 0; i < nivel; i++) {
                fileWriter.write("  ");
            }
            fileWriter.write("├─ RIGHT:\n");
            imprimirArbol(fileWriter, nodo.getRight(), nivel + 2);
        }
    }
}
