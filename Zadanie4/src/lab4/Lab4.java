package lab4;

import java.sql.*;
import java.util.Scanner;

public class Lab4 {
    private static final String URL = "jdbc:mysql://localhost:3306/osoba";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Połączenie z bazą danych zostało nawiązane!");

            while (true) {
                System.out.println("\nWybierz opcję:");
                System.out.println("1. Wyświetl dostępne tabele");
                System.out.println("2. Wyświetl zawartość tabeli");
                System.out.println("3. Dodaj dane do tabeli");
                System.out.println("4. Usuń dane z tabeli");
                System.out.println("5. Dodaj przykładowe dane do tabeli 'studenci'");
                System.out.println("6. Wyjdź");

                int option = scanner.nextInt();
                scanner.nextLine(); // konsumuj nową linię

                switch (option) {
                    case 1 -> displayTables(conn);
                    case 2 -> {
                        System.out.print("Podaj nazwę tabeli: ");
                        String tableName = scanner.nextLine();
                        displayTableContent(conn, tableName);
                    }
                    case 3 -> {
                        System.out.print("Podaj nazwę tabeli: ");
                        String tableName = scanner.nextLine();
                        addDataToTable(conn, tableName, scanner);
                    }
                    case 4 -> {
                        System.out.print("Podaj nazwę tabeli: ");
                        String tableName = scanner.nextLine();
                        deleteDataFromTable(conn, tableName, scanner);
                    }
                    case 5 -> addSampleDataToStudents(conn);
                    case 6 -> {
                        System.out.println("Zakończono działanie programu.");
                        return;
                    }
                    default -> System.out.println("Nieprawidłowa opcja, spróbuj ponownie.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayTables(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        System.out.println("Dostępne tabele:");
        while (tables.next()) {
            System.out.println(tables.getString("TABLE_NAME"));
        }
    }

    private static void displayTableContent(Connection conn, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        }
    }

    private static void addDataToTable(Connection conn, String tableName, Scanner scanner) throws SQLException {
        System.out.print("Podaj wartości do wstawienia (oddzielone przecinkami i spacją): ");
        String values = scanner.nextLine();
        String[] valuesArray = values.split(",\\s*");

        String query = "INSERT INTO " + tableName + " (imie, nazwisko, wiek) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, valuesArray[0]);
            pstmt.setString(2, valuesArray[1]);
            pstmt.setInt(3, Integer.parseInt(valuesArray[2]));

            pstmt.executeUpdate();
            System.out.println("Dane zostały dodane do tabeli " + tableName);
        }
    }

    private static void deleteDataFromTable(Connection conn, String tableName, Scanner scanner) throws SQLException {
        System.out.print("Podaj warunek usunięcia (np. id=1): ");
        String condition = scanner.nextLine();
        String query = "DELETE FROM " + tableName + " WHERE " + condition;

        try (Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Usunięto " + rowsAffected + " wiersz(y) z tabeli " + tableName);
        }
    }

    private static void addSampleDataToStudents(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO studenci (imie, nazwisko, wiek) VALUES ('Jan', 'Kowalski', 20)");
            st.executeUpdate("INSERT INTO studenci (imie, nazwisko, wiek) VALUES ('Anna', 'Nowak', 22)");
            st.executeUpdate("INSERT INTO studenci (imie, nazwisko, wiek) VALUES ('Piotr', 'Wiśniewski', 21)");
            st.executeUpdate("INSERT INTO studenci (imie, nazwisko, wiek) VALUES ('Maria', 'Kowalczyk', 23)");
        }
    }
}
