package lyc.compiler.model;


public class NodoArbol {
    
    private String valor;
    private NodoArbol left;
    private NodoArbol right;    
    
    public NodoArbol(String valor) {
        this.valor = valor;
        this.left = null;
        this.right = null;
    }    
    
    public NodoArbol(String valor, NodoArbol left, NodoArbol right) {
        this.valor = valor;
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
                ", esHoja=" + esHoja() +
                '}';
    }
}
