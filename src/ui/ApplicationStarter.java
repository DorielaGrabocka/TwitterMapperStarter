package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import query.Query;
import query.UIQueryInteractionController;
import twitter.TwitterSource;
import twitter.TwitterSourceFactory;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * The Twitter viewer application
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 */
public class ApplicationStarter extends JFrame {
    // The content panel, which contains the entire UI
    private ContentPanel contentPanel;
    // The provider of the tiles for the map, we use the Bing source
    private BingAerialTileSource bing;
    // All of the active queries
    UIQueryInteractionController uiQueryController;


    /**Method used to initialize a twitter resource
     * (Only for the PlaybackTwitterSource)The number passed to the constructor is
     * a speedup value:
     *      1.0 - play back at the recorded speed
     *      2.0 - play back twice as fast
     */
    private void initializeController() {
        // To use the live twitter stream, use the following line
        //TwitterSource twitterSource = TwitterSourceFactory.createLiveTwitterSource();

        // To use the recorded twitter stream, use the following line
        TwitterSource twitterSource = TwitterSourceFactory.createPlaybackTwitterSource(60.0);
        uiQueryController = UIQueryInteractionController.getController(twitterSource);
 }

    private void initializeUIComponents(){
        setSize(300, 300);
        bing = new BingAerialTileSource();
        // Do UI initialization
        contentPanel = new ContentPanel(this);
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**Method used to show map markers, zoom controls and allow scrolling the map*/
    private void setUpMap(){
        getMap().setMapMarkerVisible(true);
        getMap().setZoomContolsVisible(true);
        getMap().setScrollWrapEnabled(true);
        getMap().setTileSource(bing);
    }

    /**This is so that the map eventually loads the tiles once Bing attribution is ready.
     * We use this to trigger zooming the map so that the entire world is visible.
     * */
    private void loadMapTiles(){
        Coordinate coord = new Coordinate(0, 0);
        Timer bingTimer = new Timer();
        TimerTask bingAttributionCheck = new TimerTask() {
            @Override
            public void run() {
                if (!bing.getAttributionText(0, coord, coord).equals("Error loading Bing attribution data")) {
                    getMap().setZoom(2);
                    bingTimer.cancel();
                }
            }
        };
        bingTimer.schedule(bingAttributionCheck, 100, 200);
    }

    /**Set up a motion listener to create a tooltip showing the tweets at the pointer position*/
    private void addMouseMotionListener(){
        getMap().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                ICoordinate pos = getMap().getPosition(p);
                List<MapMarker> listOfMarkers = getMarkersCovering(pos, pixelWidth(p));
                if(!listOfMarkers.isEmpty()){
                    MapMarker marker = listOfMarkers.get(listOfMarkers.size()-1);
                    AdvancedMapMarker advMarker = (AdvancedMapMarker) marker;
                    String text = advMarker.getTweet();
                    String author = advMarker.getAuthor();
                    String imgUrl = advMarker.getAuthorImageURL();
                    getMap().setToolTipText("<html><img src='"+imgUrl+"' height='40' width='40'/><b>"+author+":</b> "+text+"</html>");
                }
            }
        });
    }

    /**
     * A new query has been entered via the User Interface
     * @param   query   The new query object
     */
    public void addQuery(Query query) {
        //here we delegate the task to the uiQueryController object
        uiQueryController.connectQueryWithSource(query);
        uiQueryController.addQueryToUI(query, contentPanel);
    }


    /** A query has been deleted, remove all traces of it
     * @param query is the query to be removed
     * */
    public void terminateQuery(Query query) {
        uiQueryController.terminateQuery(query);
    }


    /**
     * Constructs the {@code Application}.
     */
    public ApplicationStarter() {
        super("Twitter content viewer");
        initializeController();
        initializeUIComponents();
        setUpMap();
        loadMapTiles();
        addMouseMotionListener();
    }

    // How big is a single pixel on the map?  We use this to compute which tweet markers
    // are at the current most position.
    private double pixelWidth(Point p) {
        ICoordinate center = getMap().getPosition(p);
        ICoordinate edge = getMap().getPosition(new Point(p.x + 1, p.y));
        return Util.distanceBetween(center, edge);
    }

    // Get those layers (of tweet markers) that are visible because their corresponding query is enabled
    private Set<Layer> getVisibleLayers() {
        Set<Layer> ans = new HashSet<>();
        for (Query q : uiQueryController.getListOfQueries()) {
            if (q.getVisible()) {
                ans.add(q.getLayer());
            }
        }
        return ans;
    }

    // Get all the markers at the given map position, at the current map zoom setting
    private List<MapMarker> getMarkersCovering(ICoordinate pos, double pixelWidth) {
        List<MapMarker> ans = new ArrayList<>();
        Set<Layer> visibleLayers = getVisibleLayers();
        for (MapMarker m : getMap().getMapMarkerList()) {
            if (visibleLayers.contains(m.getLayer())){
                double distance = Util.distanceBetween(m.getCoordinate(), pos);
                double radiusInPixels = m.getRadius() * pixelWidth;
                if (distance < radiusInPixels) {
                    ans.add(m);
                }
            }
        }
        return ans;
    }

    public JMapViewer getMap() {
        return contentPanel.getViewer();
    }


    // Update which queries are visible after any checkBox has been changed
    public void updateVisibility() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Recomputing visible queries");
                for (Query q : uiQueryController.getListOfQueries()) {
                    q.setVisible(q.getCheckBox().isSelected());
                }
                getMap().repaint();
            }
        });
    }

}
