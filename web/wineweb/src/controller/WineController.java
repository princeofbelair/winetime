package controller;

import data.Wine;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * ManagedBean for Index View
 */

@ManagedBean(name = "data")
@ViewScoped
public class WineController implements Serializable {

    private Wine wine = new Wine();

    public List<Wine> getWines() {
        return wine.selectAll();
    }


}
