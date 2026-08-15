/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import java.math.BigDecimal;
import java.util.List;
import utils.cfdi.Comprobante;

/**
 *
 * @author Abe
 */
public class Documento {

    private String folio, serie, metodoPago, moneda, uuid;
    private BigDecimal impSaldoInsoluto, impPagado, impSaldoAnterior, tipoCambio;
    private int numParcialidad;
    private String rfcEmisor, rfcReceptor;
    private BigDecimal 
	retencionIVA
        ,retencionTasaIVA
	,retensionISR
        ,retensionTasaISR
	,retensionIEPS
        ,retensionTasaIEPS
	,trasladoBaseIVA16 
	,trasladoImpuestoIVA16 
	,trasladoBaseIVA8 
	,trasladoImpuestoIVA8 
	,trasladoBaseIVA0 
	,trasladoImpuestoIVA0 
	,trasladoBaseIVAExento;

    private List<Impuesto> impuestos;
    private String objImp;
    private BigDecimal equivalencia;
    private Comprobante comp;
    
    public Documento() {

    }
    
    public BigDecimal getRetencionTasaIVA() {
        return retencionTasaIVA;
    }

    public void setRetencionTasaIVA(BigDecimal retencionTasaIVA) {
        this.retencionTasaIVA = retencionTasaIVA;
    }

    public BigDecimal getRetensionTasaISR() {
        return retensionTasaISR;
    }

    public void setRetensionTasaISR(BigDecimal retensionTasaISR) {
        this.retensionTasaISR = retensionTasaISR;
    }

    public BigDecimal getRetensionTasaIEPS() {
        return retensionTasaIEPS;
    }

    public void setRetensionTasaIEPS(BigDecimal retensionTasaIEPS) {
        this.retensionTasaIEPS = retensionTasaIEPS;
    }

    public List<Impuesto> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<Impuesto> impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(BigDecimal equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getObjImp() {
        return objImp;
    }

    public void setObjImp(String objImp) {
        this.objImp = objImp;
    }

    public BigDecimal getRetencionIVA() {
        return retencionIVA;
    }

    public void setRetencionIVA(BigDecimal retencionIVA) {
        this.retencionIVA = retencionIVA;
    }

    public BigDecimal getRetensionISR() {
        return retensionISR;
    }

    public void setRetensionISR(BigDecimal retensionISR) {
        this.retensionISR = retensionISR;
    }

    public BigDecimal getRetensionIEPS() {
        return retensionIEPS;
    }

    public void setRetensionIEPS(BigDecimal retensionIEPS) {
        this.retensionIEPS = retensionIEPS;
    }

    public BigDecimal getTrasladoBaseIVA16() {
        return trasladoBaseIVA16;
    }

    public void setTrasladoBaseIVA16(BigDecimal trasladoBaseIVA16) {
        this.trasladoBaseIVA16 = trasladoBaseIVA16;
    }

    public BigDecimal getTrasladoImpuestoIVA16() {
        return trasladoImpuestoIVA16;
    }

    public void setTrasladoImpuestoIVA16(BigDecimal trasladoImpuestoIVA16) {
        this.trasladoImpuestoIVA16 = trasladoImpuestoIVA16;
    }

    public BigDecimal getTrasladoBaseIVA8() {
        return trasladoBaseIVA8;
    }

    public void setTrasladoBaseIVA8(BigDecimal trasladoBaseIVA8) {
        this.trasladoBaseIVA8 = trasladoBaseIVA8;
    }

    public BigDecimal getTrasladoImpuestoIVA8() {
        return trasladoImpuestoIVA8;
    }

    public void setTrasladoImpuestoIVA8(BigDecimal trasladoImpuestoIVA8) {
        this.trasladoImpuestoIVA8 = trasladoImpuestoIVA8;
    }

    public BigDecimal getTrasladoBaseIVA0() {
        return trasladoBaseIVA0;
    }

    public void setTrasladoBaseIVA0(BigDecimal trasladoBaseIVA0) {
        this.trasladoBaseIVA0 = trasladoBaseIVA0;
    }

    public BigDecimal getTrasladoImpuestoIVA0() {
        return trasladoImpuestoIVA0;
    }

    public void setTrasladoImpuestoIVA0(BigDecimal trasladoImpuestoIVA0) {
        this.trasladoImpuestoIVA0 = trasladoImpuestoIVA0;
    }

    public BigDecimal getTrasladoBaseIVAExento() {
        return trasladoBaseIVAExento;
    }

    public void setTrasladoBaseIVAExento(BigDecimal trasladoBaseIVAExento) {
        this.trasladoBaseIVAExento = trasladoBaseIVAExento;
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

    public void setComprobante(Comprobante comp) {
        this.comp = comp;
    }
    
    public Comprobante getComprobante(){
        return this.comp;
    }
}