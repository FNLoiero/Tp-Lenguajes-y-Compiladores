package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lyc.compiler.model.NodoArbol;
import lyc.compiler.model.Symbol;

public class AsmCodeGenerator implements FileGenerator {

    // <editor-fold desc="Campos y Variables">
    private static NodoArbol arbolSintactico;
    private StringBuilder codigoAsm;
    private StringBuilder seccionData;
    private int contadorEtiquetas;
    private int contadorTemporales;
    private Set<String> stringsGenerados;
    private Set<String> temporalesDeclaradas;
    private HashMap<String, String> constantesString; 
    // </editor-fold>

    // <editor-fold desc="Métodos Públicos">
    public static void setArbolSintactico(NodoArbol arbol) {
        arbolSintactico = arbol;
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        if (arbolSintactico == null) {
            fileWriter.write("; No se generó árbol sintáctico.\n");
            return;
        }

        codigoAsm = new StringBuilder();
        seccionData = new StringBuilder();
        contadorEtiquetas = 0;
        contadorTemporales = 0;
        stringsGenerados = new HashSet<>();
        temporalesDeclaradas = new HashSet<>();
        constantesString = new HashMap<>();
        
        generarSeccionData();

        
        StringBuilder codigoTemp = new StringBuilder();
        StringBuilder codigoAsmOriginal = codigoAsm;
        codigoAsm = codigoTemp; 
        
        // Generar código del programa recorriendo el árbol (esto puede generar más temporales y literales)
        generarCodigo(arbolSintactico);
        
        // Restaurar codigoAsm y generar archivo completo
        codigoAsm = codigoAsmOriginal;
        generarEncabezado();
        generarSeccionDataCompleta();
        
        // Generar sección .CODE
        codigoAsm.append("\n.CODE\n\n");
        codigoAsm.append("PUBLIC START\n");
        codigoAsm.append("START:\n");
        codigoAsm.append("    mov AX,@DATA\n");
        codigoAsm.append("    mov DS,AX\n");
        codigoAsm.append("    mov es,ax\n");

        // Agregar el código generado
        codigoAsm.append(codigoTemp.toString());

        // Generar código de finalización
        codigoAsm.append("    displayString _msgOK\n");
        codigoAsm.append("    newLine 1\n");
        codigoAsm.append("    mov ax, 4C00h\n");
        codigoAsm.append("    int 21h\n");
        codigoAsm.append("\nEND START\n");

        fileWriter.write(codigoAsm.toString());
    }
    // </editor-fold>

    // <editor-fold desc="Sección .DATA">
    private void generarEncabezado() {
        codigoAsm.append("include macros.asm\n");
        codigoAsm.append("include macros2.asm\n");        
        codigoAsm.append("include number.asm\n\n");
        codigoAsm.append(".MODEL  LARGE\n");
        codigoAsm.append(".386\n");
        codigoAsm.append(".STACK 200h\n\n");
        codigoAsm.append("MAXTEXTSIZE equ 50\n\n");
    }

    private void generarSeccionData() {
        HashMap<String, Symbol> tabla = SymbolTableGenerator.getTable();
        
        // Primero, generar variables declaradas (ID con tipo)
        for (Symbol symbol : tabla.values()) {
            if (symbol.tipoDato != null && !symbol.tipoDato.isEmpty() && 
                !symbol.tipoDato.equals("CTE_CADENA") && !symbol.nombre.startsWith("_")) {                
                String nombreVar = "_" + symbol.nombre;
                if ("Int".equals(symbol.tipoDato) || "Float".equals(symbol.tipoDato))
                    seccionData.append("    ").append(nombreVar).append("         dd  ?\n");
                else if ("String".equals(symbol.tipoDato))
                    seccionData.append("    ").append(nombreVar).append("         db  MAXTEXTSIZE dup (?),'$'\n");
                
            }
        }

        // Generar constantes string (CTE_CADENA)
        for (Symbol symbol : tabla.values()) {
            if ("CTE_CADENA".equals(symbol.tipoDato) && symbol.valor != null && !symbol.valor.isEmpty()) {
                // Limpiar el valor de comillas si las tiene
                String valor = symbol.valor;
                if (valor.startsWith("\"") && valor.endsWith("\""))
                    valor = valor.substring(1, valor.length() - 1);
                // Usar método centralizado para obtener nombre único
                obtenerNombreConstanteString(valor);
            }
        }        
        
        seccionData.append("    _msgOK            db  0DH,0AH,\"Se ejecuto OK\",'$'\n");
    }

