/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Abe
 */
public class Pago {
    private String formaPago, cuentaBeneficiario, cuentaOrdenante, moneda, rfcCtaBen, rfcCtaOrd;
    private BigDecimal monto, tipoCambio;
    private Date fechaPago;
    private List<Documento> documentos;
    private String numOperacion, nomBancoOrdExt, tipoCadPago, certPago, cadPago, selloPago;    
    private BigDecimal pagoRetencionesIVA;
    private BigDecimal pagoRetensionesISR;
    private BigDecimal pagoRetensionesIEPS; 
    private BigDecimal pagoTrasladosBaseIVA16; 
    private BigDecimal pagoTrasladosImpuestoIVA16; 
    private BigDecimal pagoTrasladosBaseIVA8;
    private BigDecimal pagoTrasladosImpuestoIVA8; 
    private BigDecimal pagoTrasladosBaseIVA0;
    private BigDecimal pagoTrasladosImpuestoIVA0; 
    private BigDecimal pagoTrasladosBaseIVAExento; 

    public BigDecimal getPagoRetencionesIVA() {
        return pagoRetencionesIVA;
    }

    public void setPagoRetencionesIVA(BigDecimal pagoRetencionesIVA) {
        this.pagoRetencionesIVA = pagoRetencionesIVA;
    }

    public BigDecimal getPagoRetensionesISR() {
        return pagoRetensionesISR;
    }

    public void setPagoRetensionesISR(BigDecimal pagoRetensionesISR) {
        this.pagoRetensionesISR = pagoRetensionesISR;
    }

    public BigDecimal getPagoRetensionesIEPS() {
        return pagoRetensionesIEPS;
    }

    public void setPagoRetensionesIEPS(BigDecimal pagoRetensionesIEPS) {
        this.pagoRetensionesIEPS = pagoRetensionesIEPS;
    }

    public BigDecimal getPagoTrasladosBaseIVA16() {
        return pagoTrasladosBaseIVA16;
    }

    public void setPagoTrasladosBaseIVA16(BigDecimal pagoTrasladosBaseIVA16) {
        this.pagoTrasladosBaseIVA16 = pagoTrasladosBaseIVA16;
    }

    public BigDecimal getPagoTrasladosImpuestoIVA16() {
        return pagoTrasladosImpuestoIVA16;
    }

    public void setPagoTrasladosImpuestoIVA16(BigDecimal pagoTrasladosImpuestoIVA16) {
        this.pagoTrasladosImpuestoIVA16 = pagoTrasladosImpuestoIVA16;
    }

    public BigDecimal getPagoTrasladosBaseIVA8() {
        return pagoTrasladosBaseIVA8;
    }

    public void setPagoTrasladosBaseIVA8(BigDecimal pagoTrasladosBaseIVA8) {
        this.pagoTrasladosBaseIVA8 = pagoTrasladosBaseIVA8;
    }

    public BigDecimal getPagoTrasladosImpuestoIVA8() {
        return pagoTrasladosImpuestoIVA8;
    }

    public void setPagoTrasladosImpuestoIVA8(BigDecimal pagoTrasladosImpuestoIVA8) {
        this.pagoTrasladosImpuestoIVA8 = pagoTrasladosImpuestoIVA8;
    }

    public BigDecimal getPagoTrasladosBaseIVA0() {
        return pagoTrasladosBaseIVA0;
    }

    public void setPagoTrasladosBaseIVA0(BigDecimal pagoTrasladosBaseIVA0) {
        this.pagoTrasladosBaseIVA0 = pagoTrasladosBaseIVA0;
    }

    public BigDecimal getPagoTrasladosImpuestoIVA0() {
        return pagoTrasladosImpuestoIVA0;
    }

    public void setPagoTrasladosImpuestoIVA0(BigDecimal pagoTrasladosImpuestoIVA0) {
        this.pagoTrasladosImpuestoIVA0 = pagoTrasladosImpuestoIVA0;
    }

    public BigDecimal getPagoTrasladosBaseIVAExento() {
        return pagoTrasladosBaseIVAExento;
    }

    public void setPagoTrasladosBaseIVAExento(BigDecimal pagoTrasladosBaseIVAExento) {
        this.pagoTrasladosBaseIVAExento = pagoTrasladosBaseIVAExento;
    }

    public String getNumOperacion() {
        return numOperacion;
    }

    public void setNumOperacion(String numOperacion) {
        this.numOperacion = numOperacion;
    }

    public String getNomBancoOrdExt() {
        return nomBancoOrdExt;
    }

    public void setNomBancoOrdExt(String nomBancoOrdExt) {
        this.nomBancoOrdExt = nomBancoOrdExt;
    }

    public String getTipoCadPago() {
        return tipoCadPago;
    }

    public void setTipoCadPago(String tipoCadPago) {
        this.tipoCadPago = tipoCadPago;
    }

    public String getCertPago() {
        return certPago;
    }

    public void setCertPago(String certPago) {
        this.certPago = certPago;
    }

    public String getCadPago() {
        return cadPago;
    }

    public void setCadPago(String cadPago) {
        this.cadPago = cadPago;
    }

    public String getSelloPago() {
        return selloPago;
    }

    public void setSelloPago(String selloPago) {
        this.selloPago = selloPago;
    }

    
    public String getRfcCtaBen() {
        return rfcCtaBen;
    }

    public void setRfcCtaBen(String rfcCtaBen) {
        this.rfcCtaBen = rfcCtaBen;
    }

    public String getRfcCtaOrd() {
        return rfcCtaOrd;
    }

    public void setRfcCtaOrd(String rfcCtaOrd) {
        this.rfcCtaOrd = rfcCtaOrd;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getCuentaBeneficiario() {
        return cuentaBeneficiario;
    }

    public void setCuentaBeneficiario(String cuentaBeneficiario) {
        this.cuentaBeneficiario = cuentaBeneficiario;
    }

    public String getCuentaOrdenante() {
        return cuentaOrdenante;
    }

    public void setCuentaOrdenante(String cuentaOrdenante) {
        this.cuentaOrdenante = cuentaOrdenante;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
}
