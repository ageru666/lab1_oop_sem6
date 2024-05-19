package main.webapp.Data;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UniversalDAO<T> {
    private final Connection connection;
    private final Class<T> clazz;

    public UniversalDAO(Connection connection, Class<T> clazz) {
        this.connection = connection;
        this.clazz = clazz;
    }

    public void create(T object) throws SQLException, IllegalAccessException {
        String tableName = clazz.getSimpleName() + 's';
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder values = new StringBuilder(") VALUES (");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.getName().equals("id")) {
                query.append(field.getName()).append(", ");
                values.append("?, ");
            }
        }
        query.delete(query.length() - 2, query.length());
        values.delete(values.length() - 2, values.length());
        query.append(values).append(")");

        try (PreparedStatement statement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    statement.setObject(i++, field.get(object));
                }
            }
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Field idField = clazz.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(object, generatedKeys.getInt(1));
                }
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public T findBy(String fieldName, Object value) throws SQLException, InstantiationException, IllegalAccessException {
        String tableName = clazz.getSimpleName() + 's';
        String query = "SELECT * FROM " + tableName + " WHERE " + fieldName + "=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, value);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                T object = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(object, resultSet.getObject(field.getName()));
                }
                return object;
            }
        }
        return null;
    }


    public List<T> getAll() throws SQLException, InstantiationException, IllegalAccessException {
        List<T> objects = new ArrayList<>();
        String tableName = clazz.getSimpleName();
        String query = "SELECT * FROM " + tableName + 's'; // костыль
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T object = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(object, resultSet.getObject(field.getName()));
                }
                objects.add(object);
            }
        }
        return objects;
    }

    public T getById(int id) throws SQLException, InstantiationException, IllegalAccessException {
        String tableName = clazz.getSimpleName() + 's';
        String query = "SELECT * FROM " + tableName + " WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                T object = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(object, resultSet.getObject(field.getName()));
                }
                return object;
            }
        }
        return null;
    }
    public T getById(int id, String fieldName) throws SQLException, InstantiationException, IllegalAccessException {
        String tableName = clazz.getSimpleName() + 's';
        String query = "SELECT * FROM " + tableName + " WHERE " + fieldName + "=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                T object = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(object, resultSet.getObject(field.getName()));
                }
                return object;
            }
        }
        return null;
    }

    public List<T> getAllById(int clientId, String fieldName) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> objects = new ArrayList<>();
        String tableName = clazz.getSimpleName() + 's';

        String query = "SELECT * FROM " + tableName + " WHERE " + fieldName + "=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T object = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = resultSet.getObject(field.getName());
                    if (value != null) {
                        field.set(object, value);
                    }
                }
                objects.add(object);
            }
        }
        return objects;
    }

    public List<T> getAllByClientId(int clientId) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> objects = new ArrayList<>();
        String tableName = clazz.getSimpleName() + 's';
        String fieldName = "clientid";

        String query = "SELECT * FROM " + tableName + " WHERE " + fieldName + "=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T object = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = resultSet.getObject(field.getName());
                    if (value != null) {
                        field.set(object, value);
                    }
                }
                objects.add(object);
            }
        }
        return objects;
    }


    public void update(T object) throws SQLException, IllegalAccessException {
        String tableName = clazz.getSimpleName() + 's';
        Field[] fields = clazz.getDeclaredFields();

        List<Field> fieldsWithoutId = Arrays.stream(fields)
                .filter(field -> !field.getName().equals("id"))
                .toList();

        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (Field field : fieldsWithoutId) {
            query.append(field.getName()).append("=?, ");
        }
        query.delete(query.length() - 2, query.length()).append(" WHERE id=?");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int i = 1;
            for (Field field : fieldsWithoutId) {
                field.setAccessible(true);
                statement.setObject(i++, field.get(object));
            }
            Field idField = clazz.getDeclaredField("id");
            idField.setAccessible(true);
            statement.setObject(i, idField.get(object));
            statement.executeUpdate();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(int id) throws SQLException {
        String tableName = clazz.getSimpleName() + 's';
        String query = "DELETE FROM " + tableName + " WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}