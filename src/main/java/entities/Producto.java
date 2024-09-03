package entities;

public class Producto {
    private Integer idProducto;
    private String nombre;
    private float valor;

    public Producto(Integer idProducto, String nombre, float valor) {
        idProducto = idProducto;
        this.nombre = nombre;
        this.valor = valor;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
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