    private void generarSeccionDataCompleta() {
        codigoAsm.append(".DATA\n\n");
        codigoAsm.append(seccionData.toString());
        
        // Agregar todas las variables temporales que se usaron
        for (String temp : temporalesDeclaradas) {
            codigoAsm.append("    ").append(temp).append("         dd  ?\n");
        }
    }
    // </editor-fold>

    // <editor-fold desc="Generación de Código Principal">
    private void generarCodigo(NodoArbol nodo) {
        if (nodo == null) return;

        String valor = nodo.getValor();

        switch (valor) {
            case "Programa":
                // Procesar serie de instrucciones
                if (nodo.getLeft() != null)
                    generarSerieInstrucciones(nodo.getLeft());
                break;

            case "instrucción":
                // Serie de instrucciones
                if (nodo.getLeft() != null)
                    generarCodigo(nodo.getLeft());
                if (nodo.getRight() != null)
                    generarCodigo(nodo.getRight());
                break;

            case ":=":
                generarAsignacion(nodo);
                break;

            case "READ":
                generarRead(nodo);
                break;

            case "WRITE":
                generarWrite(nodo);
                break;

            case "IF":
                generarIf(nodo);
                break;

            case "WHILE":
                generarWhile(nodo);
                break;

            case "+":
            case "-":
            case "*":
            case "/":
                // Operaciones aritméticas se manejan en expresiones
                break;

            default:
                // Procesar hijos si existen
                if (nodo.getLeft() != null)
                    generarCodigo(nodo.getLeft());
                if (nodo.getRight() != null)
                    generarCodigo(nodo.getRight());
                break;
        }
    }

    private void generarSerieInstrucciones(NodoArbol nodo) {
        if (nodo == null) return;

        if ("instrucción".equals(nodo.getValor())) {
            if (nodo.getLeft() != null)
                generarCodigo(nodo.getLeft());
            if (nodo.getRight() != null)
                generarCodigo(nodo.getRight());
        } else
            generarCodigo(nodo);
    }
    // </editor-fold>

    // <editor-fold desc="Instrucciones Simples">
    private void generarAsignacion(NodoArbol nodo) {
        if (nodo.getLeft() == null) return;

        String nombreVar = "_" + nodo.getLeft().getValor();
        String tipoVar = SymbolTableGenerator.GetTipo(nodo.getLeft().getValor());

        if (nodo.getRight() != null) {
            String exprResult = generarExpresion(nodo.getRight(), tipoVar);
            
            if ("Int".equals(tipoVar)) {
                // Para enteros, usar mov con eax
                codigoAsm.append("    mov eax, ").append(exprResult).append("\n");
                codigoAsm.append("    mov ").append(nombreVar).append(", eax\n");
            } else if ("Float".equals(tipoVar)) {
                // Para floats, usar stack de float
                codigoAsm.append("    fld ").append(exprResult).append("\n");
                codigoAsm.append("    fstp ").append(nombreVar).append("\n");
            } else if ("String".equals(tipoVar)) {
                // Para strings, se necesita copiar el string
                // En modo LARGE, usar OFFSET para obtener la dirección
                codigoAsm.append("    mov si, OFFSET ").append(exprResult).append("\n");
                codigoAsm.append("    mov di, OFFSET ").append(nombreVar).append("\n");
                codigoAsm.append("    STRCPY\n");
            }
        }
    }

