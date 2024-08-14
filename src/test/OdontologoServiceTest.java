package test;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OdontologoService;
import dao.impl.DaoH2Odontologo;
import model.Odontologo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OdontologoServiceTest {
    private static final Logger logger = Logger.getLogger(OdontologoServiceTest.class);
    private static OdontologoService odontologoService;

    @BeforeAll
    static void setup() {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:./clinica_odontologica;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
            odontologoService = new OdontologoService(new DaoH2Odontologo());
            connection.close(); // Cierra la conexión si no es necesaria en la configuración
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Error en la configuración de la base de datos: ", e);
            fail("Error en la configuración de la base de datos");
        }
    }

    @Test
    @DisplayName("Testear que un odontólogo se guardó en la BD")
    void testGuardarOdontologo() {
        // Dado
        Odontologo odontologo = new Odontologo(12345, "Juan", "Pérez");
        // Cuando
        Odontologo odontologoDesdeLaDB = odontologoService.guardar(odontologo);
        // Entonces
        assertNotNull(odontologoDesdeLaDB, "El odontólogo guardado no debería ser nulo.");
        assertNotNull(odontologoDesdeLaDB.getId(), "El ID del odontólogo guardado no debería ser nulo.");
    }

    @Test
    @DisplayName("Testear que me traiga todos los odontólogos guardados")
    void testListarTodos() {
        // Cuando
        List<Odontologo> odontologos = odontologoService.listarTodos();
        // Entonces
        assertNotNull(odontologos, "La lista de odontólogos no debería ser nula.");
        assertTrue(odontologos.size() > 0, "La lista de odontólogos debería contener al menos un odontólogo.");
    }
}
