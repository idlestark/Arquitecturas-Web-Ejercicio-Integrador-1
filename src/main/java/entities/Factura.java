package entities;

public class Factura {
    private Integer idFactura;
    private Integer idCliente;

    public Factura(Integer idFactura, Integer idCliente) {
        idFactura = idFactura;
        idCliente = idCliente;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        idFactura = idFactura;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "IdFactura=" + idFactura +
                ", IdCliente=" + idCliente +
                '}';
    }
}
