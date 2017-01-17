package data;


import helper.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Wine {

    private int id;
    private String label;
    private String wineGrape;
    private String wineCategory;
    private String wineGrower;
    private String wineRegion;
    private String locality;

    public Wine() {}

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getWineGrape() {
        return wineGrape;
    }

    public String getWineCategory() {
        return wineCategory;
    }

    public String getWineGrower() {
        return wineGrower;
    }

    public String getWineRegion() {
        return wineRegion;
    }

    public String getLocality() {
        return locality;
    }

    public void setId(int id) { this.id = id; }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWineGrape(String wineGrape) {
        this.wineGrape = wineGrape;
    }

    public void setWineCategory(String wineCategory) {
        this.wineCategory = wineCategory;
    }

    public void setWineGrower(String wineGrower) {
        this.wineGrower = wineGrower;
    }

    public void setWineRegion(String wineRegion) {
        this.wineRegion = wineRegion;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }


    public List<Wine> selectAll() {
        String query = "select Id, Weinname, Weinsorte, Weinkategorie, Winzer, Region, Ort from wines";
        List<Wine> data = new ArrayList<>();

        try {
            Statement stmt = DBHelper.mySQLConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                Wine wine = new Wine();
                wine.setId(rs.getInt("Id"));
                wine.setLabel(rs.getString("Weinname"));
                wine.setWineGrape(rs.getString("Weinsorte"));
                wine.setWineCategory(rs.getString("Weinkategorie"));
                wine.setWineGrower(rs.getString("Winzer"));
                wine.setWineRegion(rs.getString("Region"));
                wine.setLocality(rs.getString("Ort"));

                data.add(wine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


}
