package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import static lyc.compiler.constants.Constants.*;
import lyc.compiler.files.SymbolTableGenerator;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

  /* Tema especial labeledBreak: palabras que no pueden usarse como etiqueta */
  private static final java.util.Set<String> PALABRAS_RESERVADAS = java.util.Set.of(
      "while", "FOR", "TO", "STEP", "NEXT", "if", "else", "write", "read", "init",
      "AND", "OR", "NOT", "Float", "Int", "String", "equalExpressions", "isZero",
      "break", "continue");

  /* Arma el token BREAK / CONTINUE. Si viene seguido de una etiqueta en la misma
     linea, el valor del token es el nombre de la etiqueta; si no, el valor es null */
  private Symbol corteOSalto(int tipo, int largoPalabra) throws CompilerException {
      String etiqueta = yytext().substring(largoPalabra).trim();
      if (PALABRAS_RESERVADAS.contains(etiqueta)) {
          // No es una etiqueta: se devuelve la palabra siguiente al scanner
          yypushback(yylength() - largoPalabra);
          return symbol(tipo);
      }
      if (etiqueta.length() > MAX_LENGTH) {
          throw new InvalidLengthException("Etiqueta demasiado larga (max 30): " + etiqueta);
      }
      return symbol(tipo, etiqueta);
  }
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

OP_SUMA = "+"
OP_MULT = "*"
OP_RESTA = "-"
OP_DIV = "/"
OP_ASIG = ":="
COMP_MEN = "<"
COMP_MAY = ">"
OP_COMA = ","
OP_PUNTOS = ":"

PAR_ABRIR = "("
PAR_CERRAR = ")"
LLAVE_ABRIR = "{"
LLAVE_CERRAR = "}"
COM_INI = "#+"
COM_FIN = "+#"

DIGITO = [0-9]
LETRA = [a-zA-Z]

COMENTARIO = {COM_INI}.*{COM_FIN}
ID = {LETRA}({LETRA}|{DIGITO})*
CTE_CADENA = \"([^\"\\\\]|\\\\.)*\"
CTE_ENTERA = {DIGITO}+ | -{DIGITO}+
CTE_FLOTANTE = "-"?({DIGITO}+\.{DIGITO}*|\.{DIGITO}+)
ESPACIO_BLANCO = {LineTerminator} | {Identation}

/* Tema especial labeledBreak: la etiqueta opcional va en la misma linea */
CORTE = "break"
SALTO = "continue"
CORTE_ETIQUETADO = {CORTE}{Identation}+{ID}
SALTO_ETIQUETADO = {SALTO}{Identation}+{ID}

%%


<YYINITIAL> {
  /* keywords */
  "while"           { return symbol(ParserSym.WHILE); }  
  "FOR"             { return symbol(ParserSym.FOR); }
  "TO"              { return symbol(ParserSym.TO); }
  "STEP"            { return symbol(ParserSym.STEP); }
  "NEXT"            { return symbol(ParserSym.NEXT); }
  "if"              { return symbol(ParserSym.IF); }
  "else"            { return symbol(ParserSym.ELSE); }  
  "write"           { return symbol(ParserSym.WRITE); }
  "read"            { return symbol(ParserSym.READ); }
  "init"            { return symbol(ParserSym.INIT); }
  "AND"             { return symbol(ParserSym.AND); }
  "OR"              { return symbol(ParserSym.OR); }
  "NOT"             { return symbol(ParserSym.NOT); }
  "Float" 	        { return symbol(ParserSym.DT_FLOAT); }
  "Int" 		        { return symbol(ParserSym.DT_INT); }
  "String" 	        { return symbol(ParserSym.DT_STRING); }

  /* Temas especiales */
  "equalExpressions"  { return symbol(ParserSym.EQUAL_EXPRESSIONS); }
  "isZero"            { return symbol(ParserSym.IS_ZERO); }
  {CORTE_ETIQUETADO}  { return corteOSalto(ParserSym.BREAK, 5); }
  {SALTO_ETIQUETADO}  { return corteOSalto(ParserSym.CONTINUE, 8); }
  {CORTE}             { return symbol(ParserSym.BREAK); }
  {SALTO}             { return symbol(ParserSym.CONTINUE); }


  /* identifiers */
  {ID}
  {
      if (yytext().length() > MAX_LENGTH) {
          throw new InvalidLengthException("Identificador demasiado largo (max 30): " + yytext());          
      }
      SymbolTableGenerator.Insert(yytext(), "ID", yytext());
      return symbol(ParserSym.ID, yytext());
  }

  /* Constants */
  {CTE_ENTERA} {
    try {
      int valor = Integer.parseInt(yytext());
      if (valor > 65535) {
          throw new InvalidIntegerException("Constante entera fuera del rango de 0 a 65535: " + yytext());          
      }
    }
    catch(Exception e) {
      throw new InvalidIntegerException("Constante entera fuera del rango de 0 a 65535: " + yytext());      
    }
    SymbolTableGenerator.Insert("_" + yytext(), "CTE_ENTERA", yytext()); 
    return symbol(ParserSym.CTE_ENTERA, yytext()); 
  }
  {CTE_FLOTANTE} {
    try {
      float valor = Float.parseFloat(yytext());
      double min_pos = 1.4 * Math.pow(10, -45);
      double max_pos = 3.4028235 * Math.pow(10, 38);

      if ((valor != 0.0) && (Math.abs(valor) < min_pos || Math.abs(valor) > max_pos)) {
        throw new InvalidIntegerException("Constante flotante fuera del rango de -3.4028235*10^38 a 3.3.4028235*10^38: " + yytext());        
      }
    } 
    catch(Exception e) {
      throw new InvalidIntegerException("Constante flotante fuera del rango de -3.4028235*10^38 a 3.3.4028235*10^38: " + yytext());      
    }

    SymbolTableGenerator.Insert("_" + yytext(), "CTE_FLOTANTE", yytext()); 
    return symbol(ParserSym.CTE_FLOTANTE, yytext());
  }
  {CTE_CADENA} { 
    String valor = yytext().substring(1, yytext().length() - 1);
    if (valor.length() > MAX_LENGTH) {
        throw new InvalidLengthException("Constante cadena excede los 30 caracteres: " + valor);    
    }
    SymbolTableGenerator.Insert("_" + yytext(), "CTE_CADENA", yytext()); 
    return symbol(ParserSym.CTE_CADENA, yytext()); 
  }
  {COMENTARIO}                        {  }

  /* operators */
  {OP_SUMA}         { return symbol(ParserSym.OP_SUMA); }
  {OP_RESTA}        { return symbol(ParserSym.OP_RESTA); }
  {OP_MULT}         { return symbol(ParserSym.OP_MULT); }
  {OP_DIV}          { return symbol(ParserSym.OP_DIV); }
  {OP_ASIG}         { return symbol(ParserSym.OP_ASIG); }
  {PAR_ABRIR}       { return symbol(ParserSym.PAR_ABRIR); }
  {PAR_CERRAR}      { return symbol(ParserSym.PAR_CERRAR); }
  {LLAVE_ABRIR}     { return symbol(ParserSym.LLAVE_ABRIR); }
  {LLAVE_CERRAR}    { return symbol(ParserSym.LLAVE_CERRAR); } 
  {OP_COMA}         { return symbol(ParserSym.OP_COMA); }
  {COMP_MEN}        { return symbol(ParserSym.COMP_MEN); }
  {COMP_MAY}        { return symbol(ParserSym.COMP_MAY); }
  {OP_PUNTOS}       { return symbol(ParserSym.OP_PUNTOS); }

  /* whitespace */
  {ESPACIO_BLANCO}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
