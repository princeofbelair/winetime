package controller;

import data.Wine;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * ManagedBean for Index View
 */

@ManagedBean(name = "data")
@ViewScoped
public class WineController implements Serializable {

    private static Wine wine = new Wine();

    public static List<Wine> getWines() {
        return wine.selectAll();
    }

    private Wine selectedWine;

    public Wine getSelectedWine() {
        return selectedWine;
    }

    public void setSelectedWine(Wine selectedWine) {
        this.selectedWine = selectedWine;
    }


    public static void main(String[] argv) throws IOException {
        List<Wine> resultList = getWines();
        Wine wine = new Wine();
        List<Wine> sW = wine.searchForSubstring("Loimer");
        List<Wine> list = wine.searchForString("Loimer");
        List<Wine> wineFrom = wine.searchWinesFromRegion("Wachau");
        List<String> stringList = wine.queryEquivalentClass("Chardonnay");
        System.out.println(resultList.get(0).getLabel());
        System.out.println(sW.get(0).getLabel());
    }
}