    private void generarRead(NodoArbol nodo) {
        if (nodo.getLeft() == null) return;

        String nombreVar = "_" + nodo.getLeft().getValor();
        String tipoVar = SymbolTableGenerator.GetTipo(nodo.getLeft().getValor());

        if ("Int".equals(tipoVar))
            codigoAsm.append("    GetInteger ").append(nombreVar).append("\n");
        else if ("Float".equals(tipoVar))
            codigoAsm.append("    GetFloat ").append(nombreVar).append("\n");
        else if ("String".equals(tipoVar))
            codigoAsm.append("    getString ").append(nombreVar).append("\n");
    }

    private void generarWrite(NodoArbol nodo) {
        if (nodo.getLeft() == null) return;

        NodoArbol param = nodo.getLeft();
        String valor = param.getValor();

        if (param.esHoja()) {
            if (valor.startsWith("\"") && valor.endsWith("\"")) {
                // Es un string literal
                String nombreStr = generarStringLiteral(valor);
                codigoAsm.append("    displayString ").append(nombreStr).append("\n");
            } else {
                // Es una variable
                String nombreVar = "_" + valor;
                String tipoVar = SymbolTableGenerator.GetTipo(valor);
                
                if ("Int".equals(tipoVar))
                    codigoAsm.append("    DisplayInteger ").append(nombreVar).append("\n");
                else if ("Float".equals(tipoVar))
                    codigoAsm.append("    DisplayFloat ").append(nombreVar).append(", 2\n");
                else if ("String".equals(tipoVar))
                    codigoAsm.append("    displayString ").append(nombreVar).append("\n");
            }
        }
        codigoAsm.append("    newLine 1\n");
    }
    // </editor-fold>

    // <editor-fold desc="Estructuras de Control">
    private void generarIf(NodoArbol nodo) {
        if (nodo.getLeft() == null) return;

        int etiquetaIf = contadorEtiquetas++;
        int etiquetaElse = contadorEtiquetas++;
        int etiquetaEnd = contadorEtiquetas++;

        // Generar condición
        NodoArbol condicion = nodo.getLeft().getLeft();
        generarCondicion(condicion, "ET_IF_" + etiquetaIf, "ET_ELSE_" + etiquetaElse);

        // Código del if
        codigoAsm.append("ET_IF_").append(etiquetaIf).append(":\n");
        if (nodo.getRight() != null && nodo.getRight().getLeft() != null)
            generarCodigo(nodo.getRight().getLeft());
        codigoAsm.append("    JMP ET_END_").append(etiquetaEnd).append("\n");

        // Código del else (si existe)
        codigoAsm.append("ET_ELSE_").append(etiquetaElse).append(":\n");
        if (nodo.getRight() != null && nodo.getRight().getRight() != null)
            generarCodigo(nodo.getRight().getRight());

        codigoAsm.append("ET_END_").append(etiquetaEnd).append(":\n");
    }

    private void generarWhile(NodoArbol nodo) {
        if (nodo.getLeft() == null) return;

        int etiquetaInicio = contadorEtiquetas++;
        int etiquetaEnd = contadorEtiquetas++;

        codigoAsm.append("ET_WHILE_").append(etiquetaInicio).append(":\n");

        // Generar condición
        NodoArbol condicion = nodo.getLeft().getLeft();
        generarCondicion(condicion, "ET_WHILE_BODY_" + etiquetaInicio, "ET_WHILE_END_" + etiquetaEnd);

        // Código del cuerpo
        codigoAsm.append("ET_WHILE_BODY_").append(etiquetaInicio).append(":\n");
        if (nodo.getRight() != null && nodo.getRight().getLeft() != null)
            generarCodigo(nodo.getRight().getLeft());
        codigoAsm.append("    JMP ET_WHILE_").append(etiquetaInicio).append("\n");

        codigoAsm.append("ET_WHILE_END_").append(etiquetaEnd).append(":\n");
    }

