package org.example;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import entities.Cliente;
import factory.AbstractFactory;
import utils.HelperDerby;
import utils.HelperMySQL;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

        public static void main(String[] args) throws SQLException, IOException {

        System.out.println("BASE DE DATOS SQL");

        HelperMySQL dbMysql= new HelperMySQL();
        dbMysql.dropTables();
        dbMysql.createTables();
        dbMysql.populateDB();
        dbMysql.closeConnection();

        AbstractFactory MysqlFactory = AbstractFactory.getDAOFactory(1);
        ClienteDAO clienteMySQL = MysqlFactory.getClienteDAO();
        FacturaDAO facturaMySQL = MysqlFactory.getFacturaDAO();
        FacturaProductoDAO fapMySQL = MysqlFactory.getFacturaProductoDAO();
        ProductoDAO productoMySQL = MysqlFactory.getProductoDAO();

        System.out.println();
        System.out.println("////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////");
        System.out.println();

        //EJERCICIO 3
        System.out.println("Producto con mayor recaudación: "+productoMySQL.ejercicio3().toString());
        //EJERCICIO 4
        List<Cliente> salida = clienteMySQL.ejercicio4();
        for(Cliente c: salida){
            System.out.println(c.toString());
        }

        //BASE DE DATOS DERBY
        System.out.println("BASE DE DATOS DERBY - Tengo información que podría llevar al arresto de Cristina Frenandez de Kirchner");

        HelperDerby dbDerby = new HelperDerby();
        dbDerby.dropTables();
        dbDerby.createTables();
        dbDerby.populateDB();
        dbDerby.closeConnection();

        AbstractFactory DerbyFactory = AbstractFactory.getDAOFactory(2);
        ClienteDAO clienteDerby = DerbyFactory.getClienteDAO();
        FacturaDAO facturaDerby = DerbyFactory.getFacturaDAO();
        FacturaProductoDAO fapDerby = DerbyFactory.getFacturaProductoDAO();
        ProductoDAO productoDerby = DerbyFactory.getProductoDAO();

        System.out.println();
        System.out.println("////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////");
        System.out.println();

        //EJERCICIO 3
        //System.out.println("Producto con mayor recaudacion: "+productoDerby.ejercicio3().toString());
        //EJERCICIO 4
        List<Cliente> salidaDerby = clienteDerby.ejercicio4();
        for(Cliente c: salidaDerby){
            System.out.println(c.toString());
        }
    }
}
