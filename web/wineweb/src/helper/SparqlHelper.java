package helper;


import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.web.HttpOp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;

import java.util.ArrayList;
import java.util.List;

public class SparqlHelper {

    //prefixes for queries
    private static String prefixRdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
    private static String prefixRdf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    private static String prefixOwl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
    private static String prefixWine = "PREFIX wine: <http://jku.at/winetime#>";


    //kA ob ma des brauchen
    public static void uploadRDF(File rdf, String serviceURI)
            throws IOException {

        // parse the file
        Model m = ModelFactory.createDefaultModel();
        try (FileInputStream in = new FileInputStream(rdf)) {
            m.read(in, null, "RDF/XML");
        }

        // upload the resulting model
        DatasetAccessor accessor = DatasetAccessorFactory
                .createHTTP(serviceURI);
        accessor.putModel(m);
    }
    /**
     * Query for querying subClasses from sparql-endpoint
     *
     * @param queriedClass
     * @return
     */
    private static ResultSet querySubClass (String queriedClass) {
        String query = prefixRdfs + prefixRdf + prefixOwl + prefixWine +
                "SELECT ?class" +
                "\nWHERE { \n" +
                "?class rdfs:subClassOf wine:" + queriedClass +
                "}";
        QueryExecution q = QueryExecutionFactory.sparqlService("http://localhost:3030/wineTime", query);
        ResultSet results = q.execSelect();
        return results;
    }

    /**
     * Query for querying superClasses from sparql-endpoint
     *
     * @param queriedClass
     * @return
     */
    private static ResultSet querySuperClass (String queriedClass) {
        String query = prefixRdfs + prefixRdf + prefixOwl + prefixWine +
                "SELECT ?class" +
                "\nWHERE { \n" +
                "wine:" + queriedClass + " rdfs:subClassOf ?class." +
                "\n}";

        QueryExecution q = QueryExecutionFactory.sparqlService("http://localhost:3030/wineTime", query);
        ResultSet results = q.execSelect();
        return results;
    }

    /**
     * Query for querying equivalent classes from sparql-endpoint
     *
     * @param queriedClass
     * @return
     */
    private static ResultSet queryEquivalentClass (String queriedClass) {
        String query = prefixRdfs + prefixRdf + prefixOwl + prefixWine +
                "SELECT ?class" +
                "\nWHERE { \n" +
                "?class (owl:equivalentClass|^owl:equivalentClass) wine:" + queriedClass + "." +
                "\n}";

        QueryExecution q = QueryExecutionFactory.sparqlService("http://localhost:3030/wineTime", query);
        ResultSet results = q.execSelect();
        return results;
    }

    public static void main(String[] argv) throws IOException {
        //uploadRDF(new File("C:/Users/Romana/Desktop/WineTime.rdf"), "http://localhost:3030/");


        List<String> resultList = new ArrayList<String>();

        ResultSet results = querySuperClass("Gewürztraminer");
        if(!results.hasNext()) System.out.println("Ergebnisliste ist leer");
        System.out.println("Ergebnisliste:\n");
        while (results.hasNext()) {
            //parameter in get() depends on what query is executed (subClass, superClass, equivalentClass)
            String solution = results.next().get("class").toString().split("#")[1];
            //add result-values to list for further usage
            resultList.add(solution);
            // print the output to stdout
            System.out.println(solution);

        }
        System.out.println("\n\nListe:");
        for(String res : resultList) {
            System.out.println(res);
        }
        System.out.print("\nErgebnis-Ende :)");
    }
}
