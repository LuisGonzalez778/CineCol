package cinecol;
 
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
 
public class FrmCineCol extends javax.swing.JFrame {
 
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(FrmCineCol.class.getName());
 
    private static final String TODOS = "Todos";
    private static final int MAX_SUGERENCIAS = 10;
 
    // Boton creado por codigo (fuera de la zona generada por NetBeans,
    // asi el diseno no se pierde si abres/guardas la pestana Design).
    private JButton btnBuscar;
 
    // --- Autocompletado ---
    private JPopupMenu popupSugerencias;
    private JList<String> listaSugerencias;
    private DefaultListModel<String> modeloSugerencias;
    private final List<String> titulosCache = new ArrayList<>();
 
    public FrmCineCol() {
        initComponents();
        armarDiseno();
        configurarTabla();
        configurarAutocompletado();
 
        // Una sola conexion para traer generos y titulos, y de paso
        // comprobar que la base de datos responde.
        cargarDatosIniciales();
 
        btnBuscar.addActionListener(evt -> buscarPelicula());
    }
 
    /**
     * Reorganiza el diseno de la ventana:
     *
     *   [Nombre de la pelicula]      [Genero]
     *   [_____________________]      [_______]
     *   [       Buscar        ]
     *   +--------------------------------------------+
     *   | Titulo | Director | Ano | Duracion | Genero |
     *   +--------------------------------------------+
     */
    private void armarDiseno() {
        btnBuscar = new JButton("Buscar");
        btnBuscar.setToolTipText("Buscar peliculas por nombre");
 
        jLabel1.setText("Ingrese el nombre de la pelicula:");
        jLabel2.setText("Genero de la pelicula:");
 
        getContentPane().removeAll();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
 
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, 0, 640, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
 
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, 200, 291, Short.MAX_VALUE)
                .addContainerGap())
        );
 
        pack();
        setLocationRelativeTo(null);
    }
 
    /**
     * Configura las columnas del JTable en el orden solicitado:
     * Titulo - Director - Ano - Duracion - Genero
     */
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Titulo", "Director", "Ano", "Duracion", "Genero"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
 
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // La columna Ano se ordena como numero, no como texto
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };
        jTable1.setModel(modelo);
        jTable1.setAutoCreateRowSorter(true);
 
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(220); // Titulo
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(170); // Director
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);  // Ano
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(70);  // Duracion
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(110); // Genero
    }
 
    /**
     * Abre UNA conexion y con ella llena el combo de generos y la lista de
     * titulos usada por el autocompletado. Tambien sirve como comprobacion
     * de que la conexion realmente funciona.
     */
    private void cargarDatosIniciales() {
        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
        modeloCombo.addElement(TODOS);
 
        try (Connection conexion = CineCol.conectar()) {
 
            if (conexion == null || conexion.isClosed()) {
                setTitle("CineCol - SIN CONEXION");
                JOptionPane.showMessageDialog(this,
                        "No fue posible conectar con la base de datos CineCol.\n"
                      + "Revisa que el servidor MySQL este encendido, que el usuario\n"
                      + "y la contrasena en CineCol.java sean correctos y que el driver\n"
                      + "mysql-connector-j este agregado al proyecto.",
                        "Conexion fallida", JOptionPane.ERROR_MESSAGE);
                jComboBox1.setModel(modeloCombo);
                return;
            }
 
            setTitle("CineCol - conectado a " + conexion.getCatalog());
 
            // Generos, desde la tabla Genero
            try (PreparedStatement ps = conexion.prepareStatement(
                        "SELECT nombre_genero FROM Genero ORDER BY nombre_genero");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modeloCombo.addElement(rs.getString("nombre_genero"));
                }
            }
 
            // Titulos, para el autocompletado
            titulosCache.clear();
            try (PreparedStatement ps = conexion.prepareStatement(
                        "SELECT titulo FROM Pelicula ORDER BY titulo");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    titulosCache.add(rs.getString("titulo"));
                }
            }
 
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al cargar generos y titulos", e);
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos iniciales: " + e.getMessage());
        }
 
        jComboBox1.setModel(modeloCombo);
    }
 
    // ------------------------------------------------------------------
    // Autocompletado del nombre de la pelicula
    // ------------------------------------------------------------------
 
    private void configurarAutocompletado() {
        modeloSugerencias = new DefaultListModel<>();
        listaSugerencias = new JList<>(modeloSugerencias);
        listaSugerencias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaSugerencias.setFocusable(false);
 
        popupSugerencias = new JPopupMenu();
        popupSugerencias.setFocusable(false);
        popupSugerencias.setBorder(null);
        popupSugerencias.add(new JScrollPane(listaSugerencias));
 
        listaSugerencias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = listaSugerencias.locationToIndex(e.getPoint());
                if (fila >= 0) {
                    listaSugerencias.setSelectedIndex(fila);
                    aceptarSugerencia();
                }
            }
        });
 
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { programarSugerencias(); }
            @Override public void removeUpdate(DocumentEvent e) { programarSugerencias(); }
            @Override public void changedUpdate(DocumentEvent e) { programarSugerencias(); }
        });
 
        jTextField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if (popupSugerencias.isVisible()) {
                            moverSeleccion(1);
                            e.consume();
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (popupSugerencias.isVisible()) {
                            moverSeleccion(-1);
                            e.consume();
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                        if (popupSugerencias.isVisible() && listaSugerencias.getSelectedIndex() >= 0) {
                            aceptarSugerencia();
                        } else {
                            popupSugerencias.setVisible(false);
                            buscarPelicula();
                        }
                        e.consume();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        popupSugerencias.setVisible(false);
                        break;
                    default:
                        break;
                }
            }
        });
    }
 
    /** No se puede tocar la UI dentro del DocumentListener: se aplaza. */
    private void programarSugerencias() {
        SwingUtilities.invokeLater(this::actualizarSugerencias);
    }
 
    private void actualizarSugerencias() {
        String texto = jTextField1.getText().trim();
        modeloSugerencias.clear();
 
        if (texto.length() < 2) {
            popupSugerencias.setVisible(false);
            return;
        }
 
        String buscado = normalizar(texto);
        for (String titulo : titulosCache) {
            if (normalizar(titulo).contains(buscado)) {
                modeloSugerencias.addElement(titulo);
                if (modeloSugerencias.size() >= MAX_SUGERENCIAS) {
                    break;
                }
            }
        }
 
        if (modeloSugerencias.isEmpty()) {
            popupSugerencias.setVisible(false);
            return;
        }
 
        int alto = Math.min(modeloSugerencias.size(), MAX_SUGERENCIAS)
                 * listaSugerencias.getFixedCellHeight();
        if (alto <= 0) {
            alto = Math.min(modeloSugerencias.size(), MAX_SUGERENCIAS) * 20;
        }
        popupSugerencias.setPopupSize(jTextField1.getWidth(), alto + 6);
        listaSugerencias.setSelectedIndex(-1);
        popupSugerencias.show(jTextField1, 0, jTextField1.getHeight());
        jTextField1.requestFocusInWindow();
    }
 
    private void moverSeleccion(int paso) {
        int total = modeloSugerencias.size();
        if (total == 0) {
            return;
        }
        int actual = listaSugerencias.getSelectedIndex();
        int nuevo = actual + paso;
        if (nuevo < 0) {
            nuevo = total - 1;
        } else if (nuevo >= total) {
            nuevo = 0;
        }
        listaSugerencias.setSelectedIndex(nuevo);
        listaSugerencias.ensureIndexIsVisible(nuevo);
    }
 
    private void aceptarSugerencia() {
        String elegido = listaSugerencias.getSelectedValue();
        popupSugerencias.setVisible(false);
        if (elegido != null) {
            jTextField1.setText(elegido);
            buscarPelicula();
        }
    }
 
    /** Pasa a minusculas y quita tildes, para que "pajaros" encuentre "Pajaros". */
    private static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                                     .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinTildes.toLowerCase();
    }
 
    // ------------------------------------------------------------------
    // Busqueda
    // ------------------------------------------------------------------
 
    /**
     * Toma el texto del jTextField1 y lo compara con los titulos de la tabla
     * Pelicula. El nombre del genero se obtiene con un JOIN a la tabla Genero.
     * Si en el combo se elige un genero distinto de "Todos", tambien filtra por el.
     */
    private void buscarPelicula() {
        popupSugerencias.setVisible(false);
 
        String nombre = jTextField1.getText().trim();
        String genero = (String) jComboBox1.getSelectedItem();
        boolean filtrarGenero = genero != null && !TODOS.equals(genero);
 
        if (nombre.isEmpty() && !filtrarGenero) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de pelicula para buscar.");
            jTextField1.requestFocusInWindow();
            return;
        }
 
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
 
        String sql = "SELECT p.titulo, p.director, p.anio_estreno, p.duracion, g.nombre_genero "
                   + "FROM Pelicula p "
                   + "LEFT JOIN Genero g ON p.id_genero = g.id_genero "
                   + "WHERE p.titulo LIKE ?"
                   + (filtrarGenero ? " AND g.nombre_genero = ?" : "")
                   + " ORDER BY p.titulo";
 
        try (Connection conexion = CineCol.conectar()) {
 
            if (conexion == null) {
                JOptionPane.showMessageDialog(this, "No fue posible conectar con la base de datos.");
                return;
            }
 
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, "%" + nombre + "%");
                if (filtrarGenero) {
                    ps.setString(2, genero);
                }
 
                try (ResultSet rs = ps.executeQuery()) {
                    int filas = 0;
                    while (rs.next()) {
                        filas++;
                        modelo.addRow(new Object[]{
                            rs.getString("titulo"),
                            rs.getString("director"),
                            rs.getInt("anio_estreno"),
                            rs.getString("duracion"),
                            rs.getString("nombre_genero")
                        });
                    }
                    if (filas == 0) {
                        JOptionPane.showMessageDialog(this,
                                "No se encontraron peliculas con ese criterio.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al consultar la base de datos", e);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar la base de datos: " + e.getMessage());
        }
    }
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
 
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
 
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
 
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
 
        jLabel1.setText("Ingrese el nombre de la peliciula:");
 
        jLabel2.setText("Genero de la pelicula:");
 
        jTextField1.setText("");
 
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
 
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 247, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
 
        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
 
        java.awt.EventQueue.invokeLater(() -> new FrmCineCol().setVisible(true));
    }
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
 
