/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas;

import java.math.BigDecimal;
import java.util.Date;
import utils.cfdi.Receptor;

/**
 *
 * @author Abe
 */
public class Empleado {
    private complementos.nominas.Empleado emp;
    private int idEmpleado;
    private Receptor receptor;

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }
    
    public Empleado(){
        emp = new complementos.nominas.Empleado();
    }
    
    public complementos.nominas.Empleado getEmpleado(){
        return emp;
    }
    
    public BigDecimal getSalarioDiarioInt() {
        return emp.getSalarioDiarioInt();
    }

    public void setSalarioDiarioInt(BigDecimal salarioDiarioInt) {
        emp.setSalarioDiarioInt(salarioDiarioInt);
    }

    public BigDecimal getSalarioBaseCotApor() {
        return emp.getSalarioBaseCotApor();
    }

    public void setSalarioBaseCotApor(BigDecimal salarioBaseCotApor) {
        emp.setSalarioBaseCotApor(salarioBaseCotApor);
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCurp() {
        return emp.getCurp();
    }

    public void setCurp(String curp) {
        emp.setCurp(curp);
    }

    public String getTipoRegimen() {
        return emp.getTipoRegimen();
    }

    public void setTipoRegimen(String tipoRegimen) {
        emp.setTipoRegimen(tipoRegimen);
    }

    public String getNss() {
        return emp.getNss();
    }

    public void setNss(String nss) {
        emp.setNss(nss);
    }

    public String getDepartamento() {
        return emp.getDepartamento();
    }

    public void setDepartamento(String departamento) {
        emp.setDepartamento(departamento);
    }

    public String getClabe() {
        return emp.getClabe();
    }

    public void setClabe(String clabe) {
        emp.setClabe(clabe);
    }

    public String getBanco() {
        return emp.getBanco();
    }

    public void setBanco(String banco) {
        emp.setBanco(banco);
    }

    public String getPuesto() {
        return emp.getPuesto();
    }

    public void setPuesto(String puesto) {
        emp.setPuesto(puesto);
    }

    public String getTipoContrato() {
        return emp.getTipoContrato();
    }

    public void setTipoContrato(String tipoContrato) {
        emp.setTipoContrato(tipoContrato);
    }

    public String getTipoJornada() {
        return emp.getTipoJornada();
    }

    public void setTipoJornada(String tipoJornada) {
        emp.setTipoJornada(tipoJornada);
    }

    public String getPeriodicidadPago() {
        return emp.getPeriodicidadPago();
    }

    public void setPeriodicidadPago(String periodicidadPago) {
        emp.setPeriodicidadPago(periodicidadPago);
    }

    public String getRiesgoPuesto() {
        return emp.getRiesgoPuesto();
    }

    public void setRiesgoPuesto(String riesgoPuesto) {
        emp.setRiesgoPuesto(riesgoPuesto);
    }

    public String getNumEmpleado() {
        return emp.getNumEmpleado();
    }

    public void setNumEmpleado(String numEmpleado) {
        emp.setNumEmpleado(numEmpleado);
    }

    public Date getFechaInicialRelLaboral() {
        return emp.getFechaInicialRelLaboral();
    }

    public void setFechaInicialRelLaboral(Date fechaInicialRelLaboral) {
        emp.setFechaInicialRelLaboral(fechaInicialRelLaboral);
    }
}
