package dto;

public class ProductoDTO {

    private String nombreP;
    private float precio;

    public ProductoDTO(String nombreP, float precio) {
        this.nombreP = nombreP;
        this.precio = precio;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "nombreP='" + nombreP + '\'' +
                ", precio=" + precio +
                '}';
    }
}
