package controller;

import data.Wine;
//import org.apache.jena.base.Sys;
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
 * ManagedBean for Index, Searchresults View
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
    private String subclass = "";
    private String superclass = "";
    private String synonym = "";
    private String grape = "";
    private String winecategory = "";

    /**
     * Default Setter-Method for var results
     *
     * @param results
     */
    private void setResults(Map<String, List<String>> results) {
        this.results = results;
    }

    /**
     * Default Getter-Method for var resutls
     *
     * @return
     */
    private Map<String, List<String>> getResults() {
        return this.results;
    }

    /**
     * Default Setter-Method for var searchString
     *
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * Default Getter-Method for var searchString
     *
     * @return
     */
    public String getSearchString() {
        return this.searchString;
    }

    /**
     * Getter-Method for Wines
     * Returns all wines for the view searchresults.xhtml which can be accessed by data.wines
     * Additionally a filter is applied, which is set by clicking on the displayed semantic content
     *
     * @return
     */
    public List<Wine> getWines() {
        return wine.searchForSubstring(this.searchString, region, grower, locality, "", "");

    }

    /**
     * TagCloudModels for all filter options based on semantic content including:
     *   Regions
     *   Subclass
     *   Superclass
     *   Synonyms
     *   Locality
     *   Grower
     *   Grape
     *   Wine Category
     *
     * @return DefaultTagCloudModel
     */

    public TagCloudModel getRegions() {
        return generateModel("regions");
    }

    public TagCloudModel getSubClass() {
        return generateModel("subClass");
    }

    public TagCloudModel getSuperClass() {
        return generateModel("superClass");
    }

    public  TagCloudModel getSynonyms() {
        return generateModel("synonyms");
    }

    public TagCloudModel getLocality() {
        return generateModel("locality");
    }

    public TagCloudModel getGrowers() {
        return generateModel("growers");
    }

    public TagCloudModel getGrapes() {
        return generateModel("wineGrapes");
    }

    public TagCloudModel getWinecategory() {
        return generateModel("wineCategories");
    }

    /**
     * Helper-Method for generating a DefaultTagCloudModel of a given attribute
     * to display semantic data in view searchresults.xhtml
     *
     * @param attribute
     * @return DefaultTagCloudModel
     */
    private TagCloudModel generateModel(String attribute) {
        if(this.results != null) {
            List<String> growers = this.results.get(attribute);
            TagCloudModel model = new DefaultTagCloudModel();

            for (String r : growers) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }
    }

    /**
     * Action-Methods for semantic contents
     * Sets the private fields depending on which semantic option is clicked
     *
     * @param event
     */

    public void onRegionSelect(SelectEvent event) {
        resetFields();
        this.region = getLabelOfItem(event);
    }

    public void onSubclassSelect(SelectEvent event) {
        resetFields();
        this.subclass = getLabelOfItem(event);
    }

    public void onSuperclassSelect(SelectEvent event) {
        resetFields();
        this.superclass = getLabelOfItem(event);
    }

    public void onSynonymSelect(SelectEvent event) {
        resetFields();
        this.synonym = getLabelOfItem(event);
    }

    public void onLocalitySelect(SelectEvent event) {
        resetFields();
        this.locality = getLabelOfItem(event);
    }

    public void onGrowerSelect(SelectEvent event) {
        resetFields();
        this.grower = getLabelOfItem(event);
    }

    public void onGrapeSelect(SelectEvent event) {
        resetFields();
        this.grape = getLabelOfItem(event);
    }

    public void onWinecategorySelect(SelectEvent event) {
        resetFields();
        this.winecategory = getLabelOfItem(event);
    }

    /**
     * Helper-Method for labels of TagCloudItems
     * Returns labels of a given TagCloudItem
     *
     * @param event
     * @return String
     */
    private String getLabelOfItem(SelectEvent event) {
        TagCloudItem item = (TagCloudItem) event.getObject();
        return item.getLabel();
    }

    /**
     * Action-Method for searching
     * Gets fired if the searchbutton is clicked;
     * Sets the val of results with the semantic data of the triplestore
     * Returns view searchresults.xhtml
     *
     * @return String
     */
    public String submitSearch() {
        setResults(this.semanticSearch(this.searchString));
        resetFields();
        return "searchresults";
    }

    /**
     * Helper-Method for resetting semantic search fields if a new search is started
     */
    private void resetFields() {
        this.region = "";
        this.subclass = "";
        this.superclass = "";
        this.synonym = "";
        this.locality = "";
        this.grower = "";
        this.grape = "";
        this.winecategory = "";
    }

    /**
     * The selected wine of the table
     */
    private Wine selectedWine;

    /**
     * Getter- and Setter-Methods for var selectedWine
     *
     */

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

        String changedWord = changeWord(word);

        List<String> subClass = wine.querySubClass(changedWord);
        List<String> filteredSubClass = filter(subClass);
        result.put("subClass", filteredSubClass);
        List<String> superClass = wine.querySuperClass(changedWord);
        List<String> filteredSuperClass = filter(superClass);
        result.put("superClass", filteredSuperClass);
        List<String> synonyms = wine.queryEquivalentClass(changedWord);
        List<String> filteredSynonyms = filter(synonyms);
        result.put("synonyms", filteredSynonyms);
        //db
        List<Wine> dbResults = wine.searchForSubstring(word, "", "", "", "", "");
        List<String> localities = getLocalityFromSearchResult(dbResults);
        result.put("locality", localities);
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

    private List<String> filter (List<String> list) {
        String[] matches = {"wine", "wein", "region", "winegrape", "weinsorte"};
        for (String s : matches)
        {
            if (list.contains(s))
            {
                list.remove(s);
                break;
            }
        }
        return list;
    }

    private String changeWord (String word) {
        word = word.toLowerCase();
        String changedWord;
        if (word.contains(" ")) {
            changedWord = word.replace(" ", "");
        } else {
            changedWord = word;
        }
        return changedWord;
    }

    /**
     * Helper-Method to get a random number in a given range
     *
     * @param maximum
     * @param minimum
     * @return
     */
    private int getRandomNumber(int maximum, int minimum) {

        return rnd.nextInt(maximum - minimum + 1) + minimum;
    }
}
