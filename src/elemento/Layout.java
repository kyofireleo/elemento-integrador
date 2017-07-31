package elemento;

import complementos.nominas.Deducciones;
import complementos.nominas.HorasExtra;
import complementos.nominas.Incapacidad;
import complementos.nominas.Nominas;
import complementos.nominas.OtrosPagos;
import complementos.nominas.Percepciones;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import nominas.Empleado;

public class Layout {
    public String folio;
    public String nombre;
    public String rfc;
    public String rfcEmi;
    public String ruta;
    public String texto;
    public String fecha;
    public String name;
    public String sucursal;
    public String serie;
    public String fechaT;
    public String uuid;
    public String layout;
    public BigDecimal total;
    public BigDecimal subtotal;
    public BigDecimal iva;
    public String xml;
    private final Factura fact;
    private final Emisor emisor;
    private Empleado emp;
    private Nominas nomi;
    public static String fol;
    private List<String> layoutLista;
    private Long transId;
    List<String> conceptos;

    public Layout(Factura fact) {
        this.serie = fact.getSerie();
        this.fact = fact;
        this.nombre = fact.getNombre();
        this.rfc = fact.getRfc();
        this.conceptos = fact.getConceptos();
        this.iva = BigDecimal.valueOf(fact.getIva());
        this.subtotal = BigDecimal.valueOf(fact.getSubtotal());
        this.total = BigDecimal.valueOf(fact.getTotal());
        this.emisor = fact.getEmisor();
        this.rfcEmi = this.emisor.getRfc();
        fol = this.folio = fact.getFolio();
        this.crearLayout(this.rellenar(fact.prefactura));
    }

    public Layout(Factura fact, String foli) {
        this.fact = fact;
        this.nombre = fact.getNombre();
        this.rfc = fact.getRfc();
        this.conceptos = fact.getConceptos();
        this.iva = BigDecimal.valueOf(fact.getIva());
        this.subtotal = BigDecimal.valueOf(fact.getSubtotal());
        this.total = BigDecimal.valueOf(fact.getTotal());
        this.emisor = fact.getEmisor();
        this.rfcEmi = this.emisor.getRfc();
        this.serie = fact.getSerie();
        if (foli.equals("")) {
            int limite = 100000;
            int inicio = 1000;
            this.folio = "" + (int)Math.floor(Math.random() * (double)(limite - inicio + 1) + (double)inicio) + "";
        } else {
            this.folio = foli;
        }
        this.crearLayout(this.rellenar(fact.prefactura));
    }

    public Layout(Factura fact, String foli, boolean crear) {
        this.fact = fact;
        this.nombre = fact.getNombre();
        this.rfc = fact.getRfc();
        this.conceptos = fact.getConceptos();
        this.iva = BigDecimal.valueOf(fact.getIva());
        this.subtotal = BigDecimal.valueOf(fact.getSubtotal());
        this.total = BigDecimal.valueOf(fact.getTotal());
        this.emisor = fact.getEmisor();
        this.rfcEmi = this.emisor.getRfc();
        this.serie = fact.getSerie();
        if (foli.equals("")) {
            int limite = 100000;
            int inicio = 1000;
            this.folio = "" + (int)Math.floor(Math.random() * (double)(limite - inicio + 1) + (double)inicio) + "";
        } else {
            this.folio = foli;
        }
        this.layout = this.rellenar(fact.prefactura);
        if (crear) {
            this.crearLayout(this.layout);
        }
    }

    public Layout(Factura fact, Empleado emp, Nominas nomi) {
        this.fact = fact;
        this.emisor = fact.emisor;
        this.emp = emp;
        this.nomi = nomi;
        this.nombre = fact.nombre;
        this.rfc = fact.rfc;
        this.conceptos = fact.conceptos;
        this.iva = BigDecimal.valueOf(fact.iva);
        this.subtotal = BigDecimal.valueOf(fact.subtotal);
        this.total = BigDecimal.valueOf(fact.total);
        this.rfcEmi = this.emisor.getRfc();
        this.folio = fact.folio;
        this.serie = fact.serie;
        fol = this.folio;
        try {
            this.crearLayout(this.rellenarNominas(fact.prefactura));
        }
        catch (Error | Exception e) {
            e.printStackTrace();
            Elemento.log.error((Object)"Error al procesar el layout: ", e);
        }
    }

    public Long getTransId() {
        return this.transId;
    }

