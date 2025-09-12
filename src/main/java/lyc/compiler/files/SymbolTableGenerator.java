package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import lyc.compiler.model.Symbol;

public class SymbolTableGenerator implements FileGenerator{

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write(SymbolTableGenerator.Write());
    }

    private static final HashMap<String, Symbol> table = new HashMap<>();

    public static void Insert(String nombre, String tipoDato, String valor) {
        if (!table.containsKey(nombre)) {
            String longitud;
            
            if ("ID".equals(tipoDato)) {
                tipoDato = "";
                valor = "";
                longitud = "";
            } 
            else if ("CTE_CADENA".equals(tipoDato)) {
                longitud = String.valueOf(valor.length());
            }             
            else 
                longitud = "";            

            table.put(nombre, new Symbol(nombre, tipoDato, valor, longitud));
        }
    }

    public static void SetTipo(Object nombre, Object tipo) {
        Symbol entry = table.get((String)nombre);
        if (entry != null) {
            entry.tipoDato = (String)tipo;
        } else {
            System.err.println("Error: identificador '" + nombre + "' no encontrado en tabla de simbolos.");
        }
    }

    public static String GetTipo(Object nombre) {
        Symbol entry = table.get(nombre);
        if (entry != null)
            return entry.tipoDato;
        
        throw new RuntimeException("Error: identificador '" + nombre + "' no encontrado en tabla de símbolos.");
    }
    
    public static String Write() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════╦══════════════╦══════════════════════════════════════════════════════╦══════════╗\n");
        sb.append(String.format("║ %-52s ║ %-12s ║ %-52s ║ %-8s ║\n", "NOMBRE", "TIPODATO", "VALOR", "LONGITUD"));
        sb.append("╠══════════════════════════════════════════════════════╬══════════════╬══════════════════════════════════════════════════════╬══════════╣\n");
        for (Symbol s : table.values()) {
            String nombreMostrado = s.nombre;
            String valorMostrado = s.valor;
            if ("String".equals(s.tipoDato) && s.valor.length() > 15) {
                nombreMostrado = s.nombre.substring(0, 15);
                nombreMostrado = nombreMostrado + "...\"";
                valorMostrado = s.valor.substring(0, 15);
                valorMostrado = valorMostrado + "...\"";
            }
            sb.append(String.format("║ %-52s ║ %-12s ║ %-52s ║ %-8s ║\n",
                nombreMostrado, s.tipoDato, valorMostrado, s.longitud));
        }
        sb.append("╚══════════════════════════════════════════════════════╩══════════════╩══════════════════════════════════════════════════════╩══════════╝\n");
        return sb.toString();
    }

}

