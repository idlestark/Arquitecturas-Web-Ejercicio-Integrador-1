package dao;

import entities.FacturaProducto;
import entities.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductoDAO {
    private Connection con;

    public ProductoDAO(Connection con){this.con = con;}

    public void insert (Producto producto) {

        String query = "INSERT INTO Producto(idProducto, nombre, valor) VALUES(?, ?, ?)";

        PreparedStatement ps = null;

        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, producto.getIdProducto());
            ps.setString(2, producto.getNombre());
            ps.setFloat(3, producto.getValor());
            ps.executeUpdate();
            System.out.println("Producto insertado correctamente.");
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

    public void delete (int idProducto) {
        String query = "DELETE FROM producto WHERE idProducto = ?";

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idProducto);
            ps.executeUpdate();
            System.out.println("El producto " + idProducto + " se eliminó correctamente.");
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

    public void update (Producto producto){
        String query = "UPDATE INTO Producto(idFactura) WHERE idProducto = ?";
        PreparedStatement ps = null;
        try{
            ps = con.prepareStatement(query);
            ps.setInt(1, producto.getIdProducto());
            ps.executeUpdate();
            System.out.println("El producto con id: " + producto.getIdProducto() + "se actualizo correctamente");
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

    public Producto find(Integer pk) {
        String query = "SELECT * FROM Producto WHERE idProducto = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Producto salida = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, pk);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                float valor = rs.getFloat("valor");
                salida = new Producto (pk, nombre, valor);
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


    //EJERCICIO 3
    //Escriba un programa JDBC que retorne el producto que más recaudó. Se define
    //“recaudación” como cantidad de productos vendidos multiplicado por su valor.

    public Producto ejercicio3(){

        float mayor = 0;
        float temp = 0;
        int idMayor = 0;
        ArrayList<Producto> listaProductos = getProductos();

        for(Producto producto : listaProductos){
            temp = getCantidad(producto.getIdProducto()) * producto.getValor();
            if(temp > mayor){
                mayor = temp;
                idMayor = producto.getIdProducto();
            }
        }
        return find(idMayor);
    }

    public int getCantidad(int id){
        String query = "SELECT SUM(cantidad) AS cant FROM Factura-Producto WHERE idProducto = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int salida = 0;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            salida = rs.getInt("cant");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
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

    public ArrayList<Producto> getProductos(){
        String query = "SELECT * FROM Producto";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Producto> salida = new ArrayList<>();
        try{
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                float valor = rs.getFloat("valor");
                Producto p = new Producto(id, nombre, valor);
                salida.add(p);
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
        return salida;
    }

}