    private void generarCondicion(NodoArbol nodo, String etiquetaTrue, String etiquetaFalse) {
        if (nodo == null) return;

        String valor = nodo.getValor();

        if ("NOT".equals(valor)) {
            // Invertir etiquetas
            generarCondicion(nodo.getLeft(), etiquetaFalse, etiquetaTrue);
            return;
        }

        if ("AND".equals(valor)) {
            int etiquetaIntermedia = contadorEtiquetas++;
            generarCondicion(nodo.getLeft(), "ET_AND_" + etiquetaIntermedia, etiquetaFalse);
            codigoAsm.append("ET_AND_").append(etiquetaIntermedia).append(":\n");
            generarCondicion(nodo.getRight(), etiquetaTrue, etiquetaFalse);
            return;
        }

        if ("OR".equals(valor)) {
            int etiquetaIntermedia = contadorEtiquetas++;
            generarCondicion(nodo.getLeft(), etiquetaTrue, "ET_OR_" + etiquetaIntermedia);
            codigoAsm.append("ET_OR_").append(etiquetaIntermedia).append(":\n");
            generarCondicion(nodo.getRight(), etiquetaTrue, etiquetaFalse);
            return;
        }

        if ("IS_ZERO".equals(valor)) {
            String expr = generarExpresion(nodo.getLeft(), "Int");
            codigoAsm.append("    cmp ").append(expr).append(", 0\n");
            codigoAsm.append("    je ").append(etiquetaTrue).append("\n");
            codigoAsm.append("    jmp ").append(etiquetaFalse).append("\n");
            return;
        }

        if ("<".equals(valor) || ">".equals(valor)) {
            String left = generarExpresion(nodo.getLeft(), "Int");
            String right = generarExpresion(nodo.getRight(), "Int");

            // Verificar si son float
            String tipo = SymbolTableGenerator.GetTipo(left.replace("_", ""));
            if (tipo != null && "Float".equals(tipo)) {
                // Comparación de float
                codigoAsm.append("    fld ").append(left).append("\n");
                codigoAsm.append("    fld ").append(right).append("\n");
                codigoAsm.append("    fxch\n");
                codigoAsm.append("    fcomp\n");
                codigoAsm.append("    fstsw ax\n");
                codigoAsm.append("    ffree st(0)\n");
                codigoAsm.append("    sahf\n");
                if (">".equals(valor))
                    codigoAsm.append("    JBE ").append(etiquetaFalse).append("\n");
                else
                    codigoAsm.append("    JAE ").append(etiquetaFalse).append("\n");
            } else {
                // Comparación de enteros
                codigoAsm.append("    mov eax, ").append(left).append("\n");
                codigoAsm.append("    cmp eax, ").append(right).append("\n");
                if (">".equals(valor))
                    codigoAsm.append("    jle ").append(etiquetaFalse).append("\n");
                else
                    codigoAsm.append("    jge ").append(etiquetaFalse).append("\n");
            }
            codigoAsm.append("    jmp ").append(etiquetaTrue).append("\n");
            return;
        }

        // Si no es ninguna condición conocida, procesar recursivamente
        if (nodo.getLeft() != null)
            generarCondicion(nodo.getLeft(), etiquetaTrue, etiquetaFalse);
    }
    // </editor-fold>

