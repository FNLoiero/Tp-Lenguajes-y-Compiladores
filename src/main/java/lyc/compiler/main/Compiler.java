package lyc.compiler.main;

import lyc.compiler.Parser;
import lyc.compiler.factories.FileFactory;
import lyc.compiler.factories.ParserFactory;
import lyc.compiler.files.FileOutputWriter;
import lyc.compiler.files.SymbolTableGenerator;
import lyc.compiler.files.IntermediateCodeGenerator;
import lyc.compiler.files.AsmCodeGenerator;
import lyc.compiler.model.NodoArbol;

import java.io.IOException;
import java.io.Reader;

public final class Compiler {

    private Compiler(){}

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Filename must be provided as argument.");
            System.exit(0);
        }

        try (Reader reader = FileFactory.create(args[0])) {
            Parser parser = ParserFactory.create(reader);
            parser.parse();
            
            // Obtener el árbol sintáctico del parser
            NodoArbol arbolSintactico = parser.getArbolSintactico();
            
            FileOutputWriter.writeOutput("symbol-table.txt", new SymbolTableGenerator());
            
            // Configurar el árbol sintáctico en el generador de código intermedio
            IntermediateCodeGenerator.setArbolSintactico(arbolSintactico);
            FileOutputWriter.writeOutput("intermediate-code.txt", new IntermediateCodeGenerator());
            
            // Configurar el árbol sintáctico en el generador de código assembler
            AsmCodeGenerator.setArbolSintactico(arbolSintactico);
            FileOutputWriter.writeOutput("final.asm", new AsmCodeGenerator());
        } catch (IOException e) {
            System.err.println("There was an error trying to read input file " + e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Compilation error: " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Compilation Successful");

    }

}
