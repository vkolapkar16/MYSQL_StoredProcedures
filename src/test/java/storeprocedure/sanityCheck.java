package storeprocedure;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class sanityCheck
{
	Connection con = null;
	Statement stmt = null;
	ResultSet rs;
	CallableStatement cStmt;
	ResultSet rs1;
	
	long millis = System.currentTimeMillis();  
	 java.util.Date date = new java.util.Date(millis);      
	
	String Filepath ="/home/vk/Documents/PIAB DOC/CSV FILES/results"+date+".csv";
	String Filepath1="/home/vk/Documents/PIAB DOC/CSV FILES/resultsTAB.tsv";
	
	@BeforeClass
	void setup() throws SQLException
	{
		con= DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","Vish8888@");		
	}
	
	@AfterClass
	void teardown() throws SQLException
	{
		con.close();		
	}
	
	@Test (priority = 1)
	void checkStoredProcedureStatus() throws SQLException
	{
		stmt = con.createStatement();
		rs=stmt.executeQuery("SHOW PROCEDURE STATUS WHERE Name='DateValidateData'");
		rs.next();
		
		System.out.println(rs.getString("Name"));
	}
	
	@Test (priority = 2)
	void sanityCheckforElectricity() throws SQLException, IOException
	{
		cStmt=con.prepareCall("{call S2ElectricityValidateData()}");
		rs1=cStmt.executeQuery();
		exportToCSV(rs1, Filepath);
		System.out.println("Data exported successfully to " + Filepath);
	}
	
	
	
	
	private static void exportToCSV(ResultSet resultSet, String filePath) throws IOException, SQLException {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            // Write column headers
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                csvWriter.append(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");

            // Write data rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    csvWriter.append(resultSet.getString(i));
                    if (i < columnCount) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }
        }
    }
	
	private static void exportToTSV(ResultSet resultSet, String filePath) throws IOException, SQLException {
        try (FileWriter tsvWriter = new FileWriter(filePath)) {
            // Write column headers based on result set metadata
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tsvWriter.append(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) {
                    tsvWriter.append("\t"); // Use tab as the delimiter
                }
            }
            tsvWriter.append("\n");

            // Write data rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    tsvWriter.append(resultSet.getString(i));
                    if (i < columnCount) {
                        tsvWriter.append("\t"); // Use tab as the delimiter
                    }
                }
                tsvWriter.append("\n");
            }
        }
    }
}

