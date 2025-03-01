/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elemento;

import javax.swing.JFrame;

/**
 *
 * @author esque
 */
public class ClavesProdUniSat extends JFrame{
    public String claveSat;
    public Integer idClaveSat;
    public String claveUnidadSat;
    public Integer idClaveUnidadSat;
    public String descripcionUnidad;

    public String getDescripcion() {
        return descripcionUnidad;
    }

    public void setDescripcionUnidad(String descripcion) {
        this.descripcionUnidad = descripcion;
    }

    public String getClaveSat() {
        return claveSat;
    }

    public void setClaveSat(String claveSat) {
        this.claveSat = claveSat;
    }

    public Integer getIdClaveSat() {
        return idClaveSat;
    }

    public void setIdClaveSat(Integer idClaveSat) {
        this.idClaveSat = idClaveSat;
    }

    public String getClaveUnidadSat() {
        return claveUnidadSat;
    }

    public void setClaveUnidadSat(String claveUnidadSat) {
        this.claveUnidadSat = claveUnidadSat;
    }

    public Integer getIdClaveUnidadSat() {
        return idClaveUnidadSat;
    }

    public void setIdClaveUnidadSat(Integer idClaveUnidadSat) {
        this.idClaveUnidadSat = idClaveUnidadSat;
    }
    
    
}
