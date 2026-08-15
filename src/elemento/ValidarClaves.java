/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elemento;

/**
 *
 * @author abe
 */
public class ValidarClaves {
    private boolean valida;
    private Object clave;
    
    public ValidarClaves(){
        
    }
    
    public ValidarClaves(boolean valida, Object clave){
        this.valida = valida;
        this.clave = clave;
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public Object getClave() {
        return clave;
    }

    public void setClave(Object clave) {
        this.clave = clave;
    }
}
