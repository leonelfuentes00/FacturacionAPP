package DBConnection;

import Interfaces.Factura.DetalleFactura;

import java.sql.*;
import java.util.List;

public class FacturacionDB {
    public int insertarFacturaEncabezado(int idCliente, int idEmpleado, double subtotalTotal, double descuento, double total) throws SQLException {
        Connection connection = Connect.getConnection();
        String query = "INSERT INTO factura_encabezado (id_cliente, id_empleado, fecha_registro, subtotal, descuento, total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        try {
            statement.setInt(1, idCliente);
            statement.setInt(2, idEmpleado);
            statement.setDate(3, new Date(System.currentTimeMillis()));
            statement.setDouble(4, subtotalTotal);
            statement.setDouble(5, descuento);
            statement.setDouble(6, total);

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idFactura = generatedKeys.getInt(1);
                System.out.println("Factura encabezado se insertó satisfactoriamente con el id: " + idFactura);
                return idFactura;
            } else {
                throw new SQLException("la creacion factura encabezado failed, no se encontro id.");
            }
        } catch (SQLException ex) {
            System.out.println("Error insertando factura encabezado: " + ex.getMessage());
        }
        return 0;
    }
    public void insertarDetalleFactura(List<DetalleFactura> detalles, int idFactura) throws SQLException {
        Connection connection = Connect.getConnection();
        String query = "INSERT INTO factura_detalle (id_factura, id_producto, cantidad, precio_unitario, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        try {
            for (DetalleFactura detalle : detalles) {
                statement.setInt(1, idFactura);
                statement.setInt(2, detalle.getIdProducto());
                statement.setInt(3, detalle.getCantidad());
                statement.setDouble(4, detalle.getPrecioProducto());
                statement.setDouble(5, detalle.getSubtotal());

                statement.addBatch();
            }
            statement.executeBatch();
            System.out.println("Detalle factura inserted successfully");
        } catch (SQLException ex) {
            System.out.println("Error inserting detalle factura: " + ex.getMessage());
        }
    }
}