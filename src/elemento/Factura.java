/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import java.math.BigDecimal;
import java.util.List;

public class Factura {

    public String nombre, rfc, calle, colonia, numExt, numInt, cp, localidad, municipio, estado, pais; //Datos cliente

    public String serie, folio, tipoCfd, formaPago, metodoPago, motivoDescuento, cuentaBancaria, lugarExpedicion;   //Datos CFDi
    public double subtotal, descuento, iva = 0, total, totalIeps = 0, totalRetenidos, totalTraslados, porIva, porRetenidos, porTraslados;
    public double ivaRetenido, isrRetenido, porIeps;
    public String moneda, tipoCambio, leyenda, condicionPago;
    public int idEmpleado;
    public String usoCfdi;
    public String cfdisAsociados;
    public String tipoRelacion;

    public String regimenFiscalReceptor;
    public String numRegIdTrib;

    public List<String> descripcion, unidad;    //Datos conceptos
    public List<Double> cantidad, precio, importe;
    public List<String> conceptos;
    public List<ConceptoTraslado> traslados;
    public List<ConceptoRetencion> retenciones;

    public String numEmpleado;

    public String prefactura;

    public Emisor emisor;
    private Donataria donat;

    public Factura() {

    }

    public String getRegimenFiscalReceptor() {
        return regimenFiscalReceptor;
    }

    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }

    public List<ConceptoTraslado> getTraslados() {
        return traslados;
    }

    public List<ConceptoRetencion> getRetenciones() {
        return retenciones;
    }

    public double getPorIeps() {
        return porIeps;
    }

    public String getCondicionPago() {
        return condicionPago;
    }

    public String getPrefactura() {
        return prefactura;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public double getIsrRetenido() {
        return isrRetenido;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    public Emisor getEmisor() {
        return emisor;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public double getIvaRetenido() {
        return ivaRetenido;
    }

    public void setIvaRetenido(double ivaRetenido) {
        this.ivaRetenido = ivaRetenido;
    }

    public List<String> getConceptos() {
        return conceptos;
    }

    public String getCalle() {
        return calle;
    }

    public List<Double> getCantidad() {
        return cantidad;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCp() {
        return cp;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public List<String> getDescripcion() {
        return descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public String getEstado() {
        return estado;
    }

    public String getFolio() {
        return folio;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public List<Double> getImporte() {
        return importe;
    }

    public double getIva() {
        return iva;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getMotivoDescuento() {
        return motivoDescuento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumExt() {
        return numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public String getPais() {
        return pais;
    }

    public double getPorIva() {
        return porIva;
    }

    public double getPorRetenidos() {
        return porRetenidos;
    }

    public double getPorTraslados() {
        return porTraslados;
    }

    public List<Double> getPrecio() {
        return precio;
    }

    public String getRfc() {
        return rfc;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String getTipoCfd() {
        return tipoCfd;
    }

    public double getTotal() {
        return total;
    }

    public double getTotalIeps() {
        return totalIeps;
    }

    public double getTotalRetenidos() {
        return totalRetenidos;
    }

    public double getTotalTraslados() {
        return totalTraslados;
    }

    public List<String> getUnidad() {
        return unidad;
    }

    public void setDonataria(Donataria donat) {
        this.donat = donat;
    }

    public Donataria getDonataria() {
        return donat;
    }

    public String getUsoCfdi() {
        return usoCfdi;
    }

    public class ConceptoTraslado {

        private int numConcepto;
        private BigDecimal base, tasa, importe;
        private String impuesto, tipoFactor;

        public ConceptoTraslado() {

        }

        public int getNumConcepto() {
            return numConcepto;
        }

        public void setNumConcepto(int numConcepto) {
            this.numConcepto = numConcepto;
        }

        public BigDecimal getBase() {
            return base;
        }

        public void setBase(BigDecimal base) {
            this.base = base;
        }

        public BigDecimal getTasa() {
            return tasa;
        }

        public void setTasa(BigDecimal tasa) {
            this.tasa = tasa;
        }

        public BigDecimal getImporte() {
            return importe;
        }

        public void setImporte(BigDecimal importe) {
            this.importe = importe;
        }

        public String getImpuesto() {
            return impuesto;
        }

        public void setImpuesto(String impuesto) {
            this.impuesto = impuesto;
        }

        public String getTipoFactor() {
            return tipoFactor;
        }

        public void setTipoFactor(String tipoFactor) {
            this.tipoFactor = tipoFactor;
        }

    }

    public class ConceptoRetencion {

        private int numConcepto;
        private BigDecimal base, tasa, importe;
        private String impuesto, tipoFactor;

        public ConceptoRetencion() {

        }

        public int getNumConcepto() {
            return numConcepto;
        }

        public void setNumConcepto(int numConcepto) {
            this.numConcepto = numConcepto;
        }

        public BigDecimal getBase() {
            return base;
        }

        public void setBase(BigDecimal base) {
            this.base = base;
        }

        public BigDecimal getTasa() {
            return tasa;
        }

        public void setTasa(BigDecimal tasa) {
            this.tasa = tasa;
        }

        public BigDecimal getImporte() {
            return importe;
        }

        public void setImporte(BigDecimal importe) {
            this.importe = importe;
        }

        public String getImpuesto() {
            return impuesto;
        }

        public void setImpuesto(String impuesto) {
            this.impuesto = impuesto;
        }

        public String getTipoFactor() {
            return tipoFactor;
        }

        public void setTipoFactor(String tipoFactor) {
            this.tipoFactor = tipoFactor;
        }

    }
}
