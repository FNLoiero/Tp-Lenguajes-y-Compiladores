package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import lyc.compiler.model.NodoArbol;

public class IntermediateCodeGenerator implements FileGenerator {

    private static NodoArbol arbolSintactico;
    private static int nodeCounter = 0;

    public static void setArbolSintactico(NodoArbol arbol) {
        arbolSintactico = arbol;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        if (arbolSintactico != null) {
            // Generar código DOT para Graphviz
            generarCodigoDOT(fileWriter, arbolSintactico);
        } else {
            fileWriter.write("No se generó árbol sintáctico.");
        }
    }


    /**
     Genera código DOT para Graphviz a partir del árbol sintáctico
     */
    private void generarCodigoDOT(FileWriter fileWriter, NodoArbol nodo) throws IOException {
        // Resetear contador de nodos
        nodeCounter = 0;
        
        // Encabezado
        fileWriter.write("digraph ArbolSintactico {\n");
        fileWriter.write("    node [shape=box, style=filled, fillcolor=lightblue];\n");
        fileWriter.write("    edge [color=black];\n\n");
        
        // Generar definiciones de nodos y conexiones
        generarNodosYConexiones(fileWriter, nodo);
        
        // Cerrar el grafo
        fileWriter.write("}\n");
    }
    
    /**
     * Genera las definiciones de nodos y sus conexiones recursivamente
     */
    private void generarNodosYConexiones(FileWriter fileWriter, NodoArbol nodo) throws IOException {
        if (nodo == null) {
            return;
        }
        
        // Generar ID único para el nodo actual
        int currentNodeId = nodeCounter++;
        String nodeId = "nodo" + currentNodeId;
        
        // Escapar caracteres especiales en el valor del nodo para DOT
        String valorEscapado = escaparValor(nodo.getValor());
        
        // Definir el nodo
        fileWriter.write("    " + nodeId + " [label=\"" + valorEscapado + "\"];\n");
        
        // Procesar hijo izquierdo
        if (nodo.getLeft() != null) {
            int leftChildId = nodeCounter;
            generarNodosYConexiones(fileWriter, nodo.getLeft());
            fileWriter.write("    " + nodeId + " -> nodo" + leftChildId + " [label=\"izq\"];\n");
        }
        
        // Procesar hijo derecho
        if (nodo.getRight() != null) {
            int rightChildId = nodeCounter;
            generarNodosYConexiones(fileWriter, nodo.getRight());
            fileWriter.write("    " + nodeId + " -> nodo" + rightChildId + " [label=\"der\"];\n");
        }
    }
    
    /**
     * Escapa caracteres especiales para el formato DOT
     */
    private String escaparValor(String valor) {
        if (valor == null) {
            return "";
        }
        
        // Escapar comillas dobles y caracteres especiales
        return valor.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("%", "\\%")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
