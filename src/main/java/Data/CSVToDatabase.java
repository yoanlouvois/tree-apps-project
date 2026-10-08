package Data;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.sql.*;

public class CSVToDatabase {

    public static String[] transformCSVString(String[] row){
        StringBuilder build= new StringBuilder();
        for(int i=0;i<row.length;i++){
            build.append(row[i]);
        }
        String element=build.toString();
        return element.split(";");

    }

    public static void loadDatabase(String csvFile, String jdbcURL, String username, String password) {
        // Requête SQL avec la gestion correcte du POINT
        String sql = "INSERT IGNORE INTO arbre (idBase, type_emplacement, domanialite, arrondissement, complement_adresse, numero, lieu, idEmplacement, libelle_france, genre, espece, variete_oucultivar, circonference, hauteur, stade_de_developpement, remarquable, geo_point_2d) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ST_PointFromText(?))";
        int id = 0;
        try (Connection conn = DriverManager.getConnection(jdbcURL, username, password);
             CSVReader reader = new CSVReader(new FileReader(csvFile));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String[] get;
            reader.readNext(); // Ignorer l'en-tête
            while ((get = reader.readNext()) != null) {
                String[] row = transformCSVString(get);
                id = Integer.parseInt(row[0]);
                for (int i = 0; i < row.length; i++) {
                    if (i == 0 || i == 5 || i == 12 || i == 13) {
                        if (row[i].isEmpty()) {
                            pstmt.setNull(i + 1, Types.INTEGER);
                        } else {
                            pstmt.setInt(i + 1, Integer.parseInt(row[i]));

                        }
                    } else if (i == 16) {
                        String geoPoint = "POINT(" + row[i] + ")";
                        pstmt.setString(i + 1, geoPoint);
                    } else {
                        if (row[i].isEmpty()) {
                            pstmt.setNull(i + 1, Types.VARCHAR);
                        } else {
                            pstmt.setString(i + 1, row[i]);
                        }

                    }
                }
                System.out.println("✅ Données insérées avec succès !");
                pstmt.executeUpdate();

            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL à l'id "+id+" : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur générale : " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        String csvFile = "C:\\Users\\samy0\\OneDrive\\Documents\\les-arbres.csv";  // Assure-toi que le fichier a bien l'extension .csv
        String jdbcURL = "jdbc:mysql://localhost:3306/treeappsproject?useSSL=false";
        String username = "root";
        String password = "Mysqlsamytch5!";

        loadDatabase(csvFile, jdbcURL, username, password);
    }



}
