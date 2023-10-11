package Interfaces.Factura;

public class DetalleFactura {
    private int idCliente;
    private int idEmpleado;
    private int idProducto;
    private int idProveedor;
    private int cantidad;
    private double precioProducto;
    private double subtotal;
    private double descuento;
    private double total;

    public DetalleFactura(int idCliente, int idEmpleado, int idProducto, int idProveedor, double precioProducto, double subtotal) {
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
        this.cantidad = cantidad;
        this.precioProducto = precioProducto;
        this.subtotal = subtotal;
    }

    public int getIDCliente() {
        return idCliente;
    }
    public int getIDEmpleado() {
        return idEmpleado;
    }
    public int getIdProducto() {
        return idProducto;
    }
    public int getIdProveedor() {
        return idProveedor;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double getPrecioProducto() {
        return precioProducto;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public double getDescuento() {
        return descuento;
    }
    public double getTotal() {
        return total;
    }
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}

