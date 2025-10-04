package lyc.compiler.model;


public class NodoArbol {
    
    private String valor;
    private String tipo;
    private NodoArbol left;
    private NodoArbol right;    
    
    public NodoArbol(String valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
        this.left = null;
        this.right = null;
    }    
    
    public NodoArbol(String valor, String tipo, NodoArbol left, NodoArbol right) {
        this.valor = valor;
        this.tipo = tipo;
        this.left = left;
        this.right = right;
    }
    
    // Getters y Setters
    
    public String getValor() {
        return valor;
    }
    
    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public NodoArbol getLeft() {
        return left;
    }
    
    public void setLeft(NodoArbol left) {
        this.left = left;
    }
    
    public NodoArbol getRight() {
        return right;
    }
    
    public void setRight(NodoArbol right) {
        this.right = right;
    }    
    
    public boolean esHoja() {
        return left == null && right == null;
    }   
    
    @Override
    public String toString() {
        return "NodoArbol{" +
                "valor='" + valor + '\'' +
                ", tipo='" + tipo + '\'' +
                ", esHoja=" + esHoja() +
                '}';
    }
}
