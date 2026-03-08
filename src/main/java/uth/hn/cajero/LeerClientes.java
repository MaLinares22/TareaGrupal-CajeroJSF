package uth.hn.cajero;

import java.io.*;
import java.util.*;

public class LeerClientes {
    
    public static List<Cliente> cargarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        
        try (InputStream is = LeerClientes.class.getClassLoader()
                .getResourceAsStream("clientes.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                String[] datos = linea.split(",");
                if (datos.length == 3) {
                    String numeroCuenta = datos[0].trim();
                    String pin = datos[1].trim();
                    double saldo = Double.parseDouble(datos[2].trim());
                    clientes.add(new Cliente(numeroCuenta, pin, saldo));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Caso no encuentra el archivo
            clientes.add(new Cliente("1001", "1234", 5000.00));
            clientes.add(new Cliente("1002", "2345", 3500.50));
            clientes.add(new Cliente("1003", "3456", 10000.00));
            clientes.add(new Cliente("1004", "4567", 2500.75));
            clientes.add(new Cliente("1005", "5678", 7200.25));
        }
        
        return clientes;
    }
}