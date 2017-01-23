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
import java.util.List;

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

    @PostConstruct
    public String submitSearch() {
        System.out.println("test");
        return "searchresults.xhtml";
    }

    public static void main(String[] argv) throws IOException {
        //List<Wine> resultList = getWines();
        Wine wine = new Wine();
        List<Wine> sW = wine.searchForSubstring("Riesling");
        //List<Wine> list = wine.searchForString("Loimer");
        //List<Wine> wineFrom = wine.searchWinesFromRegion("Wachau");
        //List<String> stringList = wine.queryEquivalentClass("Chardonnay");
        //System.out.println(resultList.get(0).getLabel());
        System.out.println(sW.get(0).getLabel());
    }
}
