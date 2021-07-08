package query;
import twitter.TwitterSource;
import ui.ContentPanel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**Singleton pattern class used to facilitate the communication between the UI and the
 * Queries
 * */
public class UIQueryInteractionController {
    private final List<Query> listOfQueries;
    private final TwitterSource source;
    private static UIQueryInteractionController controller;

    private UIQueryInteractionController(TwitterSource s){
        source =s;
        listOfQueries = new ArrayList<>();
    }

    /**Method used to return the UIQueryInteractionController
     * @param s - is  the TwitterSource
     * @return the controller
     * */
    public static UIQueryInteractionController getController(TwitterSource s){
        if(controller==null){
            return new UIQueryInteractionController(s);
        }
        return controller;
    }

    public List<Query> getListOfQueries() {
        return listOfQueries;
    }

    public TwitterSource getSource() {
        return source;
    }

    /**Method used to connect a query to TwitterSource
     * @param q is the query to be added to the twitter source*/
    public void connectQueryWithSource(Query q){
        listOfQueries.add(q);
        source.addObserver(q);
        source.setFilterTerms(getAllTerms());
    }

    /**Method to add the query to the UI of the app
     * @param q is the query
     * @param c is the content panel*/
    public void addQueryToUI(Query q, ContentPanel c){
        c.addQueryTextToUI(q);
    }

    /**Method used to retrieve all distinct query terms
     * @return a set of all query terms
     * */
    private Set<String> getAllTerms(){
        Set<String> terms = new HashSet<>();
        for(Query q: listOfQueries){
            terms.addAll(q.getFilter().terms());
        }
        return terms;
    }

    /**Method to terminate the search for a query and detach it from the source
     * @param q - is the query to be detached
     * */
    public void terminateQuery(Query q){
        q.terminateQuery();
        listOfQueries.remove(q);
        source.deleteObserver(q);
        source.setFilterTerms(getAllTerms());
    }
}
