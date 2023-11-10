package it.voltats.gestionepista.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {
    // Settings
    private static final String DB_URL = "jdbc:sqlite:gestione_pista.sqlite";

    // Connection
    private static Connection connection;

    private DbHelper() {
        try {
            // create a connection to the database
            connection = DriverManager.getConnection(DB_URL);



            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    // create table if not exists
                    final String USER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS user (\n"
                            + "	id integer PRIMARY KEY,\n"
                            + "	name text NOT NULL,\n"
                            + "	surname text NOT NULL,\n"
                            + "	email text NOT NULL UNIQUE,\n"
                            + "	phone_number text NOT NULL,\n"
                            + "	cf text NOT NULL UNIQUE,\n"
                            + " birthdate text NOT NULL,\n"
                            + " gender text NOT NULL\n"
                            + ");";

                    // create table if not exists
                    final String BOOKING_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS booking (\n"
                            + "	id integer PRIMARY KEY,\n"
                            + "	user_id integer NOT NULL,\n"
                            + "	start_date text NOT NULL,\n"
                            + "	end_date text NOT NULL,\n"
                            + "	status text NOT NULL,\n"
                            + " price real NOT NULL\n"
                            + ");";


                    connection.createStatement().execute(USER_TABLE_QUERY);
                    connection.createStatement().execute(BOOKING_TABLE_QUERY);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            new DbHelper();
        }
        return connection;
    }
}
