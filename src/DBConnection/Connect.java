package DBConnection;
import javax.swing.*;
import java.sql.*;

public class Connect {
    private static Connection con;
    public static Connection getConnection() {
        return con;
    }

    // PROVEEDOR
    public int getProveedorIdPorNombre(String nombreProveedor) throws SQLException {
        String sql = "SELECT id FROM proveedores WHERE nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombreProveedor);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            throw new IllegalArgumentException("Proveedor no encontrado: " + nombreProveedor);
        }
    }

    public String getNombreTipoProveedorPorID(int idTipoProveedor) {
        String nombreTipoProveedor = null;
        String sql = "SELECT nombre FROM tipo_proveedor WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idTipoProveedor);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombreTipoProveedor = rs.getString("nombre");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el nombre del tipo de proveedor por ID: " + ex.getMessage());
            ex.printStackTrace();
        }

        return nombreTipoProveedor;
    }

    // PRODUCTO
    public String getNombreProductoPorId(int idProducto) {
        String nombreProducto = "";
        String sql = "SELECT nombre FROM producto WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombreProducto = rs.getString("nombre");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el nombre del producto por ID: " + ex.getMessage());
            ex.printStackTrace();
        }

        return nombreProducto;
    }
    public double getPrecioProducto(String nombreProducto) {
        double precio = 0;
        String sql = "SELECT precio FROM producto WHERE nombre = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                precio = rs.getDouble("precio");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el precio del producto: " + ex.getMessage());
            ex.printStackTrace();
        }

        return precio;
    }

    public int obtenerUltimoID(String nombreBaseDatos, String nombreTabla) {
        String sql = "SELECT MAX(id) FROM producto";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int maxID = rs.getInt(1);
                return maxID + 1;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo obtener el próximo ID desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el próximo ID desde la base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public int getIDProducto(String nombreProducto) {
        int idProducto = -1;
        String sql = "SELECT id FROM producto WHERE nombre = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idProducto = rs.getInt("id");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el ID del producto: " + ex.getMessage());
            ex.printStackTrace();
        }
        return idProducto;
    }
    public double getPrecioProductoPorId(int idProducto) {
        double precio = 0;
        String sql = "SELECT precio FROM producto WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                precio = rs.getDouble("precio");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el precio del producto por ID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return precio;
    }

    // CLIENTE
    public int getIDCliente(String nombreCliente) {
        int idCliente = 0;
        String sql = "SELECT id FROM cliente WHERE nombre = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idCliente = rs.getInt("id");
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener el ID del cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
        return idCliente;
    }

    // EMPLEADO
    public int getIDEmpleado(String nombreEmpleado) {
        int idEmpleado = 0;
        String sql = "SELECT id FROM empleado WHERE nombre = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreEmpleado);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idEmpleado = rs.getInt("id");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el ID del empleado: " + ex.getMessage());
            ex.printStackTrace();
        }
        return idEmpleado;
    }

    public String getNombreEmpleadoPorID(int idEmpleado) {

        String nombreEmpleado = null;
        String sql = "SELECT nombre FROM empleado WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idEmpleado);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombreEmpleado = rs.getString("nombre");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener el nombre del empleado: " + ex.getMessage());
            ex.printStackTrace();
        }
        return nombreEmpleado;
    }

    public String getNombreTipoEmpleadoPorID(int idTipoEmpleado) {
        String nombreTipoEmpleado = null;
        String sql = "SELECT nombre FROM tipo_empleado WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idTipoEmpleado);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nombreTipoEmpleado = rs.getString("nombre");
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener el nombre del tipo de empleado por ID: " + ex.getMessage());
            ex.printStackTrace();
        }

        return nombreTipoEmpleado;
    }

    public int getStockProducto(int idProducto) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int stock = 0;

        try {
            String sql = "SELECT stock FROM producto WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, idProducto);
            rs = stmt.executeQuery();

            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return stock;
    }

    public boolean actualizarStockProducto(int idProducto, int nuevoStock) throws SQLException {
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE producto SET stock = ? WHERE id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, idProducto);

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    public void conectar() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/facturaciono?&useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("Conexión exitosa");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
