/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pagos;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author esque
 */
public class Pagos {
    private BigDecimal totalMontoPagos;
    private BigDecimal totalRetencionesIVA;
    private BigDecimal totalRetensionesISR;
    private BigDecimal totalRetensionesIEPS; 
    private BigDecimal totalTrasladosBaseIVA16; 
    private BigDecimal totalTrasladosImpuestoIVA16; 
    private BigDecimal totalTrasladosBaseIVA8;
    private BigDecimal totalTrasladosImpuestoIVA8; 
    private BigDecimal totalTrasladosBaseIVA0;
    private BigDecimal totalTrasladosImpuestoIVA0; 
    private BigDecimal totalTrasladosBaseIVAExento; 
    private List<Pago> pagos;

    public BigDecimal getTotalMontoPagos() {
        return totalMontoPagos;
    }

    public void setTotalMontoPagos(BigDecimal totalMontoPagos) {
        this.totalMontoPagos = totalMontoPagos;
    }

    public BigDecimal getTotalRetencionesIVA() {
        return totalRetencionesIVA;
    }

    public void setTotalRetencionesIVA(BigDecimal totalRetencionesIVA) {
        this.totalRetencionesIVA = totalRetencionesIVA;
    }

    public BigDecimal getTotalRetensionesISR() {
        return totalRetensionesISR;
    }

    public void setTotalRetensionesISR(BigDecimal totalRetensionesISR) {
        this.totalRetensionesISR = totalRetensionesISR;
    }

    public BigDecimal getTotalRetensionesIEPS() {
        return totalRetensionesIEPS;
    }

    public void setTotalRetensionesIEPS(BigDecimal totalRetensionesIEPS) {
        this.totalRetensionesIEPS = totalRetensionesIEPS;
    }

    public BigDecimal getTotalTrasladosBaseIVA16() {
        return totalTrasladosBaseIVA16;
    }

    public void setTotalTrasladosBaseIVA16(BigDecimal totalTrasladosBaseIVA16) {
        this.totalTrasladosBaseIVA16 = totalTrasladosBaseIVA16;
    }

    public BigDecimal getTotalTrasladosImpuestoIVA16() {
        return totalTrasladosImpuestoIVA16;
    }

    public void setTotalTrasladosImpuestoIVA16(BigDecimal totalTrasladosImpuestoIVA16) {
        this.totalTrasladosImpuestoIVA16 = totalTrasladosImpuestoIVA16;
    }

    public BigDecimal getTotalTrasladosBaseIVA8() {
        return totalTrasladosBaseIVA8;
    }

    public void setTotalTrasladosBaseIVA8(BigDecimal totalTrasladosBaseIVA8) {
        this.totalTrasladosBaseIVA8 = totalTrasladosBaseIVA8;
    }

    public BigDecimal getTotalTrasladosImpuestoIVA8() {
        return totalTrasladosImpuestoIVA8;
    }

    public void setTotalTrasladosImpuestoIVA8(BigDecimal totalTrasladosImpuestoIVA8) {
        this.totalTrasladosImpuestoIVA8 = totalTrasladosImpuestoIVA8;
    }

    public BigDecimal getTotalTrasladosBaseIVA0() {
        return totalTrasladosBaseIVA0;
    }

    public void setTotalTrasladosBaseIVA0(BigDecimal totalTrasladosBaseIVA0) {
        this.totalTrasladosBaseIVA0 = totalTrasladosBaseIVA0;
    }

    public BigDecimal getTotalTrasladosImpuestoIVA0() {
        return totalTrasladosImpuestoIVA0;
    }

    public void setTotalTrasladosImpuestoIVA0(BigDecimal totalTrasladosImpuestoIVA0) {
        this.totalTrasladosImpuestoIVA0 = totalTrasladosImpuestoIVA0;
    }

    public BigDecimal getTotalTrasladosBaseIVAExento() {
        return totalTrasladosBaseIVAExento;
    }

    public void setTotalTrasladosBaseIVAExento(BigDecimal totalTrasladosBaseIVAExento) {
        this.totalTrasladosBaseIVAExento = totalTrasladosBaseIVAExento;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
    
    
}
