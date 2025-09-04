package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import static lyc.compiler.constants.Constants.*;

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
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

OP_SUMA = "+"
OP_MULT = "*"
OP_RESTA = "-"
OP_DIV = "/"
OP_ASIG = ":="
OP_TIPO = ":"
COMP_MEN = "<"
COMP_MAY = ">"
COMA = ","

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
CTE_ENTERA = {DIGITO}+
CTE_FLOTANTE = "-"?({DIGITO}+\.{DIGITO}*|\.{DIGITO}+)

ESPACIO_BLANCO = {LineTerminator} | {Identation}
ID = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+

%%


/* keywords */

<YYINITIAL> {
  /* keywords */
  "while"           { return symbol(ParserSym.WHILE); }  
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

  /* identifiers */
  {ID}                             { return symbol(ParserSym.ID, yytext()); }
  /* Constants */
  {CTE_ENTERA}                        { return symbol(ParserSym.CTE_ENTERA, yytext()); }
  {CTE_FLOTANTE}                      { return symbol(ParserSym.CTE_FLOTANTE, yytext()); }
  {CTE_CADENA}                        { return symbol(ParserSym.CTE_CADENA, yytext()); }
  

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
  {COMA}            { return symbol(ParserSym.COMA); }

  /* whitespace */
  {ESPACIO_BLANCO}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
