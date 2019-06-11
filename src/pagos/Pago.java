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
