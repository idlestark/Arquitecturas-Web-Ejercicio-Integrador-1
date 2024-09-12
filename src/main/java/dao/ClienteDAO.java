package dao;

import dto.ClienteDTO;
import entities.Cliente;
import entities.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection con;
    public ClienteDAO(Connection con){this.con = con;}

    public void insert(Cliente cliente){
        //METODO INSERT
        String query = "INSERT INTO Cliente(idCliente, nombre, email) VALUES (?, ?, ?)";

        PreparedStatement ps = null;

        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, cliente.getIdCliente());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getEmail());
            ps.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try{
                if(ps != null){
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delete (int idCliente) {
        String query = "DELETE FROM cliente WHERE idCliente = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.executeUpdate();
            System.out.println("El cliente " + idCliente + " se eliminó correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally{
            try{
                if(ps != null){
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void update(Cliente cliente){
        String query = "UPDATE INTO Cliente(nombre, email) VALUES (?, ?) WHERE idCliente = ?";
        PreparedStatement ps = null;

        try{
            ps = con.prepareStatement(query);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setInt(3, cliente.getIdCliente());
            ps.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            try{
                if(ps != null){
                    ps.close();
                }
                con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Cliente find(Integer pk) {
        String query = "SELECT * FROM Cliente WHERE idCliente = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cliente salida = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, pk);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                salida = new Cliente(pk, nombre, email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(ps != null){
                    ps.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    return salida;
    }

    //EJERCICIO 4

    public List<ClienteDTO> ejercicio4() {
        String query = "SELECT c.idCliente, c.nombre, c.email, SUM(fp.cantidad * p.valor) AS totalF FROM Cliente c "+
                "INNER JOIN Factura f ON c.idCliente = f.idCliente "+
                "INNER JOIN Factura_Producto fp ON f.idFactura = fp.idFactura " +
                "INNER JOIN Producto p ON p.idProducto = fp.idProducto " +
                "GROUP BY c.idCliente, c.nombre, c.email ORDER BY totalF DESC";

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ClienteDTO> clientes;
        try{
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            clientes = new ArrayList<ClienteDTO>();
            while(rs.next()){
                int id = rs.getInt("idCliente");
                String nombre = rs.getString("nombre");
                int TotalF = rs.getInt("totalF");
                String email = rs.getString("email");
                ClienteDTO cliente = new ClienteDTO(id ,nombre, email,TotalF);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try{
                if(ps != null){
                    ps.close();
                }
                if(rs != null){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return clientes;
    }
}


