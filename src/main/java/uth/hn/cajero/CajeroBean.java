package uth.hn.cajero;

import jakarta.enterprise.context.SessionScoped; 
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped  
public class CajeroBean implements Serializable {
    
    private List<Cliente> clientes;
    private String numeroCuenta;
    private String pin;
    private double monto;
    private Cliente clienteActual;
    private String mensaje;
    
    public CajeroBean() {
        clientes = LeerClientes.cargarClientes();
        System.out.println("Bean inicializado, clientes cargados: " + clientes.size());
    }
    
    //LOGIN
    public String autenticar() {
        System.out.println("Autenticando: " + numeroCuenta);
        
        for (Cliente c : clientes) {
            if (c.getNumeroCuenta().equals(numeroCuenta) && 
                c.getPin().equals(pin)) {
                clienteActual = c;
                System.out.println("Cliente autenticado: " + clienteActual.getNumeroCuenta());
                return "menu?faces-redirect=true";
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "Numero de cuenta o PIN invalidos", null));
        return null;
    }
    
    //DEPOSITO
    public String realizarDeposito() {
        System.out.println("Iniciando depósito...");
        System.out.println("clienteActual es null? " + (clienteActual == null));
        
        if (clienteActual == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Sesion expirada. Por favor inicie sesion nuevamente", null));
            return "index?faces-redirect=true";
        }
        
        if (monto <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Monto invalido. Debe ser mayor a cero", null));
            return null;
        }
        
        double nuevoSaldo = clienteActual.getSaldo() + monto;
        clienteActual.setSaldo(nuevoSaldo);
        mensaje = "Deposito exitoso. Nuevo saldo: L. " + String.format("%.2f", nuevoSaldo);
        
        System.out.println("Deposito exitoso. Nuevo saldo: " + nuevoSaldo);
        
        return "saldo?faces-redirect=true";
    }
    
    //RETIRO
    public String realizarRetiro() {
        System.out.println("Iniciando retiro...");
        
        if (clienteActual == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Sesion expirada. Por favor inicie sesion nuevamente", null));
            return "index?faces-redirect=true";
        }
        
        if (monto <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Monto invalido. Debe ser mayor que cero", null));
            return null;
        }
        
        if (clienteActual.getSaldo() < monto) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Saldo insuficiente. Su saldo es: L. " + 
                    String.format("%.2f", clienteActual.getSaldo()), null));
            return null;
        }
        
        double nuevoSaldo = clienteActual.getSaldo() - monto;
        clienteActual.setSaldo(nuevoSaldo);
        mensaje = "Retiro exitoso. Nuevo saldo: L. " + String.format("%.2f", nuevoSaldo);
        
        return "saldo?faces-redirect=true";
    }
    
    //CERRAR SESION
    public String cerrarSesion() {
        System.out.println("Cerrando sesion para: " + 
            (clienteActual != null ? clienteActual.getNumeroCuenta() : "null"));
        
        clienteActual = null;
        numeroCuenta = null;
        pin = null;
        monto = 0;
        mensaje = null;
        
        return "index?faces-redirect=true";
    }
    
    //VERIFICACION
    public void verificarSesion() {
        if (clienteActual == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("index.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Getters y Setters
    public String getNumeroCuenta() {
    	return numeroCuenta; 
    	}
    
    public void setNumeroCuenta(String numeroCuenta) {
    	this.numeroCuenta = numeroCuenta; 
    	}
    
    public String getPin() {
    	return pin; 
    	}
    
    public void setPin(String pin) {
    	this.pin = pin; 
    	}
    
    public double getMonto() {
    	return monto; 
    	}
    
    public void setMonto(double monto) {
    	this.monto = monto; 
    	}
    
    public Cliente getClienteActual() {
    	return clienteActual; 
    	}
    
    public String getMensaje() {
    	return mensaje; 
    	}
    
    public boolean isAutenticado() {
        return clienteActual != null;
    }
}