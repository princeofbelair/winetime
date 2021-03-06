package controller;

import data.Wine;
import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

//import org.apache.jena.base.Sys;

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
    private static int SUGGESTIONS_SIZE = 10;
    private String showSynonyms = "";
    private String showSuperClass = "";
    private String showSubClass = "";
    private String message = "";
    private String updateMessage = "";
    private boolean hasResults = true;

    public String getShowSynonyms() { return this.showSynonyms; }

    public void setShowSynonyms(String showSynonyms) { this.showSynonyms = showSynonyms; }

    public String getShowSuperClass() { return this.showSuperClass; }

    public void setShowSuperClass(String showSuperClass) { this.showSuperClass = showSuperClass; }

    public String getShowSubClass() { return this.showSubClass; }

    public void setShowSubClass(String showSubClass) { this.showSubClass = showSubClass; }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }
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
        return wine.searchForSubstring(this.searchString, region, grower, locality, winecategory, grape);

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

    public TagCloudModel getSynonyms() {
        return generateModel("synonyms");
    }

    public TagCloudModel getLocality() {
        return generateModel("locality");
    }

    public TagCloudModel getGrowers() {
        return generateModel("growers");
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
        this.semanticSearch(this.searchString);
        if(this.results != null) {
            List<String> growers = this.results.get(attribute);
            TagCloudModel model = new DefaultTagCloudModel();
            int i = 0;

            for (String r : growers) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
                i++;
                if(i == SUGGESTIONS_SIZE) { break; }
            }

            return model;
        } else {
            return new DefaultTagCloudModel();
        }
    }

    /**
     * Helper-Method for generating a DefaultTagCloudModel of a semantic attributes
     * to display semantic data in view searchresults.xhtml
     *
     * @return DefaultTagCloudModel
     */
    private TagCloudModel generateSemanticModel() {
        if(this.results != null) {
            List<String> subClass = this.results.get("subClass");
            List<String> superClass = this.results.get("superClass");
            List<String> synonyms = this.results.get("synonyms");

            TagCloudModel model = new DefaultTagCloudModel();
            int i = 0;

            for (String r : subClass) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
                i++;
                if(i == SUGGESTIONS_SIZE/3) { break; }
            }
            for (String r : superClass) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
                i++;
                if(i == SUGGESTIONS_SIZE/3) { break; }
            }
            for (String r : synonyms) {
                model.addTag(new DefaultTagCloudItem(r, getRandomNumber(4, 1)));
                i++;
                if(i == SUGGESTIONS_SIZE/3) { break; }
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
        addMessageToView();
    }

    public void onSubclassSelect(SelectEvent event) {
        resetFields();
        this.searchString = getLabelOfItem(event);
        addMessageToView();
        this.submitSearch();
    }

    public void onSuperclassSelect(SelectEvent event) {
        resetFields();
        this.searchString = getLabelOfItem(event);
        addMessageToView();
        this.submitSearch();
    }

    public void onSynonymSelect(SelectEvent event) {
        resetFields();
        this.searchString = getLabelOfItem(event);
        addMessageToView();
        this.submitSearch();
    }

    public void onLocalitySelect(SelectEvent event) {
        resetFields();
        this.locality = getLabelOfItem(event);
        addMessageToView();
    }

    public void onGrowerSelect(SelectEvent event) {
        resetFields();
        this.grower = getLabelOfItem(event);
        addMessageToView();
    }

    public void onWinecategorySelect(SelectEvent event) {
        resetFields();
        this.winecategory = getLabelOfItem(event);
        addMessageToView();
        if(!this.hasResults)
        { this.searchString = this.winecategory; this.submitSearch(); }
        else { setResults(this.semanticSearch(this.searchString)); }

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

        if(RequestContext.getCurrentInstance().isAjaxRequest()) {
            setResults(semanticSearch(this.searchString));
            RequestContext.getCurrentInstance().execute("location.href='searchresults.xhtml';");

        } else {
            setResults(semanticSearch(this.searchString));
        }
        resetFields();
        proofIfEmpty();
        return "searchresults";
    }

    /**
     * Helper-Method that adds a message if a onSelect method is called
     */
    private void addMessageToView() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Info:", "Ihr Suche wurde angepasst."));
    }

    /**
     * Proofs if one of the lists bellow is empty, so that the view does not display these
     */
    private void proofIfEmpty() {
        if(results.get("synonyms").isEmpty()) {
            this.showSynonyms = "none";
        } else {
            this.showSynonyms = "block";
        }
        if(results.get("subClass").isEmpty()) {
            this.showSubClass = "none";
        } else {
            this.showSubClass = "block";
        }
        if(results.get("superClass").isEmpty()) {
            this.showSuperClass = "none";
        } else {
            this.showSuperClass = "block";
        }

        if(results.get("synonyms").isEmpty() && results.get("subClass").isEmpty() && results.get("superClass").isEmpty()) {
            this.message = "Es konnten keine Vorschläge zu diesem Begriff gefunden werden!";
        } else {
            this.message = "";
        }
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
            wineCategory.add(w.getWineCategory());
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
        if (containsCaseInsensitive(word, wineCategories))wineCategories.remove(word);
        if(wineCategories.size() == 1)wineCategories.remove(0);
        result.put("wineCategories", wineCategories);

        if(subClass.isEmpty() && superClass.isEmpty() && synonyms.isEmpty() && dbResults.isEmpty()){
            wineCategories = filter(wine.querySubClass("wein"));
            result.put("wineCategories", wineCategories);
            this.hasResults = false;
        } else {
            this.hasResults = true;
        }

        return result;
    }

    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                l.remove(string);
                return true;
            }
        }
        return false;
    }

    private List<String> filter (List<String> list) {
        String[] matches = {"wine", "wein", "region", "winegrape", "weinsorte", "dessertwine", "redwine", "whitewine", "sparklingwine", "rosewine", "winery"};
        for (String s : matches)
        {
            if (list.contains(s))
            {
                list.remove(s);
                break;
            }
        }
        List<String> changedList = new ArrayList<>();
        for (String s : list) {
            String changedString;
            if (s.contains("_")) {
                changedString = s.replace("_", " ");
            } else {
                changedString = s;
            }
            changedList.add(WordUtils.capitalizeFully(changedString));
        }

        return changedList;
    }

    private String changeWord (String word) {
        word = word.toLowerCase();
        String changedWord;
        if (word.contains(" ")) {
            changedWord = word.replace(" ", "_");
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
