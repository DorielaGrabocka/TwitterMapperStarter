package query;

import filters.Filter;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import twitter4j.Status;
import ui.AdvancedMapMarker;
import util.Util;

/**
 * A query over the twitter stream.
 * Implements Observer so that each query can be registered as an
 * observer of a TwitterSource object.
 */
public class Query implements Observer {
    // The map on which to display markers when the query matches
    private final JMapViewer map;

    // Each query has its own "layer" so they can be turned on and off all at once
    private Layer layer;

    // The color of the outside area of the marker
    private final Color color;

    // The string representing the filter for this query
    private final String queryString;

    // The filter parsed from the queryString
    private final Filter filter;

    // The checkBox in the UI corresponding to this query (so we can turn it on and off and delete it)
    private JCheckBox checkBox;

    //The list that will carry all the markers for a query
    private List<MapMarkerCircle> listOfMarkers;

    public Color getColor() {
        return color;
    }
    public String getQueryString() {
        return queryString;
    }
    public Filter getFilter() {
        return filter;
    }
    public Layer getLayer() {
        return layer;
    }
    public JCheckBox getCheckBox() {
        return checkBox;
    }
    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }
    public void setVisible(boolean visible) {
        layer.setVisible(visible);
    }
    public boolean getVisible() { return layer.isVisible(); }

    public Query(String queryString, Color color, JMapViewer map) {
        this.queryString = queryString;
        this.filter = Filter.parse(queryString);
        this.color = color;
        this.layer = new Layer(queryString);
        this.map = map;
        listOfMarkers = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Query: " + queryString;
    }

    /**
     * This query is no longer interesting, so terminate it and remove all traces of its existence.
     */
    public void terminateQuery() {
        layer.setVisible(false);
        for(MapMarkerCircle marker: listOfMarkers){
            map.removeMapMarker(marker);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        Status twitterStatus = (Status) o;
        if(filter.matches(twitterStatus)){
            MapMarkerCircle marker = displayTweetMarkerInMap(twitterStatus);
            listOfMarkers.add(marker);
        }
    }

    /**Utility method used to add a marker to a map as well as to the list of markers
     * @param s - is the status that will be associated with a marker and added to the map
     * @return the map marker
     * */
    private MapMarkerCircle displayTweetMarkerInMap(Status s){
        MapMarkerCircle marker = new AdvancedMapMarker(layer, Util.statusCoordinate(s), s, color);
        map.addMapMarker(marker);
        return marker;
    }
}