    private BigDecimal redondear(double num) {
        return new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
    }

    private String rellenarNominas(String preFactura) throws Error, Exception {
        Elemento.log.info((Object)"Se comienza a llenar el Layout...");
        StringBuilder re = new StringBuilder();
        this.fecha = this.getFecha();
        BigDecimal porIeps = this.redondear(this.fact.porIeps);
        BigDecimal totalIeps = this.redondear(this.fact.totalIeps);
        OtrosPagos otr = this.nomi.getOtrosPagos();
        Percepciones per = this.nomi.getPercepciones();
        Deducciones dec = this.nomi.getDeducciones();
        re.append(preFactura).append("\r\n");
        re.append("[DATOS_EMISOR]\r\n");
        re.append("NOMBRE1: ").append(this.emisor.getNombre()).append("\r\n");
        re.append("REGIMENFISCAL: ").append(this.emisor.getRegimenFiscal()).append("\r\n");
        re.append("RFC1: ").append(this.emisor.getRfc()).append("\r\n");
        re.append("CALLE1: ").append(this.emisor.getCalle()).append("\r\n");
        re.append("NOEXTERIOR1: ").append(this.emisor.getNoExterior()).append("\r\n");
        re.append("NOINTERIOR1: ").append(this.emisor.getNoInterior()).append("\r\n");
        re.append("COLONIA1: ").append(this.emisor.getColonia()).append("\r\n");
        re.append("LOCALIDAD1: ").append(this.emisor.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO1: ").append(this.emisor.getMunicipio()).append("\r\n");
        re.append("ESTADO1: ").append(this.emisor.getEstado()).append("\r\n");
        re.append("PAIS1: ").append(this.emisor.getPais()).append("\r\n");
        re.append("CP1: ").append(this.emisor.getCp()).append("\r\n");
        re.append("REGISTROPATRONAL: ").append(this.emisor.getRegistroPatronal()).append("\r\n");
        
        if(this.emisor.getRfc().trim().length() == 13)
            re.append("CURPE: ").append(this.emisor.getCurp()).append("\r\n");
        
        re.append("[/DATOS_EMISOR]\r\n\r\n");
        re.append("[DATOS_RECEPTOR]\r\n");
        re.append("NOMBRE2: ").append(this.nombre).append("\r\n");
        re.append("RFC2: ").append(this.rfc).append("\r\n");
        re.append("CALLE2: ").append(this.fact.getCalle()).append("\r\n");
        re.append("NOEXTERIOR2: ").append(this.fact.getNumExt()).append("\r\n");
        re.append("NOINTERIOR2: ").append(this.fact.getNumInt()).append("\r\n");
        re.append("COLONIA2: ").append(this.fact.getColonia()).append("\r\n");
        re.append("LOCALIDAD2: ").append(this.fact.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO2: ").append(this.fact.getMunicipio()).append("\r\n");
        re.append("ESTADO2: ").append(this.fact.getEstado()).append("\r\n");
        re.append("PAIS2: ").append(this.fact.getPais()).append("\r\n");
        re.append("CP2: ").append(this.fact.getCp()).append("\r\n");
        re.append("[/DATOS_RECEPTOR]\r\n\r\n");
        String tipoComprobante = this.fact.getTipoCfd();
        re.append("[DATOS_CFD]\r\n");
        re.append("FOLIO: ").append(this.folio).append("\r\n");
        re.append("SERIE: ").append(this.fact.getSerie()).append("\r\n");
        re.append("LUGAREXPEDICION: ").append(this.fact.getLugarExpedicion()).append("\r\n");
        re.append("TIPO_COMPROBANTE: ").append(tipoComprobante).append("\r\n");
        re.append("FORMAPAGO: En una sola exhibición\r\n");
        re.append("METODOPAGO: ").append("NA").append("\r\n");
        re.append("NUMCTAPAGO: ").append(this.fact.getCuentaBancaria()).append("\r\n");
        re.append("DESCUENTO: ").append(this.fact.descuento).append("\r\n");
        re.append("MOTIVODESCUENTO: ").append(this.fact.motivoDescuento).append("\r\n");
        re.append("MONEDA: ").append(this.fact.getMoneda()).append("\r\n");
        re.append("TIPOCAMBIO: ").append(this.fact.getTipoCambio()).append("\r\n");
        re.append("TOTALRETENIDOS: ").append(this.redondear(this.fact.getTotalRetenidos()).toString()).append("\r\n");
        re.append("TOTALTRASLADOS: ").append(this.redondear(this.fact.getTotalTraslados()).toString()).append("\r\n");
        re.append("SUBTOTAL: ").append(this.subtotal.toString()).append("\r\n");
        re.append("TOTALNETO: ").append(this.total.toString()).append("\r\n");
        re.append("LEYENDA: ").append(this.fact.getLeyenda()).append("\r\n");
        re.append("[/DATOS_CFD]\r\n\r\n");
        re.append("[CONCEPTOS]\r\n");
        for (String concepto : this.conceptos) {
            re.append(concepto).append("\r\n");
        }
        re.append("[/CONCEPTOS]\r\n\r\n");
        re.append("[IMPUESTOS_TRASLADADOS]\r\n");
        if(this.iva.doubleValue() > 0)
            re.append("IT1: IVA·").append(16.0).append("·").append(this.iva.toString()).append("\r\n");
        if(totalIeps.doubleValue() > 0)
            re.append("IT2: IEPS·").append(porIeps.toString()).append("·").append(totalIeps.toString()).append("\r\n");
        re.append("[/IMPUESTOS_TRASLADADOS]\r\n\r\n");
        re.append("[IMPUESTOS_RETENIDOS]\r\n");
        if(this.fact.getIvaRetenido() > 0)
            re.append("IR1: IVA·").append(this.fact.getIvaRetenido()).append("\r\n");
        if(this.fact.getIsrRetenido() > 0)
            re.append("IR2: ISR·").append(this.fact.getIsrRetenido()).append("\r\n");
        re.append("[/IMPUESTOS_RETENIDOS]\r\n\r\n");
        re.append("[EMPLEADO]\r\n");
        re.append("NUMEMPLEADO: ").append(this.emp.getNumEmpleado()).append("\r\n");
        re.append("CURP: ").append(this.emp.getCurp()).append("\r\n");
        re.append("TIPO_REGIMEN: ").append(this.emp.getTipoRegimen()).append("\r\n");
        re.append("NSS: ").append(this.emp.getNss()).append("\r\n");
        re.append("DEPARTAMENTO: ").append(this.emp.getDepartamento()).append("\r\n");
        re.append("PUESTO: ").append(this.emp.getPuesto()).append("\r\n");
        re.append("BANCO: ").append(this.emp.getBanco()).append("\r\n");
        re.append("CLABE: ").append(this.emp.getClabe()).append("\r\n");
        re.append("FECHA_INICIAL_REL_LABORAL: ").append(this.emp.getFechaInicialRelLaboral()).append("\r\n");
        re.append("TIPO_CONTRATO: ").append(this.emp.getTipoContrato()).append("\r\n");
        re.append("TIPO_JORNADA: ").append(this.emp.getTipoJornada()).append("\r\n");
        re.append("PERIODICIDAD_PAGO: ").append(this.emp.getPeriodicidadPago().split(",")[0]).append("\r\n");
        re.append("RIESGO_PUESTO: ").append(this.emp.getRiesgoPuesto()).append("\r\n");
        re.append("SUELDO_BASE: ").append(this.emp.getSalarioBaseCotApor()).append("\r\n");
        re.append("SALARIO_DIARIO_INT: ").append(this.emp.getSalarioDiarioInt()).append("\r\n");
        re.append("[/EMPLEADO]\r\n\r\n");
        re.append("[NOMINA]\r\n");
        re.append("TIPO_NOMINA: ").append(this.nomi.getTipoNomina()).append("\r\n");
        re.append("TOTAL_PER: ").append(this.redondear(per.getTotalSueldos())).append("\r\n");
        re.append("TOTAL_DEC: ").append(this.redondear(nomi.getTotalDeducciones().doubleValue()).toString()).append("\r\n");
        re.append("TOTAL_OTP: ").append(this.redondear(nomi.getTotalOtrosPagos().doubleValue()).toString()).append("\r\n");
        re.append("FECHA_PAGO: ").append(this.nomi.getFechaPago()).append("\r\n");
        re.append("FECHA_INICIAL_PAGO: ").append(this.nomi.getFechaInicialPago()).append("\r\n");
        re.append("FECHA_FINAL_PAGO: ").append(this.nomi.getFechaFinalPago()).append("\r\n");
        re.append("NUM_DIAS_PAGADOS: ").append(this.nomi.getNumDiasPagados()).append("\r\n");
        re.append("ANTIGUEDAD: ").append("P").append(this.nomi.getAntiguedad()).append("W").append("\r\n");
        re.append("[/NOMINA]\r\n\r\n");
        
        if (!otr.getOtrosPagos().isEmpty()) {
            re.append("[OTROS_PAGOS]\r\n");
            for (int i = 0; i < otr.getOtrosPagos().size(); ++i) {
                OtrosPagos.OtroPago p = (OtrosPagos.OtroPago)otr.getOtrosPagos().get(i);
                re.append("OTR").append(i + 1).append(": ").append(p.getTipoOtroPago()).append("@").append(p.getClave()).append("@").append(p.getConcepto()).append("@").append(p.getImporte()).append("\r\n");
            }
            re.append("[/OTROS_PAGOS]\r\n\r\n");
        }
        
        if (!per.getPercepciones().isEmpty()) {
            re.append("[PERCEPCIONES]\r\n");
            for (int i = 0; i < per.getPercepciones().size(); ++i) {
                Percepciones.Percepcion p = (Percepciones.Percepcion)per.getPercepciones().get(i);
                re.append("PER").append(i + 1).append(": ").append(p.getTipoPercepcion()).append("@").append(p.getClave()).append("@").append(p.getConcepto()).append("@").append(p.getImporteGravado()).append("@").append(p.getImporteExento()).append("\r\n");
            }
            re.append("[/PERCEPCIONES]\r\n\r\n");
        }
        if (!dec.getDeducciones().isEmpty()) {
            re.append("[DEDUCCIONES]\r\n");
            for (int i = 0; i < dec.getDeducciones().size(); ++i) {
                Deducciones.Deduccion d = (Deducciones.Deduccion)dec.getDeducciones().get(i);
                re.append("DEC").append(i + 1).append(": ").append(d.getTipoDeduccion()).append("@").append(d.getClave()).append("@").append(d.getConcepto()).append("@").append(d.getImporte()).append("\r\n");
            }
            re.append("[/DEDUCCIONES]\r\n\r\n");
        }
        if (this.nomi.getIncapacidad() != null) {
            Incapacidad i = this.nomi.getIncapacidad();
            re.append("[INCAPACIDAD]");
            re.append("DIAS_INCAPACIDAD: ").append(i.getDiasIncapacidad()).append("\r\n");
            re.append("TIPO_INCAPACIDAD: ").append(i.getTipoIncapacidad()).append("\r\n");
            re.append("DESCUENTO_INCAPACIDAD: ").append(this.redondear(i.getDescuento()).toString()).append("\r\n");
            re.append("[/INCAPACIDAD]");
        }
        if (this.nomi.getHorasExtra() != null) {
            HorasExtra h = this.nomi.getHorasExtra();
            re.append("[HORAS_EXTRA]\r\n");
            re.append("DIAS: ").append(h.getDias()).append("\r\n");
            re.append("NUM_HORAS_EXTRA: ").append(h.getHorasExtra()).append("\r\n");
            re.append("TIPO_HORAS: ").append(h.getTipoHoras()).append("\r\n");
            re.append("IMPORTE_PAGADO: ").append(h.getImportePagado()).append("\r\n");
            re.append("[/HORAS_EXTRA]\r\n\r\n");
        }
        return re.toString();
    }

    private String rellenar(String preFactura) {
        double porcentaje;
        Elemento.log.info((Object)"Se comienza a llenar el Layout...");
        StringBuilder re = new StringBuilder();
        this.fecha = this.getFecha();
        BigDecimal porIeps = new BigDecimal(this.fact.porIeps).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalIeps = new BigDecimal(this.fact.totalIeps).setScale(2, RoundingMode.HALF_UP);
        re.append(preFactura).append("\r\n");
        re.append("[DATOS_EMISOR]\r\n");
        re.append("NOMBRE1: ").append(this.emisor.getNombre()).append("\r\n");
        re.append("REGIMENFISCAL: ").append(this.emisor.getRegimenFiscal()).append("\r\n");
        re.append("RFC1: ").append(this.emisor.getRfc()).append("\r\n");
        re.append("CALLE1: ").append(this.emisor.getCalle()).append("\r\n");
        re.append("NOEXTERIOR1: ").append(this.emisor.getNoExterior()).append("\r\n");
        re.append("NOINTERIOR1: ").append(this.emisor.getNoInterior()).append("\r\n");
        re.append("COLONIA1: ").append(this.emisor.getColonia()).append("\r\n");
        re.append("LOCALIDAD1: ").append(this.emisor.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO1: ").append(this.emisor.getMunicipio()).append("\r\n");
        re.append("ESTADO1: ").append(this.emisor.getEstado()).append("\r\n");
        re.append("PAIS1: ").append(this.emisor.getPais()).append("\r\n");
        re.append("CP1: ").append(this.emisor.getCp()).append("\r\n");
        re.append("[/DATOS_EMISOR]\r\n\r\n");
        if (this.emisor.getExpedidoEn() != null) {
            ExpedidoEn expedidoEn = this.emisor.getExpedidoEn();
            re.append("[EXPEDIDO_EN]\r\n");
            re.append("CALLEX: ").append(expedidoEn.getCalle()).append("\r\n");
            re.append("NOEXTERIORX: ").append(expedidoEn.getNoExterior()).append("\r\n");
            re.append("NOINTERIORX: ").append(expedidoEn.getNoInterior()).append("\r\n");
            re.append("COLONIAX: ").append(expedidoEn.getColonia()).append("\r\n");
            re.append("LOCALIDADX: ").append(expedidoEn.getLocalidad()).append("\r\n");
            re.append("MUNICIPIOX: ").append(expedidoEn.getMunicipio()).append("\r\n");
            re.append("ESTADOX: ").append(expedidoEn.getEstado()).append("\r\n");
            re.append("PAISX: ").append(expedidoEn.getPais()).append("\r\n");
            re.append("CPX: ").append(expedidoEn.getCodigoPostal()).append("\r\n");
            re.append("[/EXPEDIDO_EN]\r\n\r\n");
        }
        re.append("[DATOS_RECEPTOR]\r\n");
        re.append("NOMBRE2: ").append(this.nombre).append("\r\n");
        re.append("RFC2: ").append(this.rfc).append("\r\n");
        re.append("CALLE2: ").append(this.fact.getCalle()).append("\r\n");
        re.append("NOEXTERIOR2: ").append(this.fact.getNumExt()).append("\r\n");
        re.append("NOINTERIOR2: ").append(this.fact.getNumInt()).append("\r\n");
        re.append("COLONIA2: ").append(this.fact.getColonia()).append("\r\n");
        re.append("LOCALIDAD2: ").append(this.fact.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO2: ").append(this.fact.getMunicipio()).append("\r\n");
        re.append("ESTADO2: ").append(this.fact.getEstado()).append("\r\n");
        re.append("PAIS2: ").append(this.fact.getPais()).append("\r\n");
        re.append("CP2: ").append(this.fact.getCp()).append("\r\n");
        re.append("[/DATOS_RECEPTOR]\r\n\r\n");
        
        re.append("[DATOS_CFD]\r\n");
        re.append("FOLIO: ").append(this.folio).append("\r\n");
        re.append("SERIE: ").append(this.fact.getSerie()).append("\r\n");
        re.append("LUGAREXPEDICION: ").append(this.fact.getLugarExpedicion()).append("\r\n");
        re.append("TIPO_COMPROBANTE: ").append(this.fact.getTipoCfd()).append("\r\n");
        re.append("FORMAPAGO: ").append(this.fact.getFormaPago()).append("\r\n");
        re.append("CONDICIONPAGO: ").append(this.fact.getCondicionPago()).append("\r\n");
        re.append("METODOPAGO: ").append(this.fact.getMetodoPago()).append("\r\n");
        re.append("NUMCTAPAGO: ").append(this.fact.getCuentaBancaria()).append("\r\n");
        re.append("DESCUENTO: ").append(this.fact.getDescuento()).append("\r\n");
        re.append("MOTIVODESCUENTO: _\r\n");
        re.append("MONEDA: ").append(this.fact.getMoneda()).append("\r\n");
        re.append("TIPOCAMBIO: ").append(this.fact.getTipoCambio()).append("\r\n");
        re.append("TOTALRETENIDOS: ").append(new BigDecimal(this.fact.getTotalRetenidos()).setScale(2, RoundingMode.HALF_UP).toString()).append("\r\n");
        re.append("TOTALTRASLADOS: ").append(new BigDecimal(this.fact.getTotalTraslados()).setScale(2, RoundingMode.HALF_UP).toString()).append("\r\n");
        re.append("SUBTOTAL: ").append(this.subtotal.toString()).append("\r\n");
        re.append("TOTALNETO: ").append(this.total.toString()).append("\r\n");
        re.append("LEYENDA: ").append(this.fact.getLeyenda()).append("\r\n");
        re.append("[/DATOS_CFD]\r\n\r\n");
        
        re.append("[CONCEPTOS]\r\n");
        for (String concepto : this.conceptos) {
            re.append(concepto).append("\r\n");
        }
        re.append("[/CONCEPTOS]\r\n\r\n");
        
        if(fact.getDonataria() != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaAuto = sdf.format(fact.getDonataria().getFechaAutorizacion());
            re.append("[DONATARIA]\r\n");
            re.append("FECHAAUTO: ").append(fechaAuto).append("\r\n");
            re.append("NOAUTO: ").append(fact.getDonataria().getNoAutorizacion()).append("\r\n");
            re.append("[/DONATARIA]\r\n");
        }
        
        re.append("[IMPUESTOS_TRASLADADOS]\r\n");
        if (this.iva.doubleValue() == 0.0) {
            porcentaje = 0.0;
        } else {
            porcentaje = 16.0;
        }
        re.append("IT1: IVA·").append(porcentaje).append("·").append(this.iva.toString()).append("\r\n");
        re.append("IT2: IEPS·").append(porIeps.toString()).append("·").append(totalIeps.toString()).append("\r\n");
        re.append("[/IMPUESTOS_TRASLADADOS]\r\n");
        re.append("[IMPUESTOS_RETENIDOS]\r\n");
        re.append("IR1: IVA·").append(this.fact.getIvaRetenido()).append("\r\n");
        re.append("IR2: ISR·").append(this.fact.getIsrRetenido()).append("\r\n");
        re.append("[/IMPUESTOS_RETENIDOS]\r\n");
        this.texto = re.toString();
        return this.texto;
    }

    private void crearLayout(String texto) {
        Elemento.log.info((Object)"Se procede a crear el archivo Layout");
        this.ruta = Elemento.pathLayout + this.serie + "_" + this.folio + "_" + this.emisor.getRfc() + "_" + this.rfc;
        if (!this.fact.prefactura.equals("")) {
            this.ruta = this.ruta + "_" + this.fact.prefactura;
        }
        this.ruta = this.ruta + ".txt";
        File lay = new File(this.ruta);
        try {
            if (lay.exists()) {
                lay.delete();
            }
            FileOutputStream file = new FileOutputStream(this.ruta);
            OutputStreamWriter fw = new OutputStreamWriter((OutputStream)file, "UTF8");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(texto);
            bw.close();
            fw.close();
            file.close();
            Elemento.log.info((Object)"Archivo layout creado correctamente...");
        }
        catch (IOException e) {
            e.printStackTrace();
            Elemento.log.error((Object)("Ocurrio la siguiente excepcion: " + e.getMessage()), (Throwable)e);
        }
    }

    private String getFecha() {
        GregorianCalendar cal = new GregorianCalendar();
        String toString = cal.toString();
        String fechaFact = "";
        fechaFact = fechaFact + cal.get(1);
        fechaFact = fechaFact + "-";
        fechaFact = 1 + cal.get(2) < 10 ? fechaFact + "0" + (1 + cal.get(2)) : fechaFact + (1 + cal.get(2));
        fechaFact = fechaFact + "-";
        fechaFact = 1 + cal.get(5) < 10 ? fechaFact + "0" + cal.get(5) : fechaFact + cal.get(5);
        fechaFact = fechaFact + "T";
        fechaFact = cal.get(11) < 10 ? fechaFact + "0" + cal.get(11) : fechaFact + cal.get(11);
        fechaFact = fechaFact + ":";
        fechaFact = cal.get(12) < 10 ? fechaFact + "0" + cal.get(12) : fechaFact + cal.get(12);
        fechaFact = fechaFact + ":";
        fechaFact = cal.get(13) < 10 ? fechaFact + "0" + cal.get(13) : fechaFact + cal.get(13);
        return fechaFact;
    }

    private String getPorcentajeIva(double iv, double sb) {
        double porcentaje = Math.rint(iv * 100.0 / sb * 100.0) / 100.0;
        return "" + porcentaje;
    }
}