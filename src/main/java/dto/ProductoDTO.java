package dto;

public class ProductoDTO {

    private int idProducto;
    private String nombreP;
    private float valor;
    private float recaudacion;

    public ProductoDTO(int idProductom, String nombreP, float valor, float recaudacion){
        this.idProducto = idProductom;
        this.nombreP = nombreP;
        this.valor = valor;
        this.recaudacion = recaudacion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(float recaudacion) {
        this.recaudacion = recaudacion;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "idProducto=" + idProducto +
                ", nombreP='" + nombreP + '\'' +
                ", valor=" + valor +
                ", recaudacion=" + recaudacion +
                '}';
    }
}
