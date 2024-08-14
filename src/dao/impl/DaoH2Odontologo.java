package dao.impl;

import dao.IDao;
import db.H2Connection;
import model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoH2Odontologo implements IDao<Odontologo> {
    private static final Logger logger = Logger.getLogger(DaoH2Odontologo.class);
    private static final String INSERT = "INSERT INTO ODONTOLOGOS (NUMERODEMATRICULA, NOMBRE, APELLIDO) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM ODONTOLOGOS";

    @Override
    public Odontologo guardar(Odontologo odontologo) {
        try (Connection connection = H2Connection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, odontologo.getNumeroDeMatricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.executeUpdate();
            connection.commit();

            // Recuperamos la key generada
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                Integer id = null;
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                Odontologo odontologoARetornar = new Odontologo(id, odontologo.getNumeroDeMatricula(), odontologo.getNombre(), odontologo.getApellido());
                logger.info("Odontólogo persistido: " + odontologoARetornar);
                return odontologoARetornar;
            }
        } catch (SQLException e) {
            logger.error("Error al guardar odontólogo: ", e);
            try (Connection connection = H2Connection.getConnection()) {
                connection.rollback();
            } catch (SQLException rollbackException) {
                logger.error("Error al hacer rollback: ", rollbackException);
            }
            return null;
        }
    }

    @Override
    public List<Odontologo> listarTodos() {
        List<Odontologo> odontologos = new ArrayList<>();
        try (Connection connection = H2Connection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("ID"); // Asegúrate de que el nombre de la columna sea correcto
                Integer numeroMatricula = resultSet.getInt("NUMERODEMATRICULA");
                String nombre = resultSet.getString("NOMBRE");
                String apellido = resultSet.getString("APELLIDO");
                Odontologo odontologo = new Odontologo(id, numeroMatricula, nombre, apellido);
                logger.info("Odontólogo encontrado: " + odontologo);
                odontologos.add(odontologo);
            }

        } catch (SQLException e) {
            logger.error("Error al listar odontólogos: ", e);
        }
        return odontologos;
    }
}

