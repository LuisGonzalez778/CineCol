package cinecol;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * Clase encargada de manejar la conexion a la base de datos de CineCol.
 *
 * AJUSTA estos 3 valores segun tu configuracion real:
 *  - URL:        host, puerto y nombre de la base de datos
 *  - USUARIO:    usuario de MySQL
 *  - CONTRASENA: contrasena de ese usuario
 *
 * Requisito: tener el driver JDBC de MySQL (mysql-connector-j) agregado
 * como libreria/dependencia del proyecto.
 */
public class CineCol {
 
    // serverTimezone y allowPublicKeyRetrieval evitan los dos errores mas
    // comunes al conectar con MySQL 8 desde NetBeans.
    private static final String URL =
            "jdbc:mysql://localhost:3306/CineCol"
            + "?serverTimezone=America/Bogota"
            + "&useSSL=false"
            + "&allowPublicKeyRetrieval=true";
 
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "luis2026";
 
    private static final Logger LOGGER = Logger.getLogger(CineCol.class.getName());
 
    private CineCol() {
        // clase de utilidad: no se instancia
    }
 
    /**
     * Abre y devuelve una conexion a la base de datos.
     * Devuelve null si la conexion falla (revisa la consola/log para el detalle).
     */
    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "No se encontro el driver JDBC de MySQL en el classpath", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
        }
        return conexion;
    }
 
    /**
     * Devuelve un mensaje describiendo el estado de la conexion.
     * Sirve para comprobar desde el formulario que la conexion funciona.
     */
    public static String estadoConexion() {
        try (Connection c = conectar()) {
            if (c == null || c.isClosed()) {
                return "Sin conexion";
            }
            return "Conectado a " + c.getCatalog();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar la conexion", e);
            return "Sin conexion";
        }
    }
 
    /** Prueba rapida por consola, sin abrir la ventana. */
    public static void main(String[] args) {
        System.out.println(estadoConexion());
    }
    }
