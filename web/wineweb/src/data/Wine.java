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
     * returns all wines from db
     *
     * @return
     */
    public List<Wine> selectAll() {
        String query = "select Id, Weinname, Weinsort, Weinkategorie, Winzer, Region, Ort from wines";

        return executeQuery(query);
    }

    /**
     * look up all wines from a given grower
     *
     * @param wineGrower
     * @return
     */
    public List<Wine> searchWinesFromGrower(String wineGrower) {
        String query = "select * from wines where Winzer like lower(\"%" + wineGrower + "%\") ";
        return executeQuery(query);
    }

    /**
     * look up all wines from a given region
     *
     * @param region
     * @return
     */
    public List<Wine> searchWinesFromRegion(String region) {
        String query = "select * from wines where Region like lower(\"%" + region + "%\") ";
        return executeQuery(query);
    }

    /**
     * look up all wines form a given locality
     *
     * @param locality
     * @return
     */
    public List<Wine> searchWinesFromLocality(String locality) {
        String query = "select * from wines where Ort like lower(\"%" + locality + "%\") ";
        return executeQuery(query);
    }

    /**
     * look up all wines by a given category
     *
     * @param category
     * @return
     */
    public List<Wine> searchWinesByCategory(String category) {
        String query = "select * from wines where Weinkategorie like lower(\"%" + category + "%\") ";
        return executeQuery(query);
    }

    /**
     *look up all wines by a given sort
     *
     * @param sort
     * @return
     */
    public List<Wine> searchWinesBySort(String sort) {
        String query = "select * from wines where Weinsort like lower(\"%" + sort + "%\") ";
        return executeQuery(query);
    }

    /**
     * look up all wines with a given name
     *
     * @param name
     * @return
     */
    public List<Wine> searchWinesByName(String name) {
        String query = "select * from wines where Weinname like lower(\"%" + name + "%\") ";
        return executeQuery(query);
    }

    /**
     * looks up a given string as substring in whole table wines
     *
     * @param word
     * @return
     */
    public List<Wine> searchForSubstring(String word) {
        String query = "select Id, Weinname, Weinsorte, Weinkategorie, Winzer, Region, Ort " +
                        "from wines " +
                        "WHERE Weinname like lower(\"%" + word + "%\") or " +
                        "Weinsorte like lower(\"%" + word + "%\") or " +
                        "Weinkategorie like lower(\"%" + word + "%\") or " +
                        "Winzer like lower(\"%" + word + "%\") or " +
                        "Region like lower(\"%" + word + "%\") or " +
                        "Ort like lower(\"%" + word + "%\")" +
                        "AND Region ";

        return executeQuery(query);
    }

    /**
     * looks up a given string in whole table wines
     *
     * @param word
     * @return
     */
    public List<Wine> searchForString(String word) {
        String query = "select Id, Weinname, Weinsorte, Weinkategorie, Winzer, Region, Ort " +
                "from wines " +
                "WHERE Weinname like lower(\"" + word + "\") or " +
                "Weinsorte like lower(\"" + word + "\") or " +
                "Weinkategorie like lower(\"" + word + "\") or " +
                "Winzer like lower(\"" + word + "\") or " +
                "Region like lower(\"" + word + "\") or " +
                "Ort like lower(\"" + word + "\")";

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
