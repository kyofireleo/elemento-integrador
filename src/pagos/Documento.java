/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import java.math.BigDecimal;

/**
 *
 * @author Abe
 */
public class Documento {

    private String folio, serie, metodoPago, moneda, uuid;
    private BigDecimal impSaldoInsoluto, impPagado, impSaldoAnterior, tipoCambio;
    private int numParcialidad;
    private String rfcEmisor, rfcReceptor;

    public Documento() {

    }

    public String getRfcEmisor() {
        return rfcEmisor;
    }

    public void setRfcEmisor(String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }

    public String getRfcReceptor() {
        return rfcReceptor;
    }

    public void setRfcReceptor(String rfcReceptor) {
        this.rfcReceptor = rfcReceptor;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getImpSaldoInsoluto() {
        return impSaldoInsoluto;
    }

    public void setImpSaldoInsoluto(BigDecimal impSaldoInsoluto) {
        this.impSaldoInsoluto = impSaldoInsoluto;
    }

    public BigDecimal getImpPagado() {
        return impPagado;
    }

    public void setImpPagado(BigDecimal impPagado) {
        this.impPagado = impPagado;
    }

    public BigDecimal getImpSaldoAnterior() {
        return impSaldoAnterior;
    }

    public void setImpSaldoAnterior(BigDecimal impSaldoAnterior) {
        this.impSaldoAnterior = impSaldoAnterior;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public int getNumParcialidad() {
        return numParcialidad;
    }

    public void setNumParcialidad(int numParcialidad) {
        this.numParcialidad = numParcialidad;
    }

}
