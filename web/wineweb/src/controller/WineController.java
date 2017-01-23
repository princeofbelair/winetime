package controller;

import data.Wine;
import org.apache.jena.base.Sys;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
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
    private Map<String, List<String>> results;
    private Random rnd = new Random();
    private String locality = "";
    private String region = "";
    private String grower = "";

    private void setResults(Map<String, List<String>> results) {
        this.results = results;
    }

    private Map<String, List<String>> getResults() {
        return this.results;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return this.searchString;
    }

    public List<Wine> getWines() {
        return wine.searchForSubstring(this.searchString, region, grower, locality);

    }

    public TagCloudModel getRegions() {
        if(this.results != null) {
            List<String> regions = this.results.get("regions");
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : regions) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }
            return model;
        } else {
            return new DefaultTagCloudModel();
        }
    }

    public TagCloudModel getSubClass() {
        if(this.results != null) {
            List<String> subclass = this.results.get("subclass");
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : subclass) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }

    }

    public TagCloudModel getSuperClass() {
        if(this.results != null) {
            List<String> superclass = this.results.get("superclass");
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : superclass) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }
    }

    public  TagCloudModel getSynonyms() {
        if(this.results != null) {
            List<String> synonyms = this.results.get("synonyms");
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : synonyms) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }

    }

    public TagCloudModel getLocality() {
        if(this.results != null) {
            List<String> locality = this.results.get("locality");
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : locality) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }

    }

    public TagCloudModel getGrowers() {
        if(this.results != null) {
            List<String> growers = this.results.get("growers");
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : growers) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }

    }

    public void onRegionSelect(SelectEvent event) {
        TagCloudItem item = (TagCloudItem) event.getObject();
        this.region = item.getLabel();
    }

    public String submitSearch() {
        setResults(this.semanticSearch(this.searchString));
        return "searchresults";
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

    private List<String> getWineGrapeFromSearchResult(List<Wine> resultList) {
        List<String> wineGrape = new ArrayList<>();
        for (Wine w : resultList) {
            wineGrape.add(w.getLocality());
        }
        List<String> wineGrapes = wineGrape.stream().distinct().collect(Collectors.toList());
        return wineGrapes;
    }

    private List<String> getWineCategoryFromSearchResult(List<Wine> resultList) {
        List<String> wineCategory = new ArrayList<>();
        for (Wine w : resultList) {
            wineCategory.add(w.getLocality());
        }
        List<String> wineCategories = wineCategory.stream().distinct().collect(Collectors.toList());
        return wineCategories;
    }


    private Map<String, List<String>> semanticSearch(String word) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        //sparql
        List<String> subClass = wine.querySubClass(word);
        if (!subClass.isEmpty()) result.put("subClass", subClass);
        List<String> superClass = wine.querySuperClass(word);
        if (!subClass.isEmpty()) result.put("superClass", superClass);
        List<String> synonyms = wine.queryEquivalentClass(word);
        if (!synonyms.isEmpty()) result.put("synonyms", synonyms);

        //db
        List<Wine> dbResults = wine.searchForSubstring(word, "", "", "");
        List<String> locality = getLocalityFromSearchResult(dbResults);
        result.put("locality", locality);
        List<String> growers = getGrowerFromSearchResult(dbResults);
        result.put("growers", growers);
        List<String> regions = getRegionsFromSearchResult(dbResults);
        result.put("regions", regions);
        List<String> wineGrapes = getWineGrapeFromSearchResult(dbResults);
        result.put("wineGrapes", wineGrapes);
        List<String> wineCategories = getWineCategoryFromSearchResult(dbResults);
        result.put("wineCategories", wineCategories);

        return result;
    }

    private int getRandomNumber(int maximum, int minimum) {

        return rnd.nextInt(maximum - minimum + 1) + minimum;
    }
}
