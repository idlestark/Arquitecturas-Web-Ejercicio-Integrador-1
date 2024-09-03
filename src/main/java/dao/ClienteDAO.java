package dao;

import entities.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}


