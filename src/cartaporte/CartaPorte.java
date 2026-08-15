package cartaporte;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mx.grupocorasa.sat.common.catalogos.CClaveUnidad;
import mx.grupocorasa.sat.common.catalogos.CMoneda;
import mx.grupocorasa.sat.common.catalogos.CPais;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CClaveUnidadPeso;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CCondicionesEspeciales;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CConfigAutotransporte;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CCveTransporte;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CFiguraTransporte;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CSectorCOFEPRIS;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CSubTipoRem;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CTipoEmbalaje;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CTipoEstacion;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CTipoMateria;
import mx.grupocorasa.sat.common.catalogos.CartaPorte.CTipoPermiso;

/**
 *
 * @author abe
 */
public class CartaPorte {

    private String idCCP;
    private BigDecimal totalDistRec;
    private FiguraTransporte figuraTransporte;
    private Ubicaciones ubicaciones;
    private Mercancias mercancias;

    public CartaPorte() {

    }

    public Mercancias getMercancias() {
        return mercancias;
    }

    public void setMercancias(Mercancias mercancias) {
        this.mercancias = mercancias;
    }

    public String getIdCCP() {
        return idCCP;
    }

    public void setIdCCP(String idCCP) {
        this.idCCP = idCCP;
    }

    public BigDecimal getTotalDistRec() {
        return totalDistRec;
    }

    public void setTotalDistRec(BigDecimal totalDistRec) {
        this.totalDistRec = totalDistRec;
    }

    public FiguraTransporte getFiguraTransporte() {
        return figuraTransporte;
    }

    public void setFiguraTransporte(FiguraTransporte figuraTransporte) {
        this.figuraTransporte = figuraTransporte;
    }

