package jku.winetime;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

public class Main {

    public static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
        createRDFDummy();
    }

    public static void createRDFDummy() {
        String wineUri = "http://jku.at/Wine";
        String whitewineUri = "http://jku.at/Whitewine";
        String redwineUri = "http://jku.at/Redwine";

        Model model = ModelFactory.createDefaultModel();
        Resource wine = model.createResource(wineUri);
        Resource whitewine = model.createResource(whitewineUri);
        Resource redwine = model.createResource(redwineUri);


        wine.addProperty(OWL.hasValue, "Wine");
        wine.addProperty(OWL.disjointWith, "Beer");

        whitewine.addProperty(OWL.hasValue, "Whitewine");
        wine.addProperty(OWL.equivalentClass, whitewine);

        redwine.addProperty(OWL.hasValue, "Redwine");
        wine.addProperty(OWL.equivalentClass, redwine);

        model.write(System.out);
    }
}
