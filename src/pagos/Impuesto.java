
package pagos;

import java.math.BigDecimal;

/**
 *
 * @author abe
 */
public class Impuesto {

    char tipo;
    String nombre;
    String impuestoId;
    String tipoFactor;
    BigDecimal tasaOCuota;
    BigDecimal base;
    BigDecimal importe;
    
    public Impuesto(){
        
    }

    public Impuesto(char tipo, String nombre, String impuestoId, String tipoFactor, BigDecimal tasaOCuota, BigDecimal base, BigDecimal importe) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.impuestoId = impuestoId;
        this.tipoFactor = tipoFactor;
        this.tasaOCuota = tasaOCuota;
        this.base = base;
        this.importe = importe;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImpuestoId() {
        return impuestoId;
    }

    public void setImpuestoId(String impuestoId) {
        this.impuestoId = impuestoId;
    }

    public String getTipoFactor() {
        return tipoFactor;
    }

    public void setTipoFactor(String tipoFactor) {
        this.tipoFactor = tipoFactor;
    }

    public BigDecimal getTasaOCuota() {
        return tasaOCuota;
    }

    public void setTasaOCuota(BigDecimal tasaOCuota) {
        this.tasaOCuota = tasaOCuota;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

}
