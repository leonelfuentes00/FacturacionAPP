package Interfaces.Factura;
import java.math.BigDecimal;
import java.sql.Date;

public class EncabezadoFactura {
    private static int idFactura;
    private static Date fechaFactura;
    private static BigDecimal total;

    public static int getIdFactura() {return idFactura;}
    public void setIdFactura(int idFactura) {this.idFactura = idFactura;}
    public static Date getFechaFactura() {return fechaFactura;}
    public void setFechaFactura(Date fechaFactura) {this.fechaFactura = fechaFactura;}
    public static BigDecimal getTotal() {return total;}
    public void setTotal(BigDecimal total) {this.total = total;}
}
