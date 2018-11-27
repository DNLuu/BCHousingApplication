import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicantManager {
    public static void applicantMain(Connection conn) throws SQLException {
        boolean done = false;
        printApplicantPage();
        System.out.print("Type in your option: ");
        System.out.flush();
        String ch = readLine();
        System.out.println();
        switch (ch.charAt(0)) {
            case '1':
                addNewApplicant(conn);
                break;
            case '2':
            	 System.out.println("Not required to implement this.");
                break;
            case '3':
            	System.out.println("Not required to implement this.");
                break;
            case '4':
            	System.out.println("Not required to implement this.");
            	break;
            case '5':
                done = true;
                break;
            default:
                System.out.println(" Not a valid option.");
        }
    }
    
    private static void printApplicantPage() {
        System.out.println("***********************************************************");
        System.out.println("                     ***************                       ");
        System.out.println("                      Applicant Menu                       ");
        System.out.println("                     ***************                       ");
        System.out.println("***********************************************************");
        System.out.println("              1. Update Applicant Information              ");
        System.out.println("                2. Submit New Application                  ");
        System.out.println("              3. Delete Applicant Information              ");
        System.out.println("               4. Check Housing Availability               ");
        System.out.println("                        5. Quit                            ");
    }


    private static void addNewApplicant(Connection conn) throws SQLException {
        String idNumber = null;
        String name, gender, college, department, maritalStatus;
        String roomSSN = null;
        name = readEntry("Enter your name: ");
        gender = readEntry("Enter your gender: ");
        college = readEntry("Enter your college: ");
        department = readEntry("Enter your department: ");
        maritalStatus = readEntry("Enter marital status: ");

        String ssn, suitePreference, apartmentPreference, villagePreference, date;
        ssn = readEntry("Enter ssn: ");
        suitePreference = readEntry("Enter suite preference (One Bedroom or Two Bedroom): ");
        apartmentPreference = readEntry("Enter apartment preference (Two Bedroom or Four Bedroom): ");
        villagePreference = readEntry("Enter village preference (East or West): ");
        
        System.out.println();
        System.out.print("Would you like to request a roommate (y/n)?: ");
        System.out.flush();
        String ch = readLine();
        System.out.println();
        switch (ch.charAt(0)) {
            case 'y':
                roomSSN = readEntry("Enter roommate SSN: ");                   // should there be another way to track the roommate?
                break;                                                         // Should Spouse field be a boolean instead?
            case 'n':
                break;
            default:
                System.out.println(" Not a valid option.");
        }

        String maxIdQuery = "SELECT MAX(IdNumber) FROM Students";

        PreparedStatement p = conn.prepareStatement(maxIdQuery);
        ResultSet r = p.executeQuery();
        while (r.next()) {
            idNumber = r.getString(1);
        }

        String query = "INSERT INTO Students VALUES "
                + "(1 + " + idNumber + ",'" + name + "','" + gender + "','"
                + college + "','" + department + "','" + maritalStatus + "')";

        p = conn.prepareStatement(query);
        p.executeUpdate();

        query = "INSERT INTO Applicant VALUES "
                + "(" + ssn + ",'Applied','" + suitePreference + "','" + apartmentPreference
                + "','" + villagePreference + "'," + "NOW()" + ")";                // NOW() gets current date

        p = conn.prepareStatement(query);
        p.executeUpdate();
        
        if (roomSSN != null) {
        	query = "INSERT INTO RoommateRequest VALUES"
        			+ "(" + roomSSN + "," + ssn + ",'" + "NULL" + "')";
        	
        	p = conn.prepareStatement(query);
        	p.executeUpdate();
        }
        
        System.out.println();
        System.out.println("              Application successfully submitted           ");
        System.out.println();
    }
    
    private static void addRoommate(Connection conn) throws SQLException {
    	
    }
    
//    private static void updateApplicant(Connection conn) throws SQLException {
//    	
//    }
//    
//    private static void printUpdateMenu() {
//    	
//    }


    static String readEntry(String prompt) {
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while (c != '\n' && c != -1) {
                buffer.append((char) c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

    private static String readLine() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr, 1);
        String line = "";

        try {
            line = br.readLine();
        } catch (IOException e) {
            System.out.println("Error in SimpleIO.readLine: " + "IOException was thrown");
            System.exit(1);
        }
        return line;
    }
}
