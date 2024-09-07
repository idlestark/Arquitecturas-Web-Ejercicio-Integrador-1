package entities;

public class Producto {

    private Integer idProducto;
    private String nombre;
    private float valor;

    public Producto(Integer idProducto, String nombre, float valor) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.valor = valor;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public float getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "IdProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", valor=" + valor +
                '}';
    }

}
