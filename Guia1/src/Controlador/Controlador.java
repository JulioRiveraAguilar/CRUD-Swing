package Controlador;
import Modelo.PersonaDAO;
import Modelo.Persona;
import Vista.Vista; 
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.util.List;
import javax.swing.JOptionPane;

public class Controlador implements ActionListener {

    PersonaDAO dao = new PersonaDAO();
    Persona p = new Persona();
    Vista V = new Vista();
    DefaultTableModel modelo = new DefaultTableModel();

    public Controlador(Vista V) {
        this.V = V;
        this.V.btnListar.addActionListener(this);
        this.V.btnGuardar.addActionListener(this);
        this.V.btnEditar.addActionListener(this); 
        this.V.btnEliminar.addActionListener(this);
        this.V.btnCerrar.addActionListener(this);
        this.V.BtnAplicarFiltro.addActionListener(this);
        this.V.BtnFiltrar.addActionListener(this);
    }
    @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == V.btnListar) {
                Listar(V.jTable2);
            }
            if (e.getSource() == V.btnGuardar) {
                agregar();
            }
            if (e.getSource() == V.btnEditar) {
                editar();
            }
            if (e.getSource() == V.btnEliminar) {
                eliminar();
            }
            if (e.getSource() == V.btnCerrar) {
                cerrarVentana();
            }
            if (e.getSource() == V.BtnAplicarFiltro) {
                String filterOption = (String) V.BtnFiltrar.getSelectedItem();
                switch (filterOption) {
                    case "Nombre":
                        filtrarPorNombre(V.jTable2);
                        break;
                    case "Teléfono":
                        filtrarPorTelefono(V.jTable2);
                        break;
                    case "Correo":
                        filtrarPorCorreo(V.jTable2);
                        break;
                    default:
                        JOptionPane.showMessageDialog(V, "Opción de filtro no válida");
                        break;
                }
            }
        }
        public void Listar(JTable tabla) {
        modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas
        List<Persona> lista = dao.listar(); 

        Object[] object = new Object[4];

        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getId();
            object[1] = lista.get(i).getNombre();
            object[2] = lista.get(i).getCorreo();
            object[3] = lista.get(i).getTelefono();

            modelo.addRow(object);
        }
    }
        public void agregar() {
            String nom = V.tfNombre.getText();
            String corr = V.tfCorreo.getText();
            String tel = V.tfTelefono.getText();

            if (nom.isEmpty() || corr.isEmpty() || tel.isEmpty()) {
                JOptionPane.showMessageDialog(V, "Todos los campos deben ser completados");
                return; // Salir del método si hay campos vacíos
            }
            p.setNombre(nom);
            p.setCorreo(corr);
            p.setTelefono(tel);

            int respuesta = dao.Agregar(p);

            if (respuesta == 1) {
                JOptionPane.showMessageDialog(V, "Usuario Agregado");
                Listar(V.jTable2); // Listar después de agregar
            } else {
                JOptionPane.showMessageDialog(V, "Error al agregar el usuario");
            }
        }

        public void editar() {
        int fila = V.jTable2.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(V, "Debe seleccionar una fila");
            return;
        }
        int id = Integer.parseInt(V.jTable2.getValueAt(fila, 0).toString());
        String nom = V.tfNombre.getText();
        String corr = V.tfCorreo.getText();
        String tel = V.tfTelefono.getText();
        if (nom.isEmpty() || corr.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(V, "Todos los campos deben ser completados");
            return;
        }
        // Configurar los nuevos valores
        p.setId(id);
        p.setNombre(nom);
        p.setCorreo(corr);
        p.setTelefono(tel);
        // Ejecutar la actualización
        int respuesta = dao.Editar(p);
        if (respuesta == 1) {
            JOptionPane.showMessageDialog(V, "Usuario a sido editado");
            Listar(V.jTable2); // Refrescar la tabla
        } else {
            JOptionPane.showMessageDialog(V, "No se pudo ediar");
        }
    }
        public void eliminar() {
        int fila = V.jTable2.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(V, "Seleccione una fila");
            return;
        }
        int id = Integer.parseInt(V.jTable2.getValueAt(fila, 0).toString());
        int respuesta = JOptionPane.showConfirmDialog(V, "¿Esta seguro de eliminar este empleado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            dao.Eliminar(id);
            JOptionPane.showMessageDialog(V, "El Empleado a sido eliminado");
            Listar(V.jTable2);
        }
    }
        public void cerrarVentana() {
        V.dispose();
    }
        public void filtrarPorNombre(JTable tabla) {
	    String nombre = V.tfNombre.getText();
	    filtrar(tabla, nombre, "nombre");
	}
	
	public void filtrarPorTelefono(JTable tabla) {
	    String telefono = V.tfTelefono.getText();
	    filtrar(tabla, telefono, "telefono");
	}
	
	public void filtrarPorCorreo(JTable tabla) {
	    String correo = V.tfCorreo.getText();
	    filtrar(tabla, correo, "correo");
	}
        public void filtrar(JTable tabla, String valor, String campo) {
	    modelo = (DefaultTableModel) tabla.getModel();
	
	    while (modelo.getRowCount() > 0) {
	        modelo.removeRow(0);
	    }
	
	    List<Persona> lista = (List<Persona>) dao.filtrar(valor, campo);
	    Object[] object = new Object[4];
	
	    for (int i = 0; i < lista.size(); i++) {
	        object[0] = lista.get(i).getId();
	        object[1] = lista.get(i).getNombre();
	        object[2] = lista.get(i).getCorreo();
	        object[3] = lista.get(i).getTelefono();
	
	        modelo.addRow(object);
	    }
	
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No se encontraron resultados");
	    }
	}
}