    // <editor-fold desc="Generación de Expresiones">
    private String generarExpresion(NodoArbol nodo, String tipoEsperado) {
        if (nodo == null) return "";

        String valor = nodo.getValor();

        // Si es una hoja, devolver el valor directamente
        if (nodo.esHoja()) {
            if (valor.matches("-?\\d+")) {
                // Es un entero literal - necesitamos cargarlo en una variable temporal para operaciones
                String tempVar = "_temp_lit_" + (contadorTemporales++);
                if (!temporalesDeclaradas.contains(tempVar)) {
                    temporalesDeclaradas.add(tempVar);
                }
                codigoAsm.append("    mov ").append(tempVar).append(", ").append(valor).append("\n");
                return tempVar;
            } else if (valor.matches("-?\\d+\\.\\d+")) {
                // Es un float literal - necesitamos cargarlo en una variable temporal
                String tempVar = "_temp_lit_" + (contadorTemporales++);
                if (!temporalesDeclaradas.contains(tempVar)) {
                    temporalesDeclaradas.add(tempVar);
                }
                // Necesitamos declarar el float literal en .DATA
                String nombreFloat = "_float_lit_" + Math.abs(valor.hashCode());
                seccionData.append("    ").append(nombreFloat).append("         dd  ").append(valor).append("\n");
                codigoAsm.append("    fld ").append(nombreFloat).append("\n");
                codigoAsm.append("    fstp ").append(tempVar).append("\n");
                return tempVar;
            } else if (valor.startsWith("\"") && valor.endsWith("\"")) {
                // Es un string literal
                String nombreStr = generarStringLiteral(valor);
                return nombreStr;
            } else {
                // Es una variable - verificar que existe en la tabla de símbolos
                try {
                    String tipo = SymbolTableGenerator.GetTipo(valor);
                    if (tipo == null || tipo.isEmpty())
                        throw new RuntimeException("Error: variable '" + valor + "' no declarada en la generación de código.");
                } catch (RuntimeException e) {
                    // Si la excepción menciona "no encontrado", es una variable no declarada
                    if (e.getMessage().contains("no encontrado"))
                        throw new RuntimeException("Error: variable '" + valor + "' no declarada. Asegúrate de declarar todas las variables en el bloque INIT.");
                    throw e;
                }
                return "_" + valor;
            }
        }

        // Es una operación
        switch (valor) {
            case "+":
                return generarOperacionBinaria(nodo, "add", tipoEsperado);
            case "-":
                return generarOperacionBinaria(nodo, "sub", tipoEsperado);
            case "*":
                return generarOperacionBinaria(nodo, "mul", tipoEsperado);
            case "/":
                return generarOperacionBinaria(nodo, "div", tipoEsperado);
            case "EQUAL_EXPRESSIONS":
                return generarEqualExpressions(nodo);
            case "IS_ZERO":
                return generarIsZero(nodo);
            default:
                // Procesar recursivamente
                if (nodo.getLeft() != null)
                    return generarExpresion(nodo.getLeft(), tipoEsperado);
                return "";
        }
    }

    private String generarOperacionBinaria(NodoArbol nodo, String operacion, String tipo) {
        String tempVar = "_temp" + (contadorTemporales++);
        
        // Declarar la variable temporal en .DATA
        if (!temporalesDeclaradas.contains(tempVar)) {
            temporalesDeclaradas.add(tempVar);
        }
        
        // Primero generar el código para los operandos
        String left = generarExpresion(nodo.getLeft(), tipo);
        String right = generarExpresion(nodo.getRight(), tipo);

        // Determinar si es float o int
        boolean esFloat = "Float".equals(tipo);
        if (!esFloat && nodo.getLeft() != null) {
            String tipoLeft = obtenerTipoVariable(left);
            String tipoRight = obtenerTipoVariable(right);
            esFloat = "Float".equals(tipoLeft) || "Float".equals(tipoRight);
        }

        if (esFloat) {
            // Operación con float
            // Cargar valores en stack de float (left y right ya son variables temporales o variables)
            codigoAsm.append("    fld ").append(left).append("\n");
            codigoAsm.append("    fld ").append(right).append("\n");
            
            codigoAsm.append("    fxch\n");
            switch (operacion) {
                case "add":
                    codigoAsm.append("    faddp\n");
                    break;
                case "sub":
                    codigoAsm.append("    fsubp\n");
                    break;
                case "mul":
                    codigoAsm.append("    fmulp\n");
                    break;
                case "div":
                    codigoAsm.append("    fdivp\n");
                    break;
            }
            codigoAsm.append("    fstp ").append(tempVar).append("\n");
        } else {
            // Operación con enteros (left y right ya son variables temporales o variables)
            codigoAsm.append("    mov eax, ").append(left).append("\n");
            
            switch (operacion) {
                case "add":
                    codigoAsm.append("    add eax, ").append(right).append("\n");
                    break;
                case "sub":
                    codigoAsm.append("    sub eax, ").append(right).append("\n");
                    break;
                case "mul":
                    codigoAsm.append("    imul ").append(right).append("\n");
                    break;
                case "div":
                    codigoAsm.append("    cdq\n");
                    codigoAsm.append("    idiv ").append(right).append("\n");
                    break;
            }
            codigoAsm.append("    mov ").append(tempVar).append(", eax\n");
        }

        return tempVar;
    }

