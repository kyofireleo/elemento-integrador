package elemento;

import complementos.nominas.Deducciones;
import complementos.nominas.HorasExtra;
import complementos.nominas.Incapacidad;
import complementos.nominas.Nominas;
import complementos.nominas.OtrosPagos;
import complementos.nominas.Percepciones;
import elemento.Factura.ConceptoTraslado;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nominas.Empleado;
import pagos.Documento;
import pagos.Pago;
import pagos.Pagos;

public class Layout {

    public String folio;
    public String nombre;
    public String rfc;
    public String pais;
    public String codigoPostal;
    public String regimenFiscalReceptor;
    public String numRegIdTrib;
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
    private utils.Utils util = new utils.Utils(Elemento.log);

    public Layout(Factura fact) {
        this.serie = fact.getSerie();
        this.fact = fact;
        this.nombre = fact.getNombre();
        this.rfc = fact.getRfc();
        this.codigoPostal = fact.getCp();
        this.regimenFiscalReceptor = fact.getRegimenFiscalReceptor();
        this.numRegIdTrib = fact.getNumRegIdTrib();
        this.pais = fact.getPais();
        this.conceptos = fact.getConceptos();
        this.iva = fact.getIva();
        this.subtotal = fact.getSubtotal();
        this.total = fact.getTotal();
        this.emisor = fact.getEmisor();
        this.rfcEmi = this.emisor.getRfc();
        fol = this.folio = fact.getFolio();
        this.crearLayout(this.rellenar(fact.prefactura));
    }

    public Layout(Factura fact, String foli) {
        this.fact = fact;
        this.nombre = fact.getNombre();
        this.rfc = fact.getRfc();
        this.codigoPostal = fact.getCp();
        this.regimenFiscalReceptor = fact.getRegimenFiscalReceptor();
        this.numRegIdTrib = fact.getNumRegIdTrib();
        this.pais = fact.getPais();
        this.conceptos = fact.getConceptos();
        this.iva = fact.getIva();
        this.subtotal = fact.getSubtotal();
        this.total = fact.getTotal();
        this.emisor = fact.getEmisor();
        this.rfcEmi = this.emisor.getRfc();
        this.serie = fact.getSerie();
        if (foli.equals("")) {
            int limite = 100000;
            int inicio = 1000;
            this.folio = "" + (int) Math.floor(Math.random() * (double) (limite - inicio + 1) + (double) inicio) + "";
        } else {
            this.folio = foli;
        }
        this.crearLayout(this.rellenar(fact.prefactura));
    }

