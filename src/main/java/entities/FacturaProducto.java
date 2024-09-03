package entities;

public class FacturaProducto {

    private Integer idFactura;
    private Integer idProducto;
    private Integer cantidad;

    public FacturaProducto(Integer idFactura, Integer idProducto, Integer cantidad) {
        idFactura = idFactura;
        idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        idFactura = idFactura;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
