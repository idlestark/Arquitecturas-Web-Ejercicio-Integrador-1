package dto;

public class ClienteDTO {
    private int idCliente;
    private String nombre;
    private String email;
    private int valor;

    public ClienteDTO(int idCliente, String nombre, String email, int valor) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getIdCliente() {
        return idCliente;
    }

    @Override
    public String toString() {
        return "ClienteDTO{" +
                "idCliente=" + idCliente +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", valor=" + valor +
                '}';
    }
}
