package Modelo;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
public class PersonaDAO {
    Conexion conectar = new Conexion(); // Clase conexión
        Connection con; // Conexión a la base de datos
        PreparedStatement ps; // Para ejecutar sentencias SQL
        ResultSet rs; // Para almacenar resultados de consultas

        // Método para listar los datos de las personas.
        public List<Persona> listar() {
            List<Persona> datos = new ArrayList<>();
            String sql = "SELECT id, nombre, correo, telefono FROM Persona"; // Consulta correcta

            try {
                con = conectar.getConnection(); // Obtener conexión
                ps = con.prepareStatement(sql); // Preparar consulta
                rs = ps.executeQuery(); // Ejecutar consulta

                // Recorrer los resultados y agregar a la lista
                while (rs.next()) {
                    Persona p = new Persona();
                    p.setId(rs.getInt("id")); // Usar el nombre correcto de la columna
                    p.setNombre(rs.getString("Nombre")); // Usar el nombre correcto de la columna
                    p.setCorreo(rs.getString("Correo")); // Usar el nombre correcto de la columna
                    p.setTelefono(rs.getString("Telefono")); // Usar el nombre correcto de la columna
                    datos.add(p); // Agregar objeto Persona a la lista
                }
            } catch (SQLException e) {
                System.out.println("Error SQL al listar: " + e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
            return datos;
        }

        // Método para agregar una nueva persona a la base de datos.
        public int Agregar(Persona p) {
        String sql = "INSERT INTO Persona (id, nombre, correo, telefono) VALUES (?, ?, ?, ?)";

        try {
            con = conectar.getConnection(); // Obtener conexión
            ps = con.prepareStatement(sql); // Preparar consulta

            // Establecer valores predeterminados para los campos que no se manejan en la vista
            ps.setString(1, p.getNombre()); // Nombre
            ps.setString(2, "Nombre generico"); // Nombre genérico
            ps.setString(3, p.getTelefono()); // Teléfono
            ps.setString(4, p.getCorreo()); // Correo
            ps.setInt(5, 1); // Id genérico 

            ps.executeUpdate(); // Ejecutar la inserción
        } catch (SQLException e) {
            System.out.println("Error SQL al agregar: " + e.getMessage());
            return 0; // Retornar 0 si hay error
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return 1; // Retornar 1 si se agrega correctamente
    }

        public int Editar(Persona p) {
        String sql = "UPDATE Persona SET nombre=?, correo=?, telefono=? WHERE id=?";

        try {
            con = conectar.getConnection(); // Obtener conexión
            ps = con.prepareStatement(sql); // Preparar consulta

            // Establecer los valores para la actualización
            ps.setString(1, p.getNombre());   // Nombre
            ps.setString(2, p.getCorreo());   // Correo
            ps.setString(3, p.getTelefono()); // Teléfono
            ps.setInt(4, p.getId());          // ID del empleado

            ps.executeUpdate(); // Ejecutar la actualización
        } catch (SQLException e) {
            System.out.println("Error SQL al editar: " + e.getMessage());
            return 0; // Retornar 0 si hay error
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return 1; // Retornar 1 si se actualiza correctamente
    }

        public void Eliminar(int id) {
        String sql = "DELETE FROM Persona WHERE id=?";

        try {
            con = conectar.getConnection(); // Obtener conexión
            ps = con.prepareStatement(sql); // Preparar consulta

            // Establecer el ID del empleado a eliminar
            ps.setInt(1, id); // Usar el Id para eliminar

            ps.executeUpdate(); // Ejecutar la eliminación
        } catch (SQLException e) {
            System.out.println("Error SQL al eliminar: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    public List<Persona> filtrar(String valor, String campo) {
	    List<Persona> lista = new ArrayList<>();
	    String sql = "SELECT * FROM persona WHERE " + campo + " LIKE ?";
	    try {
	        con = conectar.getConnection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, "%" + valor + "%");
	        rs = ps.executeQuery();
	        while (rs.next()) {
	            Persona p = new Persona();
	            p.setId(rs.getInt("id"));
	            p.setNombre(rs.getString("nombre"));
	            p.setCorreo(rs.getString("correo"));
	            p.setTelefono(rs.getString("telefono"));
	            lista.add(p);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return lista;
	}
}