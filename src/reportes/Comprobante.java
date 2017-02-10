/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Abe
 */
public class Comprobante {
    private Emisor emisor;
    private Receptor receptor;
    private String folio, serie, uuid, tipoDeComprobante;
    private String subtotal, total;
    private String totalTraslados, totalRetenidos;
    private Date fechaTimbrado;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaTimbrado() {
        return fechaTimbrado;
    }

    public void setFechaTimbrado(Date fechaTimbrado) {
        this.fechaTimbrado = fechaTimbrado;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTipoDeComprobante() {
        return tipoDeComprobante;
    }

    public void setTipoDeComprobante(String tipoDeComprobante) {
        this.tipoDeComprobante = tipoDeComprobante;
    }
    
    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalTraslados() {
        return totalTraslados;
    }

    public void setTotalTraslados(String totalTraslados) {
        this.totalTraslados = totalTraslados;
    }

    public String getTotalRetenidos() {
        return totalRetenidos;
    }

    public void setTotalRetenidos(String totalRetenidos) {
        this.totalRetenidos = totalRetenidos;
    }
}
