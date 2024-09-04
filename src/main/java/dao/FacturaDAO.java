package dao;

import entities.Cliente;
import entities.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class FacturaDAO {
    private Connection con;
    public FacturaDAO(Connection con){this.con = con;}

    public void insert (Factura factura) {

        String query = "INSERT INTO Factura(idFactura, idCliente) VALUES(?, ?)";

        PreparedStatement ps = null;

        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, factura.getIdFactura());
            ps.setInt(2, factura.getIdCliente());
            ps.executeUpdate();
            System.out.println("Factura insertada correctamente.");
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

    public void delete (int idFactura) {
        String query = "DELETE FROM factura WHERE idFactura = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idFactura);
            ps.executeUpdate();
            System.out.println("El cliente " + idFactura + " se eliminó correctamente.");
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

    public void update (Factura factura){
        String query = "UPDATE INTO Factura(idCliente) WHERE idFactura = ?";
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, factura.getIdCliente());
            ps.setInt(2, factura.getIdFactura());
            ps.executeUpdate();
            System.out.println("La factura con la id: " + factura.getIdFactura() + "se actualizo correctamente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
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

    public Factura find(Integer pk) {
        String query = "SELECT * FROM Factura WHERE idFactura = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Factura salida = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, pk);
            rs = ps.executeQuery();
            if (rs.next()) {
                int idCliente = rs.getInt("idCliente");
                salida = new Factura (pk, idCliente);
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


