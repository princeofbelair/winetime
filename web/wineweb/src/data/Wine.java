package data;


import helper.DBHelper;
import javafx.collections.transformation.SortedList;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    /**
     * looks up a given string as substring in whole table wines
     *
     * @param word
     * @return
     */
    public List<Wine> searchForSubstring(String word, String region, String grower, String locality, String wineCategory, String wineGrape) {
        String query = "select Id, Weinname, Weinsorte, Weinkategorie, Winzer, Region, Ort " +
                        "from wines " +
                        "WHERE (Weinname like lower(\"%" + word + "%\") or " +
                        "Weinsorte like lower(\"%" + word + "%\") or " +
                        "Weinkategorie like lower(\"%" + word + "%\") or " +
                        "Winzer like lower(\"%" + word + "%\") or " +
                        "Region like lower(\"%" + word + "%\") or " +
                        "Ort like lower(\"%" + word + "%\"))";

        if (!region.isEmpty()) {
            query += " and Region like lower(\"%" + region + "%\")";
        }
        if (!grower.isEmpty()) {
            query += " and Winzer like lower(\"%" + grower + "%\")";
        }
        if (!locality.isEmpty()) {
            query += " and Ort like lower(\"%" + locality + "%\")";
        }
        if (!wineCategory.isEmpty()) {
            query += " and Weinkategorie like lower(\"%" + wineCategory + "%\")";
        }
        if (!wineGrape.isEmpty()) {
            query += " and Weinsorte like lower(\"%" + wineGrape + "%\")";
        }
        return executeQuery(query);
    }

    /**
     * queries subclasses of given class
     *
     * @param queriedClass
     * @return
     */
    public List<String> querySubClass (String queriedClass) {
        String prefixRdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
        String prefixRdf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
        String prefixOwl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
        String prefixWine = "PREFIX wine: <http://jku.at/winetime#>";

        String query = prefixRdfs + prefixRdf + prefixOwl + prefixWine +
                "SELECT ?class" +
                "\nWHERE { \n" +
                "?class rdfs:subClassOf wine:" + queriedClass +
                "}";
        return executeSparqlQuery(query);
    }

    /**
     * queries superclass of a given class
     *
     * @param queriedClass
     * @return
     */
    public List<String> querySuperClass (String queriedClass) {
        String prefixRdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
        String prefixRdf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
        String prefixOwl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
        String prefixWine = "PREFIX wine: <http://jku.at/winetime#>";

        String query = prefixRdfs + prefixRdf + prefixOwl + prefixWine +
                "SELECT ?class" +
                "\nWHERE { \n" +
                "wine:" + queriedClass + " rdfs:subClassOf ?class." +
                "\n}";

        return executeSparqlQuery(query);
    }

    /**
     * queries equivalentclasses of a given class
     *
     * @param queriedClass
     * @return
     */
    public List<String> queryEquivalentClass (String queriedClass) {
        String prefixRdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
        String prefixRdf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
        String prefixOwl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
        String prefixWine = "PREFIX wine: <http://jku.at/winetime#>";

        String query = prefixRdfs + prefixRdf + prefixOwl + prefixWine +
                "SELECT ?class" +
                "\nWHERE { \n" +
                "?class (owl:equivalentClass|^owl:equivalentClass) wine:" + queriedClass + "." +
                "\n}";

        return executeSparqlQuery(query);
    }

    /**
     * executes a sparql-query and returns a string-list with the results
     *
     * @param query
     * @return
     */
    public List<String> executeSparqlQuery(String query) {
        List<String> data = new ArrayList<String>();
        QueryExecution q = QueryExecutionFactory.sparqlService("http://localhost:3030/wineTime", query);
        org.apache.jena.query.ResultSet results = q.execSelect();

        while (results.hasNext()) {
            String solution = results.next().get("class").toString().split("#")[1];
            data.add(solution);
        }
        return data;
    }

    /**
     * executes a given query-String and returns a list of results
     *
     * @param query
     * @return
     */
    public List<Wine> executeQuery(String query) {
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
