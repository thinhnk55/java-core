package work.vietdefi.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SqlJavaBridge provides an implementation of the ISQLJavaBridge interface,
 * allowing interaction with SQL databases using HikariCP for connection management.
 * This class includes methods for checking table existence, creating tables,
 * executing queries, and converting query results to JSON format.
 */
public class SqlJavaBridge implements ISQLJavaBridge {
    // HikariClient instance used to manage database connections efficiently.
    private final HikariClient hikariClient;

    // Constructor to initialize the HikariClient with the given configuration file path.
    public SqlJavaBridge(HikariClient hikariClient) {
        this.hikariClient = hikariClient;
    }

    /**
     * Checks if a specified table exists in the database.
     *
     * @param table The name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    @Override
    public boolean checkTableExisting(String table) {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        try (Connection connection = hikariClient.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, table);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Return true if the count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }
        return false; // Table does not exist
    }

    /**
     * Creates a new table in the database and optionally creates indexes for it.
     * This method executes the provided SQL statement to create a table.
     * If index creation SQL statements are provided, it will execute those
     * as well. The entire operation is performed within a transaction.
     * If any part of the process fails, the transaction is rolled back
     * to ensure database integrity.
     *
     * @param createTableSQL The SQL statement used to create the table.
     * @param createIndexSql Optional SQL statements to create indexes on the table.
     * @return true if the table and indexes are created successfully,
     *         false if an error occurs during the operation.
     */
    @Override
    public boolean createTable(String createTableSQL, String... createIndexSql) {
        Connection connection = null; // Declare the connection variable outside the try block
        try {
            // Obtain a connection from the HikariClient to interact with the database
            connection = hikariClient.getConnection();
            // Set auto-commit false to manage transactions manually
            connection.setAutoCommit(false);

            // Prepare the SQL statement for creating the table
            try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
                // Execute the SQL statement to create the table
                statement.executeUpdate(); // This will throw an exception if the creation fails
            }

            // If there are index creation SQL statements provided
            for (String indexSQL : createIndexSql) {
                // Prepare the SQL statement for creating the index
                try (PreparedStatement indexStatement = connection.prepareStatement(indexSQL)) {
                    // Execute the SQL statement to create the index
                    indexStatement.executeUpdate(); // This will throw an exception if the index creation fails
                }
            }

            // If all operations are successful, commit the transaction
            connection.commit(); // Save changes to the database
            return true; // Return true indicating successful creation of the table and indexes
        } catch (SQLException e) {
            // Print the stack trace for debugging if an SQL exception occurs
            e.printStackTrace();
            // Attempt to roll back the transaction in case of error to maintain database integrity
            try {
                if (connection != null) { // Check if the connection is not null before attempting rollback
                    connection.rollback(); // Revert all changes made during the transaction
                }
            } catch (SQLException rollbackException) {
                // Print the stack trace for rollback exceptions
                rollbackException.printStackTrace();
            }
        } finally {
            // Ensure the connection is closed after operations are complete
            if (connection != null) {
                try {
                    connection.close(); // Close the connection to release database resources
                } catch (SQLException closeException) {
                    closeException.printStackTrace(); // Print the stack trace if closing fails
                }
            }
        }
        // Return false indicating failure to create the table or indexes due to an error
        return false;
    }


    /**
     * Converts a single row from the ResultSet to a JsonObject.
     *
     * @param resultSet The ResultSet containing the query result.
     * @return A JsonObject representing the row data.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    @Override
    public JsonObject convertResultSetToJsonObject(ResultSet resultSet) throws SQLException {
        JsonObject jsonObject = new JsonObject(); // Create a new JsonObject
        int columnCount = resultSet.getMetaData().getColumnCount(); // Get the number of columns
        // Loop through each column and add it to the JsonObject
        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            jsonObject.addProperty(columnName, resultSet.getString(i));
        }
        return jsonObject; // Return the populated JsonObject
    }

    /**
     * Converts the entire ResultSet to a JsonArray, where each row is represented as a JsonObject.
     *
     * @param resultSet The ResultSet containing multiple rows of query results.
     * @return A JsonArray representing the query result.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    @Override
    public JsonArray convertResultSetToJsonArray(ResultSet resultSet) throws SQLException {
        JsonArray jsonArray = new JsonArray(); // Create a new JsonArray
        // Loop through each row in the ResultSet and convert it to JsonObject
        while (resultSet.next()) {
            jsonArray.add(convertResultSetToJsonObject(resultSet));
        }
        return jsonArray; // Return the populated JsonArray
    }

    /**
     * Executes a query that is expected to return a single row and converts the result to a JsonObject.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return A JsonObject representing the single row result, or null if no result is found.
     * @throws Exception If the query execution or conversion fails.
     */
    @Override
    public JsonObject queryOne(String query, Object... params) throws Exception {
        try (Connection connection = hikariClient.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params); // Bind parameters to the statement
            ResultSet resultSet = statement.executeQuery(); // Execute the query
            if (resultSet.next()) {
                return convertResultSetToJsonObject(resultSet); // Convert result to JsonObject
            }
        }
        return null; // No results found
    }

    /**
     * Executes a query that returns multiple rows and converts the result to a JsonArray.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return A JsonArray containing all rows of the query result.
     * @throws Exception If the query execution or conversion fails.
     */
    @Override
    public JsonArray queryArray(String query, Object... params) throws Exception{
        try (Connection connection = hikariClient.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params); // Bind parameters to the statement
            ResultSet resultSet = statement.executeQuery(); // Execute the query
            return convertResultSetToJsonArray(resultSet); // Convert result set to JsonArray
        }
    }

    /**
     * Executes an INSERT statement and returns the generated key.
     *
     * @param query  The SQL INSERT query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return The generated key from the insert operation (e.g., auto-increment ID), or null if no key is generated.
     * @throws Exception If the insertion or key retrieval fails.
     */
    @Override
    public Object insert(String query, Object... params) throws Exception {
        try (Connection connection = hikariClient.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setParameters(statement, params); // Bind parameters to the statement
            statement.executeUpdate(); // Execute the insert statement
            ResultSet generatedKeys = statement.getGeneratedKeys(); // Retrieve generated keys
            if (generatedKeys.next()) {
                return generatedKeys.getObject(1); // Return the first generated key
            }
        }
        return null; // No key generated
    }

    /**
     * Executes an UPDATE or DELETE statement and returns the number of rows affected.
     *
     * @param query  The SQL UPDATE/DELETE query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return The number of rows affected by the operation.
     * @throws Exception If the query execution fails.
     */
    @Override
    public int update(String query, Object... params) throws Exception {
        try (Connection connection = hikariClient.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params); // Bind parameters to the statement
            return statement.executeUpdate(); // Return the number of rows affected
        }
    }

    /**
     * Sets the parameters for a PreparedStatement.
     *
     * @param statement The PreparedStatement to set parameters on.
     * @param params    The parameters to bind to the statement.
     * @throws SQLException If an error occurs while setting parameters.
     */
    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        // Loop through each parameter and set it in the PreparedStatement
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]); // Set parameter at index (i + 1) because PreparedStatement is 1-indexed
        }
    }

    /**
     * Closes the HikariClient and releases all database connections.
     * <p>
     * This method should be called when the SqlJavaBridge instance is no longer needed
     * to ensure that all database connections are closed and resources are released.
     * Failure to call this method may result in memory leaks or exhaustion of database connections.
     * </p>
     */
    public void close() {
        if (hikariClient != null) {
            hikariClient.close(); // Close the HikariClient to release resources
        }
    }
}

