package entities;

public class FacturaProducto {

    private Integer idFactura;
    private Integer idProducto;
    private Integer cantidad;

    public FacturaProducto(Integer idFactura, Integer idProducto, Integer cantidad) {
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return "Factura_producto{" +
                "IdFactura=" + idFactura +
                ", IdProducto=" + idProducto +
                ", cantidad=" + cantidad +
                '}';
    }

}