    public Ubicaciones getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(Ubicaciones ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public void setCartaPorte() {
        String value = null;
        CartaPorte cp = new CartaPorte();

        cp.setIdCCP(value);
        cp.setTotalDistRec(BigDecimal.ONE);

        /**
         * ****************FiguraTransporte******************
         */
        figuraTransporte = new FiguraTransporte();

        //TiposFigura
        FiguraTransporte.TiposFigura tiposFigura = figuraTransporte.new TiposFigura();
        tiposFigura.setNombreFigura(value);
        tiposFigura.setNumLicencia(value);
        tiposFigura.setRFCFigura(value);
        tiposFigura.setResidenciaFiscalFigura("MEX");
        //tiposFigura.setTipoFigura(CFiguraTransporte.VALUE_1); //revisar que es y de donde se obtiene

        //DomicilioFigura
        FiguraTransporte.TiposFigura.Domicilio dom = tiposFigura.new Domicilio();
        dom.setCalle(value);
        dom.setNumeroExterior(value);
        dom.setNumeroInterior(value);
        dom.setColonia(value);
        dom.setCodigoPostal(value);
        dom.setLocalidad(value);
        dom.setMunicipio(value);
        dom.setEstado(value);
        dom.setPais("MEX");
        dom.setReferencia(value);

        tiposFigura.setDomicilio(dom); //Setear domicilio
        figuraTransporte.getTiposFigura().add(tiposFigura); //Add TipoFigura

        //cp.setFiguraTransporte(figuraTransporte); //Setear FiguraTransporte
        /**
         * ****************Mercancias******************
         */
        Mercancias mercancias = new Mercancias();
        mercancias.setCargoPorTasacion(BigDecimal.ONE);
        mercancias.setLogisticaInversaRecoleccionDevolucion(value);
        mercancias.setNumTotalMercancias(0);
        mercancias.setPesoBrutoTotal(BigDecimal.ONE);
        mercancias.setPesoNetoTotal(BigDecimal.ONE);
        mercancias.setUnidadPeso("VALUE_1");
        
        //Mercancia
        Mercancias.Mercancia merca = mercancias.new Mercancia();
        merca.setCantidad(BigDecimal.ONE);
        merca.setClaveSTCC(value);
        merca.setClaveUnidad("VALUE_1");
        merca.setUnidad(value);
        merca.setDescripcion(value);
        merca.setValorMercancia(BigDecimal.ONE);
        
        merca.setBienesTransp(value);
        merca.setCondicionesEspTransp("VALUE_1");
        merca.setCveMaterialPeligroso(value);
        merca.setDatosFabricante(value);
        merca.setDatosFormulador(value);
        merca.setDatosMaquilador(value);
        merca.setDenominacionDistintivaProd(value);
        merca.setDenominacionGenericaProd(value);
        merca.setDescripEmbalaje(value);
        merca.setDescripcionMateria(value);
        merca.setDimensiones(value);
        merca.setEmbalaje("VALUE_1");
        merca.setFabricante(value);
        merca.setFechaCaducidad(LocalDate.MAX);
        merca.setFolioImpoVUCEM(value);
        merca.setMoneda("MXN");
        merca.setNumCAS(value);
        merca.setNumRegSanPlagCOFEPRIS(value);
        merca.setPesoEnKg(totalDistRec);
        merca.setRazonSocialEmpImp(value);
        merca.setSectorCOFEPRIS("VALUE_1");
        merca.setTipoMateria("VALUE_1");
        merca.setUsoAutorizado(value);
        
        //CantidadTransporta
        Mercancias.Mercancia.CantidadTransporta cantidadTrans = merca.new CantidadTransporta();
        cantidadTrans.setCantidad(BigDecimal.ONE);
        cantidadTrans.setCvesTransporte("VALUE_1");
        cantidadTrans.setIDOrigen(value);
        cantidadTrans.setIDDestino(value);
        merca.getCantidadTransporta().add(cantidadTrans);
        
        //GuiasIdentificacion
        Mercancias.Mercancia.GuiasIdentificacion guias = merca.new GuiasIdentificacion();
        guias.setDescripGuiaIdentificacion(value);
        guias.setNumeroGuiaIdentificacion(value);
        guias.setPesoGuiaIdentificacion(totalDistRec);
        merca.getGuiasIdentificacion().add(guias);
        
        //Detalle
        Mercancias.Mercancia.DetalleMercancia detalle = merca.new DetalleMercancia();
        detalle.setNumPiezas(Integer.MIN_VALUE);
        detalle.setPesoBruto(totalDistRec);
        detalle.setPesoNeto(totalDistRec);
        detalle.setPesoTara(totalDistRec);
        detalle.setUnidadPesoMerc("VALUE_1");
        merca.setDetalleMercancia(detalle);
        
        //Autotransporte
        Mercancias.Autotransporte auto = mercancias.new Autotransporte();
        auto.setNumPermisoSCT(value);
        auto.setPermSCT("TPAF_01");
        //Seguros
        Mercancias.Autotransporte.Seguros seguro = auto.new Seguros();
        seguro.setAseguraCarga(value);
        seguro.setAseguraMedAmbiente(value);
        seguro.setAseguraRespCivil(value);
        seguro.setPolizaCarga(value);
        seguro.setPolizaMedAmbiente(value);
        seguro.setPolizaRespCivil(value);
        seguro.setPrimaSeguro(BigDecimal.ONE);
        auto.setSeguros(seguro);
        //Remolques
        Mercancias.Autotransporte.Remolques remolques = auto.new Remolques();
        Mercancias.Autotransporte.Remolques.Remolque remolque = remolques.new Remolque();
        remolque.setPlaca(value);
        remolque.setSubTipoRem("CTR_001");
        remolques.getRemolques().add(remolque);
        auto.setRemolques(remolques);
        //IdentificacionVehicular
        Mercancias.Autotransporte.IdentificacionVehicular idVehiculo = auto.new IdentificacionVehicular();
        idVehiculo.setAnioModeloVM(0);
        idVehiculo.setConfigVehicular("VL");
        idVehiculo.setPesoBrutoVehicular(BigDecimal.ONE);
        idVehiculo.setPlacaVM(value);
        auto.setIdentificacionVehicular(idVehiculo);

        mercancias.setAutotransporte(auto);
        cp.setMercancias(mercancias);

        /**
         * ****************Ubicaciones******************
         */
        Ubicaciones ubicaciones = new Ubicaciones();
        Ubicaciones.Ubicacion ubicacion = ubicaciones.new Ubicacion();

        ubicacion.setTipoUbicacion(value);
        ubicacion.setResidenciaFiscal("MEX");
        ubicacion.setTipoEstacion("VALUE_1");
        ubicacion.setNumEstacion(value);
        ubicacion.setNombreEstacion(value);
        ubicacion.setRFCRemitenteDestinatario(value);
        ubicacion.setNombreRemitenteDestinatario(value);
        //Domicilio
        Ubicaciones.Ubicacion.Domicilio ubicacionDomicilio = ubicacion.new Domicilio();
        ubicacionDomicilio.setCalle(value);
        ubicacionDomicilio.setNumeroExterior(value);
        ubicacionDomicilio.setNumeroInterior(value);
        ubicacionDomicilio.setColonia(value);
        ubicacionDomicilio.setCodigoPostal(value);
        ubicacionDomicilio.setLocalidad(value);
        ubicacionDomicilio.setMunicipio(value);
        ubicacionDomicilio.setEstado(value);
        ubicacionDomicilio.setPais("MEX");
        ubicacionDomicilio.setReferencia(value);
        ubicacion.setDomicilio(ubicacionDomicilio);

        ubicaciones.getUbicaciones().add(ubicacion);

        cp.setUbicaciones(ubicaciones);

    }

    public class FiguraTransporte {

        private List<TiposFigura> tiposFigura;

        public FiguraTransporte() {
            tiposFigura = new ArrayList();
        }

        public List<TiposFigura> getTiposFigura() {
            return tiposFigura;
        }

        public class TiposFigura {

            private String nombreFigura,
                    numLicencia,
                    RFCFigura,
                    residenciaFiscalFigura,
                    tipoFigura;

            private Domicilio domicilio;

            public String getNombreFigura() {
                return nombreFigura;
            }

            public void setNombreFigura(String nombreFigura) {
                this.nombreFigura = nombreFigura;
            }

            public String getNumLicencia() {
                return numLicencia;
            }

            public void setNumLicencia(String numLicencia) {
                this.numLicencia = numLicencia;
            }

            public String getRFCFigura() {
                return RFCFigura;
            }

            public void setRFCFigura(String RFCFigura) {
                this.RFCFigura = RFCFigura;
            }

            public String getResidenciaFiscalFigura() {
                return residenciaFiscalFigura;
            }

            public void setResidenciaFiscalFigura(String residenciaFiscalFigura) {
                this.residenciaFiscalFigura = residenciaFiscalFigura;
            }

            public String getTipoFigura() {
                return tipoFigura;
            }

            public void setTipoFigura(String tipoFigura) {
                this.tipoFigura = tipoFigura;
            }

            public Domicilio getDomicilio() {
                return domicilio;
            }

            public void setDomicilio(Domicilio domicilio) {
                this.domicilio = domicilio;
            }

            public class Domicilio {

                private String calle,
                        numeroExterior,
                        numeroInterior,
                        colonia,
                        codigoPostal,
                        localidad,
                        municipio,
                        estado,
                        pais,
                        referencia;

                public String getCalle() {
                    return calle;
                }

                public void setCalle(String calle) {
                    this.calle = calle;
                }

                public String getNumeroExterior() {
                    return numeroExterior;
                }

                public void setNumeroExterior(String numeroExterior) {
                    this.numeroExterior = numeroExterior;
                }

                public String getNumeroInterior() {
                    return numeroInterior;
                }

                public void setNumeroInterior(String numeroInterior) {
                    this.numeroInterior = numeroInterior;
                }

                public String getColonia() {
                    return colonia;
                }

                public void setColonia(String colonia) {
                    this.colonia = colonia;
                }

                public String getCodigoPostal() {
                    return codigoPostal;
                }

                public void setCodigoPostal(String codigoPostal) {
                    this.codigoPostal = codigoPostal;
                }

                public String getLocalidad() {
                    return localidad;
                }

                public void setLocalidad(String localidad) {
                    this.localidad = localidad;
                }

                public String getMunicipio() {
                    return municipio;
                }

                public void setMunicipio(String municipio) {
                    this.municipio = municipio;
                }

                public String getEstado() {
                    return estado;
                }

                public void setEstado(String estado) {
                    this.estado = estado;
                }

                public String getPais() {
                    return pais;
                }

                public void setPais(String pais) {
                    this.pais = pais;
                }

                public String getReferencia() {
                    return referencia;
                }

                public void setReferencia(String referencia) {
                    this.referencia = referencia;
                }

            }
        }
    }
    
    public class Mercancias{
        private List<Mercancia> mercancias;
        private Autotransporte autotransporte;
        
        private BigDecimal 
                cargoPorTasacion
                ,pesoBrutoTotal
                ,pesoNetoTotal;
        
        private int numTotalMercancias;
        
        private String 
                logisticaInversaRecoleccionDevolucion
                ,unidadPeso;
        
        public Mercancias(){
            mercancias = new ArrayList();
        }

        public Autotransporte getAutotransporte() {
            return autotransporte;
        }

        public void setAutotransporte(Autotransporte autotransporte) {
            this.autotransporte = autotransporte;
        }

        public BigDecimal getCargoPorTasacion() {
            return cargoPorTasacion;
        }

        public void setCargoPorTasacion(BigDecimal cargoPorTasacion) {
            this.cargoPorTasacion = cargoPorTasacion;
        }

        public BigDecimal getPesoBrutoTotal() {
            return pesoBrutoTotal;
        }

        public void setPesoBrutoTotal(BigDecimal pesoBrutoTotal) {
            this.pesoBrutoTotal = pesoBrutoTotal;
        }

        public BigDecimal getPesoNetoTotal() {
            return pesoNetoTotal;
        }

        public void setPesoNetoTotal(BigDecimal pesoNetoTotal) {
            this.pesoNetoTotal = pesoNetoTotal;
        }

        public int getNumTotalMercancias() {
            return numTotalMercancias;
        }

        public void setNumTotalMercancias(int numTotalMercancias) {
            this.numTotalMercancias = numTotalMercancias;
        }

        public String getLogisticaInversaRecoleccionDevolucion() {
            return logisticaInversaRecoleccionDevolucion;
        }

        public void setLogisticaInversaRecoleccionDevolucion(String logisticaInversaRecoleccionDevolucion) {
            this.logisticaInversaRecoleccionDevolucion = logisticaInversaRecoleccionDevolucion;
        }

        public String getUnidadPeso() {
            return unidadPeso;
        }

        public void setUnidadPeso(String unidadPeso) {
            this.unidadPeso = unidadPeso;
        }

        public List<Mercancia> getMercancias() {
            return mercancias;
        }
        
        public class Mercancia{
            private BigDecimal 
                    cantidad
                    ,valorMercancia
                    ,pesoEnKg;

            private LocalDate fechaCaducidad;

            private String 
                    claveSTCC
                    ,claveUnidad
                    ,unidad
                    ,descripcion
                    ,bienesTransp
                    ,condicionesEspTransp
                    ,cveMaterialPeligroso
                    ,datosFabricante
                    ,datosFormulador
                    ,datosMaquilador
                    ,denominacionDistintivaProd
                    ,denominacionGenericaProd
                    ,descripEmbalaje
                    ,descripcionMateria
                    ,dimensiones
                    ,embalaje
                    ,fabricante
                    ,folioImpoVUCEM
                    ,moneda
                    ,numCAS
                    ,numRegSanPlagCOFEPRIS
                    ,razonSocialEmpImp
                    ,sectorCOFEPRIS
                    ,tipoMateria
                    ,usoAutorizado;
            
            private DetalleMercancia detalleMercancia;
            
            private List<CantidadTransporta> cantidadTransporta;
            private List<GuiasIdentificacion> guiasIdentificacion;
            
            public Mercancia(){
                cantidadTransporta = new ArrayList();
                guiasIdentificacion = new ArrayList();
            }

            public List<CantidadTransporta> getCantidadTransporta() {
                return cantidadTransporta;
            }

            public List<GuiasIdentificacion> getGuiasIdentificacion() {
                return guiasIdentificacion;
            }

            public BigDecimal getCantidad() {
                return cantidad;
            }

            public void setCantidad(BigDecimal cantidad) {
                this.cantidad = cantidad;
            }

            public BigDecimal getValorMercancia() {
                return valorMercancia;
            }

            public void setValorMercancia(BigDecimal valorMercancia) {
                this.valorMercancia = valorMercancia;
            }

            public BigDecimal getPesoEnKg() {
                return pesoEnKg;
            }

            public void setPesoEnKg(BigDecimal pesoEnKg) {
                this.pesoEnKg = pesoEnKg;
            }

            public LocalDate getFechaCaducidad() {
                return fechaCaducidad;
            }

            public void setFechaCaducidad(LocalDate fechaCaducidad) {
                this.fechaCaducidad = fechaCaducidad;
            }

            public String getClaveSTCC() {
                return claveSTCC;
            }

            public void setClaveSTCC(String claveSTCC) {
                this.claveSTCC = claveSTCC;
            }

            public String getClaveUnidad() {
                return claveUnidad;
            }

            public void setClaveUnidad(String claveUnidad) {
                this.claveUnidad = claveUnidad;
            }

            public String getUnidad() {
                return unidad;
            }

            public void setUnidad(String unidad) {
                this.unidad = unidad;
            }

            public String getDescripcion() {
                return descripcion;
            }

            public void setDescripcion(String descripcion) {
                this.descripcion = descripcion;
            }

            public String getBienesTransp() {
                return bienesTransp;
            }

            public void setBienesTransp(String bienesTransp) {
                this.bienesTransp = bienesTransp;
            }

            public String getCondicionesEspTransp() {
                return condicionesEspTransp;
            }

            public void setCondicionesEspTransp(String condicionesEspTransp) {
                this.condicionesEspTransp = condicionesEspTransp;
            }

            public String getCveMaterialPeligroso() {
                return cveMaterialPeligroso;
            }

            public void setCveMaterialPeligroso(String cveMaterialPeligroso) {
                this.cveMaterialPeligroso = cveMaterialPeligroso;
            }

            public String getDatosFabricante() {
                return datosFabricante;
            }

            public void setDatosFabricante(String datosFabricante) {
                this.datosFabricante = datosFabricante;
            }

            public String getDatosFormulador() {
                return datosFormulador;
            }

            public void setDatosFormulador(String datosFormulador) {
                this.datosFormulador = datosFormulador;
            }

            public String getDatosMaquilador() {
                return datosMaquilador;
            }

            public void setDatosMaquilador(String datosMaquilador) {
                this.datosMaquilador = datosMaquilador;
            }

            public String getDenominacionDistintivaProd() {
                return denominacionDistintivaProd;
            }

            public void setDenominacionDistintivaProd(String denominacionDistintivaProd) {
                this.denominacionDistintivaProd = denominacionDistintivaProd;
            }

            public String getDenominacionGenericaProd() {
                return denominacionGenericaProd;
            }

            public void setDenominacionGenericaProd(String denominacionGenericaProd) {
                this.denominacionGenericaProd = denominacionGenericaProd;
            }

            public String getDescripEmbalaje() {
                return descripEmbalaje;
            }

            public void setDescripEmbalaje(String descripEmbalaje) {
                this.descripEmbalaje = descripEmbalaje;
            }

            public String getDescripcionMateria() {
                return descripcionMateria;
            }

            public void setDescripcionMateria(String descripcionMateria) {
                this.descripcionMateria = descripcionMateria;
            }

            public String getDimensiones() {
                return dimensiones;
            }

            public void setDimensiones(String dimensiones) {
                this.dimensiones = dimensiones;
            }

            public String getEmbalaje() {
                return embalaje;
            }

            public void setEmbalaje(String embalaje) {
                this.embalaje = embalaje;
            }

            public String getFabricante() {
                return fabricante;
            }

            public void setFabricante(String fabricante) {
                this.fabricante = fabricante;
            }

            public String getFolioImpoVUCEM() {
                return folioImpoVUCEM;
            }

            public void setFolioImpoVUCEM(String folioImpoVUCEM) {
                this.folioImpoVUCEM = folioImpoVUCEM;
            }

            public String getMoneda() {
                return moneda;
            }

            public void setMoneda(String moneda) {
                this.moneda = moneda;
            }

            public String getNumCAS() {
                return numCAS;
            }

            public void setNumCAS(String numCAS) {
                this.numCAS = numCAS;
            }

            public String getNumRegSanPlagCOFEPRIS() {
                return numRegSanPlagCOFEPRIS;
            }

            public void setNumRegSanPlagCOFEPRIS(String numRegSanPlagCOFEPRIS) {
                this.numRegSanPlagCOFEPRIS = numRegSanPlagCOFEPRIS;
            }

            public String getRazonSocialEmpImp() {
                return razonSocialEmpImp;
            }

            public void setRazonSocialEmpImp(String razonSocialEmpImp) {
                this.razonSocialEmpImp = razonSocialEmpImp;
            }

            public String getSectorCOFEPRIS() {
                return sectorCOFEPRIS;
            }

            public void setSectorCOFEPRIS(String sectorCOFEPRIS) {
                this.sectorCOFEPRIS = sectorCOFEPRIS;
            }

            public String getTipoMateria() {
                return tipoMateria;
            }

            public void setTipoMateria(String tipoMateria) {
                this.tipoMateria = tipoMateria;
            }

            public String getUsoAutorizado() {
                return usoAutorizado;
            }

            public void setUsoAutorizado(String usoAutorizado) {
                this.usoAutorizado = usoAutorizado;
            }

            public DetalleMercancia getDetalleMercancia() {
                return detalleMercancia;
            }

            public void setDetalleMercancia(DetalleMercancia detalleMercancia) {
                this.detalleMercancia = detalleMercancia;
            }
            
            
            
            public class CantidadTransporta{
                private BigDecimal cantidad;
                
                private String cvesTransporte
                    ,IDOrigen
                    ,IDDestino;

                public BigDecimal getCantidad() {
                    return cantidad;
                }

                public void setCantidad(BigDecimal cantidad) {
                    this.cantidad = cantidad;
                }

                public String getCvesTransporte() {
                    return cvesTransporte;
                }

                public void setCvesTransporte(String cvesTransporte) {
                    this.cvesTransporte = cvesTransporte;
                }

                public String getIDOrigen() {
                    return IDOrigen;
                }

                public void setIDOrigen(String IDOrigen) {
                    this.IDOrigen = IDOrigen;
                }

                public String getIDDestino() {
                    return IDDestino;
                }

                public void setIDDestino(String IDDestino) {
                    this.IDDestino = IDDestino;
                }
                
            }
            
            public class GuiasIdentificacion{
                private String descripGuiaIdentificacion
                    ,numeroGuiaIdentificacion;
                
                private BigDecimal pesoGuiaIdentificacion;

                public String getDescripGuiaIdentificacion() {
                    return descripGuiaIdentificacion;
                }

                public void setDescripGuiaIdentificacion(String descripGuiaIdentificacion) {
                    this.descripGuiaIdentificacion = descripGuiaIdentificacion;
                }

                public String getNumeroGuiaIdentificacion() {
                    return numeroGuiaIdentificacion;
                }

                public void setNumeroGuiaIdentificacion(String numeroGuiaIdentificacion) {
                    this.numeroGuiaIdentificacion = numeroGuiaIdentificacion;
                }

                public BigDecimal getPesoGuiaIdentificacion() {
                    return pesoGuiaIdentificacion;
                }

                public void setPesoGuiaIdentificacion(BigDecimal pesoGuiaIdentificacion) {
                    this.pesoGuiaIdentificacion = pesoGuiaIdentificacion;
                }
                
            }
            
            public class DetalleMercancia{
                private int numPiezas;
                
                private BigDecimal pesoBruto
                    ,pesoNeto
                    ,pesoTara;
                
                private String unidadPesoMerc;

                public int getNumPiezas() {
                    return numPiezas;
                }

                public void setNumPiezas(int numPiezas) {
                    this.numPiezas = numPiezas;
                }

                public BigDecimal getPesoBruto() {
                    return pesoBruto;
                }

                public void setPesoBruto(BigDecimal pesoBruto) {
                    this.pesoBruto = pesoBruto;
                }

                public BigDecimal getPesoNeto() {
                    return pesoNeto;
                }

                public void setPesoNeto(BigDecimal pesoNeto) {
                    this.pesoNeto = pesoNeto;
                }

                public BigDecimal getPesoTara() {
                    return pesoTara;
                }

                public void setPesoTara(BigDecimal pesoTara) {
                    this.pesoTara = pesoTara;
                }

                public String getUnidadPesoMerc() {
                    return unidadPesoMerc;
                }

                public void setUnidadPesoMerc(String unidadPesoMerc) {
                    this.unidadPesoMerc = unidadPesoMerc;
                }
                
            }
            
        }
        
        public class Autotransporte{
            private String 
                    numPermisoSCT
                    ,permSCT;
            
            private Seguros seguros;
            private Remolques remolques;
            private IdentificacionVehicular identificacionVehicular;

            public Seguros getSeguros() {
                return seguros;
            }

            public void setSeguros(Seguros seguros) {
                this.seguros = seguros;
            }

            public Remolques getRemolques() {
                return remolques;
            }

            public void setRemolques(Remolques remolques) {
                this.remolques = remolques;
            }

            public IdentificacionVehicular getIdentificacionVehicular() {
                return identificacionVehicular;
            }

            public void setIdentificacionVehicular(IdentificacionVehicular identificacionVehicular) {
                this.identificacionVehicular = identificacionVehicular;
            }

            public String getNumPermisoSCT() {
                return numPermisoSCT;
            }

            public void setNumPermisoSCT(String numPermisoSCT) {
                this.numPermisoSCT = numPermisoSCT;
            }

            public String getPermSCT() {
                return permSCT;
            }

            public void setPermSCT(String permSCT) {
                this.permSCT = permSCT;
            }
            
            public class Seguros{
                private String 
                        aseguraCarga
                        ,aseguraMedAmbiente
                        ,aseguraRespCivil
                        ,polizaCarga
                        ,polizaMedAmbiente
                        ,polizaRespCivil;
                
                private BigDecimal primaSeguro;

                public String getAseguraCarga() {
                    return aseguraCarga;
                }

                public void setAseguraCarga(String aseguraCarga) {
                    this.aseguraCarga = aseguraCarga;
                }

                public String getAseguraMedAmbiente() {
                    return aseguraMedAmbiente;
                }

                public void setAseguraMedAmbiente(String aseguraMedAmbiente) {
                    this.aseguraMedAmbiente = aseguraMedAmbiente;
                }

                public String getAseguraRespCivil() {
                    return aseguraRespCivil;
                }

                public void setAseguraRespCivil(String aseguraRespCivil) {
                    this.aseguraRespCivil = aseguraRespCivil;
                }

                public String getPolizaCarga() {
                    return polizaCarga;
                }

                public void setPolizaCarga(String polizaCarga) {
                    this.polizaCarga = polizaCarga;
                }

                public String getPolizaMedAmbiente() {
                    return polizaMedAmbiente;
                }

                public void setPolizaMedAmbiente(String polizaMedAmbiente) {
                    this.polizaMedAmbiente = polizaMedAmbiente;
                }

                public String getPolizaRespCivil() {
                    return polizaRespCivil;
                }

                public void setPolizaRespCivil(String polizaRespCivil) {
                    this.polizaRespCivil = polizaRespCivil;
                }

                public BigDecimal getPrimaSeguro() {
                    return primaSeguro;
                }

                public void setPrimaSeguro(BigDecimal primaSeguro) {
                    this.primaSeguro = primaSeguro;
                }
                
            }
            
            public class Remolques{
                private List<Remolque> remolques;
                
                public Remolques(){
                    remolques = new ArrayList();
                }

                public List<Remolque> getRemolques() {
                    return remolques;
                }
                
                public class Remolque{
                    private String 
                            placa
                            ,subTipoRem;

                    public String getPlaca() {
                        return placa;
                    }

                    public void setPlaca(String placa) {
                        this.placa = placa;
                    }

                    public String getSubTipoRem() {
                        return subTipoRem;
                    }

                    public void setSubTipoRem(String subTipoRem) {
                        this.subTipoRem = subTipoRem;
                    }
                    
                }
            }
            
            public class IdentificacionVehicular{
                private int anioModeloVM;
                
                private String 
                        configVehicular
                        ,placaVM;
                
                private BigDecimal pesoBrutoVehicular;

                public int getAnioModeloVM() {
                    return anioModeloVM;
                }

                public void setAnioModeloVM(int anioModeloVM) {
                    this.anioModeloVM = anioModeloVM;
                }

                public String getConfigVehicular() {
                    return configVehicular;
                }

                public void setConfigVehicular(String configVehicular) {
                    this.configVehicular = configVehicular;
                }

                public String getPlacaVM() {
                    return placaVM;
                }

                public void setPlacaVM(String placaVM) {
                    this.placaVM = placaVM;
                }

                public BigDecimal getPesoBrutoVehicular() {
                    return pesoBrutoVehicular;
                }

                public void setPesoBrutoVehicular(BigDecimal pesoBrutoVehicular) {
                    this.pesoBrutoVehicular = pesoBrutoVehicular;
                }
                        
            }
        }
    }
    
    public class Ubicaciones{
        private List<Ubicacion> ubicaciones;
        
        public Ubicaciones(){
            ubicaciones = new ArrayList();
        }
        
        public List<Ubicacion> getUbicaciones(){
            return ubicaciones;
        }
        
        public class Ubicacion{
            private Domicilio domicilio;
            
            private String 
                    tipoUbicacion
                    ,residenciaFiscal
                    ,tipoEstacion
                    ,numEstacion
                    ,nombreEstacion
                    ,RFCRemitenteDestinatario
                    ,nombreRemitenteDestinatario;

            public Domicilio getDomicilio() {
                return domicilio;
            }

            public void setDomicilio(Domicilio domicilio) {
                this.domicilio = domicilio;
            }

            public String getTipoUbicacion() {
                return tipoUbicacion;
            }

            public void setTipoUbicacion(String tipoUbicacion) {
                this.tipoUbicacion = tipoUbicacion;
            }

            public String getResidenciaFiscal() {
                return residenciaFiscal;
            }

            public void setResidenciaFiscal(String residenciaFiscal) {
                this.residenciaFiscal = residenciaFiscal;
            }

            public String getTipoEstacion() {
                return tipoEstacion;
            }

            public void setTipoEstacion(String tipoEstacion) {
                this.tipoEstacion = tipoEstacion;
            }

            public String getNumEstacion() {
                return numEstacion;
            }

            public void setNumEstacion(String numEstacion) {
                this.numEstacion = numEstacion;
            }

            public String getNombreEstacion() {
                return nombreEstacion;
            }

            public void setNombreEstacion(String nombreEstacion) {
                this.nombreEstacion = nombreEstacion;
            }

            public String getRFCRemitenteDestinatario() {
                return RFCRemitenteDestinatario;
            }

            public void setRFCRemitenteDestinatario(String RFCRemitenteDestinatario) {
                this.RFCRemitenteDestinatario = RFCRemitenteDestinatario;
            }

            public String getNombreRemitenteDestinatario() {
                return nombreRemitenteDestinatario;
            }

            public void setNombreRemitenteDestinatario(String nombreRemitenteDestinatario) {
                this.nombreRemitenteDestinatario = nombreRemitenteDestinatario;
            }
            
            public class Domicilio{
                private String 
                        calle
                        ,numeroExterior
                        ,numeroInterior
                        ,colonia
                        ,codigoPostal
                        ,localidad
                        ,municipio
                        ,estado
                        ,pais
                        ,referencia;

                public String getCalle() {
                    return calle;
                }

                public void setCalle(String calle) {
                    this.calle = calle;
                }

                public String getNumeroExterior() {
                    return numeroExterior;
                }

                public void setNumeroExterior(String numeroExterior) {
                    this.numeroExterior = numeroExterior;
                }

                public String getNumeroInterior() {
                    return numeroInterior;
                }

                public void setNumeroInterior(String numeroInterior) {
                    this.numeroInterior = numeroInterior;
                }

                public String getColonia() {
                    return colonia;
                }

                public void setColonia(String colonia) {
                    this.colonia = colonia;
                }

                public String getCodigoPostal() {
                    return codigoPostal;
                }

                public void setCodigoPostal(String codigoPostal) {
                    this.codigoPostal = codigoPostal;
                }

                public String getLocalidad() {
                    return localidad;
                }

                public void setLocalidad(String localidad) {
                    this.localidad = localidad;
                }

                public String getMunicipio() {
                    return municipio;
                }

                public void setMunicipio(String municipio) {
                    this.municipio = municipio;
                }

                public String getEstado() {
                    return estado;
                }

                public void setEstado(String estado) {
                    this.estado = estado;
                }

                public String getPais() {
                    return pais;
                }

                public void setPais(String pais) {
                    this.pais = pais;
                }

                public String getReferencia() {
                    return referencia;
                }

                public void setReferencia(String referencia) {
                    this.referencia = referencia;
                }
                
            }
        }
    }
}
