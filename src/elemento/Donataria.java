/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import java.util.Date;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Donataria {

    private Date fechaAutorizacion;
    private String noAutorizacion;
    private int idEmisor;

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getNoAutorizacion() {
        return noAutorizacion;
    }

    public void setNoAutorizacion(String noAutorizacion) {
        this.noAutorizacion = noAutorizacion;
    }

    public int getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(int idEmisor) {
        this.idEmisor = idEmisor;
    }
}