    private String generarEqualExpressions(NodoArbol nodo) {
        // Equal expressions retorna un valor que indica si todas las expresiones son iguales
        String tempVar = "_temp" + (contadorTemporales++);
        
        // Declarar la variable temporal en .DATA
        if (!temporalesDeclaradas.contains(tempVar)) {
            temporalesDeclaradas.add(tempVar);
        }
        
        // Obtener todos los parámetros y procesarlos
        if (nodo.getLeft() != null) {
            // Procesar expresiones recursivamente
            generarExpresion(nodo.getLeft(), "Int");
        }
        // Por simplicidad, retornamos 1 si son iguales, 0 si no
        codigoAsm.append("    mov ").append(tempVar).append(", 1\n"); // Asumimos que son iguales
        
        return tempVar;
    }

    private String generarIsZero(NodoArbol nodo) {
        String tempVar = "_temp" + (contadorTemporales++);
        
        // Declarar la variable temporal en .DATA
        if (!temporalesDeclaradas.contains(tempVar)) {
            temporalesDeclaradas.add(tempVar);
        }
        
        String expr = generarExpresion(nodo.getLeft(), "Int");
        
        codigoAsm.append("    cmp ").append(expr).append(", 0\n");
        codigoAsm.append("    jne ").append("_not_zero").append(contadorTemporales).append("\n");
        codigoAsm.append("    mov ").append(tempVar).append(", 1\n");
        codigoAsm.append("    jmp ").append("_end_zero").append(contadorTemporales).append("\n");
        codigoAsm.append("_not_zero").append(contadorTemporales).append(":\n");
        codigoAsm.append("    mov ").append(tempVar).append(", 0\n");
        codigoAsm.append("_end_zero").append(contadorTemporales).append(":\n");
        
        return tempVar;
    }
    // </editor-fold>

    // <editor-fold desc="Utilidades">
    private String obtenerTipoVariable(String nombreVar) {
        // Si es una variable temporal, no buscar en tabla de símbolos
        if (nombreVar.startsWith("_temp") || nombreVar.startsWith("_temp_lit_") || nombreVar.startsWith("_float_lit_")) {
            // Variables temporales son enteros por defecto, a menos que contengan un punto
            if (nombreVar.contains("float")) {
                return "Float";
            }
            return "Int";
        }
        
        // Remover prefijo _
        String nombre = nombreVar.startsWith("_") ? nombreVar.substring(1) : nombreVar;
        try {
            return SymbolTableGenerator.GetTipo(nombre);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene el nombre de una constante string. Si ya existe, devuelve el nombre existente.
     * Si no existe, lo genera y lo declara en .DATA usando formato CTE_CADENA_ + hash.
     * 
     * @param contenido Contenido del string sin comillas
     * @return Nombre de la constante generada (formato: CTE_CADENA_ + hash)
     */
    private String obtenerNombreConstanteString(String contenido) {
        // Si ya existe, devolver el nombre existente
        if (constantesString.containsKey(contenido))
            return constantesString.get(contenido);
        
        // Generar nuevo nombre con formato CTE_CADENA_ + hash
        String nombreStr = "CTE_CADENA_" + Math.abs(contenido.hashCode());
        
        // Guardar en el mapa para evitar duplicados
        constantesString.put(contenido, nombreStr);
        
        // Declarar en .DATA solo una vez
        if (!stringsGenerados.contains(nombreStr)) {
            stringsGenerados.add(nombreStr);
            int longitud = contenido.length();
            int padding = Math.max(0, 50 - longitud - 2); // 50 es MAXTEXTSIZE, -2 por '$' y espacio
            seccionData.append("    ").append(nombreStr).append("         db  \"").append(contenido)
                      .append("\",'$', ").append(padding).append(" dup (?)\n");
        }
        
        return nombreStr;
    }

    private String generarStringLiteral(String valor) {
        // Quitar comillas
        String contenido = valor.substring(1, valor.length() - 1);
        // Usar método centralizado
        return obtenerNombreConstanteString(contenido);
    }
    // </editor-fold>
}
