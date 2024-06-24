package org.LP;

import org.LP.generator.DataWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class DataWriterTest {

    @Before
    public void setUp() throws IOException {
        DataWriter.clearFile(DataWriter.sqlFilePath);
    }

    @Test
    public void testCreateSQLFile() {
        DataWriter.createSQLFile();
        File file = new File(DataWriter.sqlFilePath);
        assertTrue(file.exists());
    }

    @Test
    public void testGenerateHouseData() throws IOException {
        DataWriter.createSQLFile();
        DataWriter.generateHouseData();
        try (BufferedReader reader = new BufferedReader(new FileReader(DataWriter.sqlFilePath))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("INSERT INTO Houses")) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testGenerateStudentData() throws IOException {
        DataWriter.createSQLFile();
        DataWriter.generateStudentData();
        try (BufferedReader reader = new BufferedReader(new FileReader(DataWriter.sqlFilePath))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("INSERT INTO Students")) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testCheckConnectionDB() {
        try {
            DataWriter.checkConnectionDB();
        } catch (Exception e) {
            fail("Database connection failed");
        }
    }

    @Test
    public void testFileToDatabase() {
        try {
            DataWriter.createSQLFile();
            DataWriter.generateHouseData();
            DataWriter.generateStudentData();
            DataWriter.fileToDatabase();

            Connection connection = DriverManager.getConnection(DataWriter.getUrl(), DataWriter.getUser(), DataWriter.getPassword());
            Statement statement = connection.createStatement();

            // Check if tables are created
            statement.execute("SELECT 1 FROM Houses LIMIT 1");
            statement.execute("SELECT 1 FROM Students LIMIT 1");

            connection.close();
        } catch (Exception e) {
            fail("File to database insertion failed: " + e.getMessage());
        }
    }

    @Test
    public void testFullWorkflow() {
        try {
            DataWriter.createSQLFile();
            DataWriter.generateHouseData();
            DataWriter.generateStudentData();
            DataWriter.fileToDatabase();

            Connection connection = DriverManager.getConnection(DataWriter.getUrl(), DataWriter.getUser(), DataWriter.getPassword());
            Statement statement = connection.createStatement();

            // Check if tables have data
            ResultSet housesRs = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM Houses");
            housesRs.next();
            int houseCount = housesRs.getInt("rowcount");
            assertTrue(houseCount > 0);

            ResultSet studentsRs = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM Students");
            studentsRs.next();
            int studentCount = studentsRs.getInt("rowcount");
            assertTrue(studentCount > 0);

            connection.close();
        } catch (Exception e) {
            fail("Full workflow test failed: " + e.getMessage());
        }
    }
}
