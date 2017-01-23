package controller;

import data.Wine;
import org.apache.jena.base.Sys;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ManagedBean for Index View
 */

@ManagedBean(name = "data")
@SessionScoped
public class WineController implements Serializable {

    private static Wine wine = new Wine();
    private String searchString;

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return this.searchString;
    }

    public List<Wine> getWines() {
        return wine.searchForSubstring(this.searchString);

    }

    private Wine selectedWine;

    public Wine getSelectedWine() {
        return selectedWine;
    }

    public void setSelectedWine(Wine selectedWine) {
        this.selectedWine = selectedWine;
    }

    /**
     * Helper-methode
     * return list of regions which occur in searchResult for semantic search suggestion
     *
     * @param resultList
     * @return
     */
    private static List<String> getRegionsFromSearchResult(List<Wine> resultList) {
        List<String> regions = new ArrayList<>();
        for (Wine w : resultList) {
            regions.add(w.getWineRegion());
        }
        List<String> region = regions.stream().distinct().collect(Collectors.toList());
        return region;
    }

    /**
     * Helper-methode
     * return list of growers which occur in searchResult for semantic search suggestion
     *
     * @param resultList
     * @return
     */
    private static List<String> getGrowerFromSearchResult(List<Wine> resultList) {
        List<String> growers = new ArrayList<>();
        for (Wine w : resultList) {
            growers.add(w.getWineGrower());
        }
        List<String> grower = growers.stream().distinct().collect(Collectors.toList());
        return grower;
    }

    /**
     * Helper-methode
     * return list of localities which occur in searchResult for semantic search suggestion
     *
     * @param resultList
     * @return
     */
    private static List<String> getLocalityFromSearchResult(List<Wine> resultList) {
        List<String> localities = new ArrayList<>();
        for (Wine w : resultList) {
            localities.add(w.getLocality());
        }
        List<String> locality = localities.stream().distinct().collect(Collectors.toList());
        return locality;
    }


    private static Map<String, List<String>> semanticSearch(String word) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        //sparql
        List<String> subClass = wine.querySubClass(word);
        //if (!subClass.isEmpty()) result.put("subClass", subClass);
        List<String> superClass = wine.querySuperClass(word);
        //if (!subClass.isEmpty()) result.put("superClass", superClass);
        List<String> synonyms = wine.queryEquivalentClass(word);
        //if (!synonyms.isEmpty()) result.put("synonyms", synonyms);

        //db
        List<Wine> dbResults = wine.searchForSubstring(word);
        List<String> locality = getLocalityFromSearchResult(dbResults);
        //if (!locality.isEmpty()) result.put("locality", locality);
        List<String> growers = getGrowerFromSearchResult(dbResults);
        //if (!growers.isEmpty()) result.put("growers", growers);
        List<String> regions = getRegionsFromSearchResult(dbResults);
        //if (!regions.isEmpty()) result.put("regions", regions);

        return result;
    }

    public static void main(String[] argv) throws IOException {
        //List<Wine> resultList = getWines();
        Wine wine = new Wine();
        List<Wine> sW = wine.searchForSubstring("Loimer");
        List<Wine> list = wine.searchForString("Loimer");
        List<Wine> wineFrom = wine.searchWinesFromRegion("Wachau");
        List<String> stringList = wine.queryEquivalentClass("Chardonnay");
        List<String> regions = new ArrayList<>();

        Map<String, List<String>> test = semanticSearch("sdgdf");

        System.out.println(resultList.get(0).getLabel());
        System.out.println(sW.get(0).getLabel());
    }
}