    public Layout(Factura fact, String foli, boolean crear) {
        this.fact = fact;
        this.nombre = fact.getNombre();
        this.rfc = fact.getRfc();
        this.codigoPostal = fact.getCp();
        this.regimenFiscalReceptor = fact.getRegimenFiscalReceptor();
        this.numRegIdTrib = fact.getNumRegIdTrib();
        this.pais = fact.getPais();
        this.conceptos = fact.getConceptos();
        this.iva = fact.getIva();
        this.subtotal = fact.getSubtotal();
        this.total = fact.getTotal();
        this.emisor = fact.getEmisor();
        this.rfcEmi = this.emisor.getRfc();
        this.serie = fact.getSerie();
        if (foli.equals("")) {
            int limite = 100000;
            int inicio = 1000;
            this.folio = "" + (int) Math.floor(Math.random() * (double) (limite - inicio + 1) + (double) inicio) + "";
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
        this.codigoPostal = fact.getCp();
        this.regimenFiscalReceptor = fact.getRegimenFiscalReceptor();
        //this.numRegIdTrib = fact.getNumRegIdTrib();
        //this.pais = fact.getPais();
        this.conceptos = fact.conceptos;
        this.iva = fact.iva;
        this.subtotal = fact.subtotal;
        this.total = fact.total;
        this.rfcEmi = this.emisor.getRfc();
        this.folio = fact.folio;
        this.serie = fact.serie;
        fol = this.folio;
        try {
            this.crearLayout(this.rellenarNominas(fact.prefactura));
        } catch (Error | Exception e) {
            e.printStackTrace();
            Elemento.log.error((Object) "Error al procesar el layout: ", e);
        }
    }

    public Layout(Factura fact, Pagos pagos) {
        this.fact = fact;
        this.emisor = fact.emisor;
        this.nombre = fact.nombre;
        this.rfc = fact.rfc;
        this.codigoPostal = fact.getCp();
        this.pais = fact.getPais();
        this.regimenFiscalReceptor = fact.getRegimenFiscalReceptor();
        //this.numRegIdTrib = fact.getNumRegIdTrib();
        //this.pais = fact.getPais();
        this.rfcEmi = fact.emisor.getRfc();
        this.folio = fact.folio;
        this.serie = fact.serie;
        fol = this.folio;
        try {
            this.crearLayout(this.rellenarPagos(pagos));
        } catch (Error | Exception e) {
            e.printStackTrace();
            Elemento.log.error("Error al procesar el layout para pagos: ", e);
        }
    }

    public Long getTransId() {
        return this.transId;
    }

    private BigDecimal redondear(double num) {
        return new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal redondear(BigDecimal num) {
        return num.setScale(2, RoundingMode.HALF_UP);
    }

    private String rellenarNominas(String preFactura) throws Error, Exception {
        Elemento.log.info((Object) "Se comienza a llenar el Layout...");
        StringBuilder re = new StringBuilder();
        this.fecha = this.getFecha();
        /*BigDecimal porIeps = this.redondear(this.fact.porIeps);
        BigDecimal totalIeps = this.redondear(this.fact.totalIeps);*/
        OtrosPagos otr = this.nomi.getOtrosPagos();
        Percepciones per = this.nomi.getPercepciones();
        Deducciones dec = this.nomi.getDeducciones();
        re.append(preFactura).append("\r\n");
        re.append("[DATOS_EMISOR]\r\n");
        re.append("NOMBRE1: ").append(this.emisor.getNombre()).append("\r\n");
        re.append("REGIMENFISCAL: ").append(this.emisor.getRegimenFiscal()).append("\r\n");
        re.append("RFC1: ").append(this.emisor.getRfc()).append("\r\n");
        /*re.append("CALLE1: ").append(this.emisor.getCalle()).append("\r\n");
        re.append("NOEXTERIOR1: ").append(this.emisor.getNoExterior()).append("\r\n");
        re.append("NOINTERIOR1: ").append(this.emisor.getNoInterior()).append("\r\n");
        re.append("COLONIA1: ").append(this.emisor.getColonia()).append("\r\n");
        re.append("LOCALIDAD1: ").append(this.emisor.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO1: ").append(this.emisor.getMunicipio()).append("\r\n");
        re.append("ESTADO1: ").append(this.emisor.getEstado()).append("\r\n");
        re.append("PAIS1: ").append(this.emisor.getPais()).append("\r\n");
        re.append("CP1: ").append(this.emisor.getCp()).append("\r\n");*/
        re.append("REGISTROPATRONAL: ").append(this.emisor.getRegistroPatronal()).append("\r\n");

        if (this.emisor.getRfc().trim().length() == 13) {
            re.append("CURPE: ").append(this.emisor.getCurp()).append("\r\n");
        }
        re.append("[/DATOS_EMISOR]\r\n\r\n");
        
        re.append("[DATOS_RECEPTOR]\r\n");
        re.append("NOMBRE2: ").append(this.nombre).append("\r\n");
        re.append("RFC2: ").append(this.rfc).append("\r\n");
        /*re.append("CALLE2: ").append(this.fact.getCalle()).append("\r\n");
        re.append("NOEXTERIOR2: ").append(this.fact.getNumExt()).append("\r\n");
        re.append("NOINTERIOR2: ").append(this.fact.getNumInt()).append("\r\n");
        re.append("COLONIA2: ").append(this.fact.getColonia()).append("\r\n");
        re.append("LOCALIDAD2: ").append(this.fact.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO2: ").append(this.fact.getMunicipio()).append("\r\n");
        re.append("ESTADO2: ").append(this.fact.getEstado()).append("\r\n");
        re.append("PAIS2: ").append(this.fact.getPais()).append("\r\n");
        re.append("CP2: ").append(this.fact.getCp()).append("\r\n");*/
        re.append("REGIMENFISCAL2: ").append(this.regimenFiscalReceptor).append("\r\n");
        re.append("DOMICILIOFISCAL: ").append(this.codigoPostal).append("\r\n");
        //re.append("RESIDENCIAFISCAL: ").append(this.pais).append("\r\n");
        re.append("USOCFDI: ").append(this.fact.usoCfdi).append("\r\n");
        re.append("[/DATOS_RECEPTOR]\r\n\r\n");

        String tipoComprobante = this.fact.getTipoCfd();
        re.append("[DATOS_CFD]\r\n");
        re.append("FOLIO: ").append(this.folio).append("\r\n");
        re.append("SERIE: ").append(this.fact.getSerie()).append("\r\n");
        re.append("LUGAREXPEDICION: ").append(this.fact.getLugarExpedicion()).append("\r\n");
        re.append("TIPO_COMPROBANTE: ").append(tipoComprobante).append("\r\n");
        re.append("FORMAPAGO: ").append(fact.formaPago).append("\r\n");
        re.append("METODOPAGO: ").append(fact.metodoPago).append("\r\n");
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
        /*re.append("[IMPUESTOS_TRASLADADOS]\r\n");
        if(this.iva.doubleValue() > 0)
            re.append("IT1: IVA@").append(16.0).append("@").append(this.iva.toString()).append("\r\n");
        
        if(totalIeps.doubleValue() > 0)
            re.append("IT2: IEPS@").append(porIeps.toString()).append("@").append(totalIeps.toString()).append("\r\n");
        
        re.append("[/IMPUESTOS_TRASLADADOS]\r\n\r\n");*/
        re.append("[IMPUESTOS_RETENIDOS]\r\n");
        if (this.fact.getIsrRetenido().compareTo(BigDecimal.ZERO) == 1) {
            re.append("IR2: 001@").append(this.fact.getIsrRetenido()).append("\r\n");
        }
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
        re.append("TOTAL_DEC: ").append(this.redondear(nomi.getTotalDeducciones()).toString()).append("\r\n");
        re.append("TOTAL_OTP: ").append(this.redondear(nomi.getTotalOtrosPagos()).toString()).append("\r\n");
        re.append("FECHA_PAGO: ").append(this.nomi.getFechaPago()).append("\r\n");
        re.append("FECHA_INICIAL_PAGO: ").append(this.nomi.getFechaInicialPago()).append("\r\n");
        re.append("FECHA_FINAL_PAGO: ").append(this.nomi.getFechaFinalPago()).append("\r\n");
        re.append("NUM_DIAS_PAGADOS: ").append(this.nomi.getNumDiasPagados()).append("\r\n");
        re.append("ANTIGUEDAD: ").append("P").append(this.nomi.getAntiguedad()).append("W").append("\r\n");
        re.append("[/NOMINA]\r\n\r\n");

        if (otr != null && !otr.getOtrosPagos().isEmpty()) {
            re.append("[OTROS_PAGOS]\r\n");
            for (int i = 0; i < otr.getOtrosPagos().size(); ++i) {
                OtrosPagos.OtroPago p = (OtrosPagos.OtroPago) otr.getOtrosPagos().get(i);
                re.append("OTR").append(i + 1).append(": ").append(p.getTipoOtroPago()).append("@").append(p.getClave()).append("@").append(p.getConcepto()).append("@").append(p.getImporte()).append("\r\n");
            }
            re.append("[/OTROS_PAGOS]\r\n\r\n");
        }

        if (per != null && !per.getPercepciones().isEmpty()) {
            re.append("[PERCEPCIONES]\r\n");
            for (int i = 0; i < per.getPercepciones().size(); ++i) {
                Percepciones.Percepcion p = (Percepciones.Percepcion) per.getPercepciones().get(i);
                re.append("PER").append(i + 1).append(": ").append(p.getTipoPercepcion()).append("@").append(p.getClave()).append("@").append(p.getConcepto()).append("@").append(p.getImporteGravado()).append("@").append(p.getImporteExento()).append("\r\n");
            }
            re.append("[/PERCEPCIONES]\r\n\r\n");
        }
        if (dec != null && !dec.getDeducciones().isEmpty()) {
            re.append("[DEDUCCIONES]\r\n");
            for (int i = 0; i < dec.getDeducciones().size(); ++i) {
                Deducciones.Deduccion d = (Deducciones.Deduccion) dec.getDeducciones().get(i);
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

    private String rellenarPagos(Pagos pagos) throws Error, Exception {
        Elemento.log.info((Object) "Se comienza a llenar el Layout...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder re = new StringBuilder();
        StringBuilder retPago = new StringBuilder();
        StringBuilder traPago = new StringBuilder();
        StringBuilder retDoc = new StringBuilder();
        StringBuilder traDoc = new StringBuilder();

        re.append("[DATOS_EMISOR]\r\n");
        re.append("NOMBRE1: ").append(this.emisor.getNombre()).append("\r\n");
        re.append("REGIMENFISCAL: ").append(this.emisor.getRegimenFiscal()).append("\r\n");
        re.append("RFC1: ").append(this.emisor.getRfc()).append("\r\n");
        re.append("[/DATOS_EMISOR]\r\n\r\n");
        
        re.append("[DATOS_RECEPTOR]\r\n");
        re.append("NOMBRE2: ").append(this.nombre).append("\r\n");
        re.append("RFC2: ").append(this.rfc).append("\r\n");
        re.append("DOMICILIOFISCAL: ").append(this.codigoPostal).append("\r\n");
        re.append("REGIMENFISCAL2: ").append(this.regimenFiscalReceptor).append("\r\n");
        re.append("RESIDENCIAFISCAL: ").append(this.pais).append("\r\n");
        re.append("USOCFDI: ").append(this.fact.usoCfdi).append("\r\n");
        re.append("[/DATOS_RECEPTOR]\r\n\r\n");

        re.append("[DATOS_CFD]\r\n");
        re.append("FOLIO: ").append(this.folio).append("\r\n");
        re.append("SERIE: ").append(this.fact.getSerie()).append("\r\n");
        re.append("LUGAREXPEDICION: ").append(this.fact.getLugarExpedicion()).append("\r\n");
        re.append("TIPO_COMPROBANTE: P\r\n");
        if (this.fact.cfdisAsociados != null && !this.fact.cfdisAsociados.trim().isEmpty()) {
            re.append("RELACIONCFDI: ").append(this.fact.cfdisAsociados).append("\r\n");
            re.append("TIPORELACION: ").append(this.fact.tipoRelacion).append("\r\n");
        }
        re.append("FORMAPAGO: \r\n");
        re.append("METODOPAGO: \r\n");
        re.append("DESCUENTO: 0.00\r\n");
        re.append("MOTIVODESCUENTO: \r\n");
        re.append("MONEDA: ").append(this.fact.getMoneda()).append("\r\n");
        re.append("TIPOCAMBIO: ").append(this.fact.getTipoCambio()).append("\r\n");
        re.append("TOTALRETENIDOS: 0\r\n");
        re.append("TOTALTRASLADOS: 0\r\n");
        re.append("SUBTOTAL: 0\r\n");
        re.append("TOTALNETO: 0\r\n");
        re.append("LEYENDA: ").append(this.fact.getLeyenda()).append("\r\n");
        re.append("[/DATOS_CFD]\r\n\r\n");

        re.append("[CONCEPTOS]\r\n");
        re.append("C1: 84111506@ACT@.@.@1@Pago@0@0@0\r\n");
        re.append("[/CONCEPTOS]\r\n\r\n");
        
        re.append("[INFO_PAGOS]\r\n");
        re.append("MontoTotalPagos: ").append(pagos.getTotalMontoPagos().toString()).append("\r\n");
        re.append("TotalRetencionesIVA: ").append((pagos.getTotalRetencionesIVA() != null ? pagos.getTotalRetencionesIVA() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalRetensionesISR: ").append((pagos.getTotalRetensionesISR() != null ? pagos.getTotalRetensionesISR() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalRetensionesIEPS: ").append((pagos.getTotalRetensionesIEPS() != null ? pagos.getTotalRetensionesIEPS() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosBaseIVA16: ").append((pagos.getTotalTrasladosBaseIVA16() != null ? pagos.getTotalTrasladosBaseIVA16() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosImpuestoIVA16: ").append((pagos.getTotalTrasladosImpuestoIVA16() != null ? pagos.getTotalTrasladosImpuestoIVA16() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosBaseIVA8: ").append((pagos.getTotalTrasladosBaseIVA8() != null ? pagos.getTotalTrasladosBaseIVA8() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosImpuestoIVA8: ").append((pagos.getTotalTrasladosImpuestoIVA8() != null ? pagos.getTotalTrasladosImpuestoIVA8() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosBaseIVA0: ").append((pagos.getTotalTrasladosBaseIVA0() != null ? pagos.getTotalTrasladosBaseIVA0() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosImpuestoIVA0: ").append((pagos.getTotalTrasladosImpuestoIVA0() != null ? pagos.getTotalTrasladosImpuestoIVA0() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("TotalTrasladosBaseIVAExento: ").append((pagos.getTotalTrasladosBaseIVAExento() != null ? pagos.getTotalTrasladosBaseIVAExento() : BigDecimal.ZERO).toString()).append("\r\n");
        re.append("[/INFO_PAGOS]\r\n\r\n");
        
        re.append("[PAGOS]\r\n");
        for (int i = 0; i < pagos.getPagos().size(); i++) {
            Pago p = pagos.getPagos().get(i);
            re.append("P").append((i + 1)).append(": ").append(p.getCuentaBeneficiario().isEmpty() ? "." : p.getCuentaBeneficiario());
            re.append("@").append(p.getCuentaBeneficiario().isEmpty() ? "." : p.getRfcCtaBen());
            re.append("@").append(p.getCuentaOrdenante().isEmpty() ? "." : p.getCuentaOrdenante());
            re.append("@").append(p.getCuentaOrdenante().isEmpty() ? "." : p.getRfcCtaOrd());
            re.append("@").append(p.getMonto().toString());
            re.append("@").append(p.getMoneda());
            re.append("@").append(p.getTipoCambio() == null ? "." : p.getTipoCambio().toString());
            re.append("@").append(p.getFormaPago());
            re.append("@").append(sdf.format(p.getFechaPago()));
            re.append("@").append(p.getTipoCadPago() != null ? p.getTipoCadPago() : "");
            re.append("@").append(p.getCertPago() != null ? p.getCertPago() : "");
            re.append("@").append(p.getCadPago() != null ? p.getCadPago() : "");
            re.append("@").append(p.getSelloPago() != null ? p.getSelloPago() : "");
            re.append("@").append(p.getNumOperacion());
            
            if(p.getRfcCtaOrd().equals("XEXX010101000")){
                re.append("@").append(p.getNomBancoOrdExt());
            }
            
            re.append("\r\n");
            int cont = 0;
            
            /*
            //Retenciones
            if(p.getPagoRetensionesISR() != null){
                cont++;
                retPago.append("PIR").append(cont).append(": P").append((i + 1)).append("@001@").append(p.getPagoRetensionesISR().toString()).append("\r\n");
            }
            if(p.getPagoRetencionesIVA() != null){
                cont++;
                retPago.append("PIR").append(cont).append(": P").append((i + 1)).append("@002@").append(p.getPagoRetencionesIVA().toString()).append("\r\n");
            }
            if(p.getPagoRetensionesIEPS() != null){
                cont++;
                retPago.append("PIR").append(cont).append(": P").append((i + 1)).append("@003@").append(p.getPagoRetensionesIEPS().toString()).append("\r\n");
            }
            */
            
            //Traslados
            cont = 0;
            if(p.getPagoTrasladosBaseIVA0() != null){
                cont++;
                traPago.append("PIT").append(cont).append(": P").append((i + 1)).append("@002@").append(p.getPagoTrasladosBaseIVA0().toString()).append("@").append(p.getPagoTrasladosImpuestoIVA0()).append("@Tasa@0.000000").append("\r\n");
            }
            if(p.getPagoTrasladosBaseIVA8() != null){
                cont++;
                traPago.append("PIT").append(cont).append(": P").append((i + 1)).append("@002@").append(p.getPagoTrasladosBaseIVA8().toString()).append("@").append(p.getPagoTrasladosImpuestoIVA8()).append("@Tasa@0.080000").append("\r\n");
            }
            if(p.getPagoTrasladosBaseIVA16() != null){
                cont++;
                traPago.append("PIT").append(cont).append(": P").append((i + 1)).append("@002@").append(p.getPagoTrasladosBaseIVA16().toString()).append("@").append(p.getPagoTrasladosImpuestoIVA16()).append("@Tasa@0.160000").append("\r\n");
            }
            if(p.getPagoTrasladosBaseIVAExento() != null){
                cont++;
                traPago.append("PIT").append(cont).append(": P").append((i + 1)).append("@002@").append(p.getPagoTrasladosBaseIVAExento().toString()).append("@").append("0.00").append("@Exento@0.000000").append("\r\n");
            }
        }
        re.append("[/PAGOS]\r\n\r\n");
        
        //IMPUESTOS_PAGOS
        
        re.append("[PAGOS_IMPUESTOS_RETENIDOS]\r\n");
        if(!retPago.toString().trim().isEmpty())
            re.append(retPago.toString());
        re.append("[/PAGOS_IMPUESTOS_RETENIDOS]\r\n\r\n");
        
        re.append("[PAGOS_IMPUESTOS_TRASLADOS]\r\n");
        if(!traPago.toString().trim().isEmpty())
            re.append(traPago.toString());
        re.append("[/PAGOS_IMPUESTOS_TRASLADOS]\r\n\r\n");

        re.append("[DOCTOS_PAGOS]\r\n");
        for (int h = 0; h < pagos.getPagos().size(); h++) {
            Pago p = pagos.getPagos().get(h);
            for (int i = 0; i < p.getDocumentos().size(); i++) {
                Documento d = p.getDocumentos().get(i);
                re.append("DP").append((i + 1)).append(": P").append((h + 1));
                re.append("@").append(d.getFolio());
                re.append("@").append(d.getSerie());
                re.append("@").append(d.getImpSaldoInsoluto().toString());
                re.append("@").append(d.getImpPagado().toString());
                re.append("@").append(d.getImpSaldoAnterior().toString());
                re.append("@").append(d.getNumParcialidad());
                //re.append("@").append(d.getMetodoPago());
                re.append("@").append(d.getMoneda());
                re.append("@").append(d.getEquivalencia().toString());
                re.append("@").append(d.getUuid());
                re.append("@").append(d.getObjImp()).append("\r\n");
                
                int cont = 0;
                
                /*
                //Retenciones
                if(d.getRetensionISR()!= null){
                    cont++;
                    retDoc.append("PIR").append(cont).append(": DP").append((i + 1)).append("@001@").append(d.getRetensionISR().toString()).append("@Tasa@").append(d.getR).append("\r\n");
                }
                if(d.getRetencionIVA()!= null){
                    cont++;
                    retDoc.append("PIR").append(cont).append(": DP").append((i + 1)).append("@002@").append(d.getRetencionIVA().toString()).append("\r\n");
                }
                if(d.getRetensionIEPS() != null){
                    cont++;
                    retDoc.append("PIR").append(cont).append(": DP").append((i + 1)).append("@003@").append(d.getRetensionIEPS().toString()).append("\r\n");
                }
                */
                
                //Traslados
                cont = 0;
                if(d.getTrasladoBaseIVA0() != null){
                    cont++;
                    traDoc.append("DPT").append(cont).append(": DP").append((i + 1)).append("@002@").append(d.getTrasladoBaseIVA0().toString()).append("@").append(d.getTrasladoImpuestoIVA0()).append("@Tasa@0.000000").append("\r\n");
                }
                if(d.getTrasladoBaseIVA8() != null){
                    cont++;
                    traDoc.append("DPT").append(cont).append(": DP").append((i + 1)).append("@002@").append(d.getTrasladoBaseIVA8().toString()).append("@").append(d.getTrasladoImpuestoIVA8()).append("@Tasa@0.080000").append("\r\n");
                }
                if(d.getTrasladoBaseIVA16() != null){
                    cont++;
                    traDoc.append("DPT").append(cont).append(": DP").append((i + 1)).append("@002@").append(d.getTrasladoBaseIVA16().toString()).append("@").append(d.getTrasladoImpuestoIVA16()).append("@Tasa@0.160000").append("\r\n");
                }
                if(d.getTrasladoBaseIVAExento() != null){
                    cont++;
                    traDoc.append("DPT").append(cont).append(": DP").append((i + 1)).append("@002@").append(p.getPagoTrasladosBaseIVAExento().toString()).append("@").append("0.00").append("@Exento@0.000000").append("\r\n");
                }
            }
        }
        re.append("[/DOCTOS_PAGOS]\r\n\r\n");
        
        //DOCTOS_IMPUESTOS_PAGOS
        re.append("[DOCTOS_PAGOS_RETENCIONES]\r\n");
        if(!retDoc.toString().trim().isEmpty())
            re.append(retDoc.toString());
        re.append("[/DOCTOS_PAGOS_RETENCIONES]\r\n\r\n");
        
        re.append("[DOCTOS_PAGOS_TRASLADOS]\r\n");
        if(!traDoc.toString().trim().isEmpty())
            re.append(traDoc.toString());
        re.append("[/DOCTOS_PAGOS_TRASLADOS]\r\n\r\n");

        return re.toString();
    }

    private String rellenar(String preFactura) {
        double porcentaje = 0.0;
        Elemento.log.info((Object) "Se comienza a llenar el Layout...");
        StringBuilder re = new StringBuilder();
        this.fecha = this.getFecha();
        BigDecimal porIeps;
        BigDecimal totalIeps;
        if(this.fact.porIeps != null){
            porIeps = util.redondear(this.fact.porIeps.divide(new BigDecimal("100")));
            totalIeps = this.fact.totalIeps;
        }

        re.append(preFactura).append("\r\n");
        re.append("[DATOS_EMISOR]\r\n");
        re.append("NOMBRE1: ").append(this.emisor.getNombre()).append("\r\n");
        re.append("REGIMENFISCAL: ").append(this.emisor.getRegimenFiscal()).append("\r\n");
        re.append("RFC1: ").append(this.emisor.getRfc()).append("\r\n");
        /*re.append("CALLE1: ").append(this.emisor.getCalle()).append("\r\n");
        re.append("NOEXTERIOR1: ").append(this.emisor.getNoExterior()).append("\r\n");
        re.append("NOINTERIOR1: ").append(this.emisor.getNoInterior()).append("\r\n");
        re.append("COLONIA1: ").append(this.emisor.getColonia()).append("\r\n");
        re.append("LOCALIDAD1: ").append(this.emisor.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO1: ").append(this.emisor.getMunicipio()).append("\r\n");
        re.append("ESTADO1: ").append(this.emisor.getEstado()).append("\r\n");
        re.append("PAIS1: ").append(this.emisor.getPais()).append("\r\n");
        re.append("CP1: ").append(this.emisor.getCp()).append("\r\n");*/
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
        re.append("DOMICILIOFISCAL: ").append(this.codigoPostal).append("\r\n");
        re.append("REGIMENFISCAL2: ").append(this.regimenFiscalReceptor).append("\r\n");
        re.append("NUMREGIDTRIB: ").append(this.numRegIdTrib).append("\r\n");
        re.append("RESIDENCIAFISCAL: ").append(this.pais).append("\r\n");
        re.append("USOCFDI: ").append(this.fact.usoCfdi).append("\r\n");
        /*re.append("CALLE2: ").append(this.fact.getCalle()).append("\r\n");
        re.append("NOEXTERIOR2: ").append(this.fact.getNumExt()).append("\r\n");
        re.append("NOINTERIOR2: ").append(this.fact.getNumInt()).append("\r\n");
        re.append("COLONIA2: ").append(this.fact.getColonia()).append("\r\n");
        re.append("LOCALIDAD2: ").append(this.fact.getLocalidad()).append("\r\n");
        re.append("MUNICIPIO2: ").append(this.fact.getMunicipio()).append("\r\n");
        re.append("ESTADO2: ").append(this.fact.getEstado()).append("\r\n");
        re.append("PAIS2: ").append(this.fact.getPais()).append("\r\n");
        re.append("CP2: ").append(this.fact.getCp()).append("\r\n");*/
        re.append("[/DATOS_RECEPTOR]\r\n\r\n");

        re.append("[DATOS_CFD]\r\n");
        re.append("FOLIO: ").append(this.folio).append("\r\n");
        re.append("SERIE: ").append(this.fact.getSerie()).append("\r\n");
        re.append("LUGAREXPEDICION: ").append(this.fact.getLugarExpedicion()).append("\r\n");
        re.append("TIPO_COMPROBANTE: ").append(this.fact.getTipoCfd()).append("\r\n");
        if (this.fact.cfdisAsociados != null && !this.fact.cfdisAsociados.trim().isEmpty()) {
            re.append("RELACIONCFDI: ").append(this.fact.cfdisAsociados).append("\r\n");
            re.append("TIPORELACION: ").append(this.fact.tipoRelacion).append("\r\n");
        }
        re.append("FORMAPAGO: ").append(this.fact.getFormaPago()).append("\r\n");
        re.append("CONDICIONPAGO: ").append(this.fact.getCondicionPago()).append("\r\n");
        re.append("METODOPAGO: ").append(this.fact.getMetodoPago()).append("\r\n");
        re.append("DESCUENTO: ").append(this.fact.getDescuento().toString()).append("\r\n");
        re.append("MOTIVODESCUENTO: _\r\n");
        re.append("MONEDA: ").append(this.fact.getMoneda()).append("\r\n");
        re.append("TIPOCAMBIO: ").append(new BigDecimal(this.fact.getTipoCambio())).append("\r\n");
        re.append("TOTALRETENIDOS: ").append(this.fact.getTotalRetenidos().toString()).append("\r\n");
        re.append("TOTALTRASLADOS: ").append(this.fact.getTotalTraslados().toString()).append("\r\n");
        re.append("SUBTOTAL: ").append(this.subtotal.toString()).append("\r\n");
        re.append("TOTALNETO: ").append(this.total.toString()).append("\r\n");
        re.append("LEYENDA: ").append(this.fact.getLeyenda()).append("\r\n");
        re.append("[/DATOS_CFD]\r\n\r\n");

        re.append("[CONCEPTOS]\r\n");
        for (String concepto : this.conceptos) {
            re.append(concepto).append("\r\n");
        }
        re.append("[/CONCEPTOS]\r\n\r\n");

        boolean writeIva = false;
        Map<String, Map<BigDecimal, ImpuestoTraslado>> trasladosBase = new HashMap();

        if (!fact.traslados.isEmpty()) {
            re.append("[TRASLADADOS_CONCEPTOS]\r\n");
            for (int i = 0; i < fact.traslados.size(); i++) {
                Factura.ConceptoTraslado tr = fact.traslados.get(i);
                if (tr.getTasa().compareTo(BigDecimal.ZERO) >= 0) {

                    if (!trasladosBase.containsKey(tr.getImpuesto())) {
                        Map<BigDecimal, ImpuestoTraslado> mit = new HashMap();
                        mit.put(tr.getTasa(), new ImpuestoTraslado(tr.getBase(), tr.getTasa(), util.redondear(tr.getImporte())));
                        trasladosBase.put(tr.getImpuesto(), mit);
                    } else {
                        Map<BigDecimal, ImpuestoTraslado> mit = trasladosBase.get(tr.getImpuesto());
                        if (mit.containsKey(tr.getTasa())) {
                            ImpuestoTraslado it = mit.get(tr.getTasa());
                            BigDecimal suma = it.getBase().add(tr.getBase());
                            BigDecimal sumaImporte = it.getImporte().add(util.redondear(tr.getImporte()));
                            it.setBase(util.redondear(suma));
                            it.setImporte(util.redondear(sumaImporte));
                            mit.replace(tr.getTasa(), it);
                        } else {
                            mit.put(tr.getTasa(), new ImpuestoTraslado(tr.getBase(), tr.getTasa(), redondear(tr.getImporte())));
                        }

                        trasladosBase.replace(tr.getImpuesto(), mit);
                    }

                    writeIva = true;

                    re.append("TC" + (i + 1) + ": ").append("C" + tr.getNumConcepto()).append("@").append(tr.getBase().toString())
                            .append("@").append(tr.getImpuesto()).append("@").append(tr.getTipoFactor()).append("@").append(tr.getTasa().toString())
                            //.append("@").append(util.redondear(tr.getImporte()).toString()).append("\r\n");
                            .append("@").append(tr.getImporte().toString()).append("\r\n");
                }
            }
            re.append("[/TRASLADADOS_CONCEPTOS]\r\n\r\n");
        }

        if (fact.retenciones.size() > 0 && !fact.tipoCfd.equals('N')) {
            re.append("[RETENCIONES_CONCEPTOS]\r\n");
            for (int i = 0; i < fact.retenciones.size(); i++) {
                Factura.ConceptoRetencion rr = fact.retenciones.get(i);
                re.append("RC" + (i + 1) + ": ").append("C" + rr.getNumConcepto()).append("@").append(rr.getBase().toString())
                        .append("@").append(rr.getImpuesto()).append("@").append(rr.getTipoFactor()).append("@").append(rr.getTasa().toString())
                        //.append("@").append(util.redondear(rr.getImporte()).toString()).append("\r\n");
                        .append("@").append(rr.getImporte().toString()).append("\r\n");
            }
            re.append("[/RETENCIONES_CONCEPTOS]\r\n\r\n");
        }

        if (fact.getDonataria() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaAuto = sdf.format(fact.getDonataria().getFechaAutorizacion());
            re.append("[DONATARIA]\r\n");
            re.append("FECHAAUTO: ").append(fechaAuto).append("\r\n");
            re.append("NOAUTO: ").append(fact.getDonataria().getNoAutorizacion()).append("\r\n");
            re.append("[/DONATARIA]\r\n");
        }

        re.append("[IMPUESTOS_TRASLADADOS]\r\n");
//        if (this.iva.doubleValue() == 0.0) {
//            porcentaje = 0.0;
//        } else {
//            porcentaje = 0.16;
//        }

        int contTras = 1;

        if (trasladosBase.containsKey("002")) {
            escribirTraslados("002", contTras, re, trasladosBase);
            
            contTras++;
        }

        if (trasladosBase.containsKey("003")) {
            escribirTraslados("003", contTras, re, trasladosBase);
        }
        
        re.append("[/IMPUESTOS_TRASLADADOS]\r\n\r\n");

        re.append("[IMPUESTOS_RETENIDOS]\r\n");
        if (this.fact.getIvaRetenido().compareTo(BigDecimal.ZERO) == 1) {
            re.append("IR1: 002@").append(this.fact.getIvaRetenido().toString()).append("\r\n");
        }
        if (this.fact.getIsrRetenido().compareTo(BigDecimal.ZERO) == 1) {
            re.append("IR2: 001@").append(this.fact.getIsrRetenido().toString()).append("\r\n");
        }
        re.append("[/IMPUESTOS_RETENIDOS]\r\n");
        this.texto = re.toString();
        return this.texto;
    }
    
    private void escribirTraslados(String impuesto, int contTras, StringBuilder re, Map<String, Map<BigDecimal, ImpuestoTraslado>> trasladosBase){
        Map<BigDecimal, ImpuestoTraslado> mip = trasladosBase.get(impuesto);
        Object its[] = mip.values().toArray();
        for (Object ot : its) {
            ImpuestoTraslado it = (ImpuestoTraslado) ot;
            if (it.getTasa().compareTo(BigDecimal.ZERO) == 1 || impuesto.equals("002")) {
                re.append("IT").append(contTras).append(": " + impuesto + "@Tasa@")
                        .append(it.getTasa().toString())
                        //.append("@").append(util.redondear(it.getImporte()).toString())
                        //.append("@").append(util.redondear(it.getBase()).toString()).append("\r\n");
                        .append("@").append(it.getImporte().toString())
                        .append("@").append(it.getBase().toString()).append("\r\n");
            }
        }
    }

    private void crearLayout(String texto) {
        Elemento.log.info("Se procede a crear el archivo Layout");
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
            OutputStreamWriter fw = new OutputStreamWriter((OutputStream) file, "UTF8");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(texto);
            bw.close();
            fw.close();
            file.close();
            Elemento.log.info((Object) "Archivo layout creado correctamente...");
        } catch (IOException e) {
            e.printStackTrace();
            Elemento.log.error((Object) ("Ocurrio la siguiente excepcion: " + e.getMessage()), (Throwable) e);
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

class ImpuestoTraslado {

    private BigDecimal base;
    private BigDecimal tasa;
    private BigDecimal importe;

    public ImpuestoTraslado(BigDecimal base, BigDecimal tasa, BigDecimal importe) {
        this.base = base;
        this.tasa = tasa;
        this.importe = importe;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
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

}
