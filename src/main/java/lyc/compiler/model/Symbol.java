package lyc.compiler.model;

public class Symbol {
    public String nombre;
    public String tipoDato;
    public String valor;
    public String longitud;

    public Symbol(String nombre, String tipoDato, String valor, String longitud) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
        this.longitud = longitud;
    }
}