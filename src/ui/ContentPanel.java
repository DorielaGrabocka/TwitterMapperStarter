package ui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentPanel extends JPanel {
    private JSplitPane topLevelSplitPane;
    private JSplitPane querySplitPane;
    private JPanel newQueryPanel;
    private JPanel existingQueryList;
    private JMapViewer map;

    private ApplicationStarter app;


    public ContentPanel(ApplicationStarter app) {
        this.app = app;
        newQueryPanel = new NewQueryPanel(app);

        setLayout(new BorderLayout());
        setUpMap();
        setUpQuerySplitPane(setUpLayeredPanel());
        setUpTopSplitPane();

        add(topLevelSplitPane, "Center");
        revalidate();
        repaint();
    }

    /**Method used to set up the Map*/
    private void setUpMap(){
        map = new JMapViewer();
        map.setMinimumSize(new Dimension(100, 50));
    }

    /**Method to set up the LayeredPanel*/
    private JPanel setUpLayeredPanel(){
        JPanel layerPanelContainer = new JPanel();
        existingQueryList = new JPanel();
        existingQueryList.setLayout(new BoxLayout(existingQueryList, BoxLayout.Y_AXIS));
        layerPanelContainer.setLayout(new BorderLayout());
        layerPanelContainer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Current Queries"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
        layerPanelContainer.add(existingQueryList, BorderLayout.NORTH);
        return layerPanelContainer;
    }

    /**Method to set up the query split panel
     * @param layerPanelContainer is the bottom neighbour
     * */
    private void setUpQuerySplitPane(JPanel layerPanelContainer){
        querySplitPane = new JSplitPane(0);
        querySplitPane.setDividerLocation(150);
        querySplitPane.setTopComponent(newQueryPanel);
        querySplitPane.setBottomComponent(layerPanelContainer);
    }

    /**Method used to set up the Top split pane*/
    private void setUpTopSplitPane(){
        topLevelSplitPane = new JSplitPane(1);
        topLevelSplitPane.setDividerLocation(150);
        topLevelSplitPane.setLeftComponent(querySplitPane);
        topLevelSplitPane.setRightComponent(map);
    }

    // Add a new query to the set of queries and update the UI to reflect the new query.
    public void addQueryTextToUI(Query query) {
        JPanel newQueryPanel = new JPanel();
        newQueryPanel.setLayout(new GridBagLayout());
        JPanel colorPanel = createColorPanel(query);
        JButton removeButton = createRemoveButton(query, newQueryPanel);
        JCheckBox checkbox = createCheckBox(query);
        GridBagConstraints c = createGridBagConstraints();

        newQueryPanel.add(colorPanel, c);
        newQueryPanel.add(checkbox, c);
        newQueryPanel.add(removeButton);

        existingQueryList.add(newQueryPanel);
        validate();
    }

    /**Helper method to create the GridBag and set its constraints
     * @return a GridBagConstraints object
     * */
    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }

    /**Helpler method to  create the checkboxes and set up the action listener
     * @param query is the query to be associated with the checkbox
     * @return a JCheckbox object
     * */
    private JCheckBox createCheckBox(Query query) {
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.updateVisibility();
            }
        });
        query.setCheckBox(checkbox);
        return checkbox;
    }

    /**Method to create a Colorpanel
     * @param query - is the query that will determine the color of the panel
     * @return a JPanel
     * */
    private JPanel createColorPanel(Query query) {
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(30, 30));
        return colorPanel;
    }

    /**Method to create and set up the RemoveButton
     * @param query - is the query that will be removed
     * @param newQueryPanel - is the jpanel where the query will be removed
     * @return JButton
     * */
    private JButton createRemoveButton(Query query, JPanel newQueryPanel) {
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(30, 20));
        removeButton.addActionListener(e -> {
            app.terminateQuery(query);
            query.terminateQuery();
            existingQueryList.remove(newQueryPanel);
            revalidate();
        });
        return removeButton;
    }



    public JMapViewer getViewer() {
        return map;
    }
}
