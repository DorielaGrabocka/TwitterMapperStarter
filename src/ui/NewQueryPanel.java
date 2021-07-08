package ui;

import query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * A UI panel for entering new queries.
 */
public class NewQueryPanel extends JPanel {
    private final JTextField newQuery;
    private final JLabel queryLabel ;
    private final JPanel colorSetter;
    private final ApplicationStarter app;
    private Random random;

    /**Utility method to initilaize and set up GUI components*/
    private void initilaizeGUI(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        queryLabel.setLabelFor(newQuery);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.gridy = 0;
        c.gridx = 0;
        add(queryLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        newQuery.setMaximumSize(new Dimension(200, 20));
        c.gridx = 1;
        add(newQuery, c);

        add(Box.createRigidArea(new Dimension(5, 5)));

        JLabel colorLabel = new JLabel("Select Color: ");
        colorSetter.setBackground(getRandomColor());

        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.gridy = 1;
        c.gridx = 0;
        add(colorLabel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        colorSetter.setMaximumSize(new Dimension(200, 20));
        add(colorSetter, c);

        add(Box.createRigidArea(new Dimension(5, 5)));

        JButton addQueryButton = new JButton("Add New Search");
        c.gridx = GridBagConstraints.RELATIVE;       //aligned with button 2
        c.gridwidth = 2;   //2 columns wide
        c.gridy = GridBagConstraints.RELATIVE;       //third row
        add(addQueryButton, c);
        // This makes the "Enter" key submit the query.
        app.getRootPane().setDefaultButton(addQueryButton);
        addQueryButtonLister(addQueryButton);
    }

    /**Method used to register the action listener for the add query button
     * @param addQueryButton is the button whose action listener will be registered
     * */
    private void addQueryButtonLister(JButton addQueryButton){
        addQueryButton.addActionListener(e -> {
            if (!newQuery.getText().equals("")) {
                addQuery(newQuery.getText().toLowerCase());
                newQuery.setText("");
            }
        });
    }

    /**Method used to register a mouse event for the color setter*/
    private void colorChooserActionListener(){
        colorSetter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color newColor = JColorChooser.showDialog(
                            app,
                            "Choose Background Color",
                            colorSetter.getBackground());
                    // newColor is "null" if user clicks "Cancel" button on JColorChooser popup.
                    if (newColor != null) {
                        colorSetter.setBackground(newColor);
                    }
                }
            }
        });
    }

    public NewQueryPanel(ApplicationStarter app) {
        newQuery = new JTextField(10);
        queryLabel = new JLabel("Enter Search: ");
        random = new Random();
        this.app = app;
        this.colorSetter = new JPanel();
        initilaizeGUI();
        addTitledBorder();
        colorChooserActionListener();
    }

    /**Method used to add a title bar */
    private void addTitledBorder(){
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("New Search"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
    }

    /**Method to add a query
     * @param newQuery is the text of the query to  be created and added
     * */
    private void addQuery(String newQuery) {
        Query query = new Query(newQuery, colorSetter.getBackground(), app.getMap());
        app.addQuery(query);
        colorSetter.setBackground(getRandomColor());
    }

    /**Method to generate a random color for the next query*/
    public Color getRandomColor() {
        // Pleasant colors: https://stackoverflow.com/questions/4246351/creating-random-colour-in-java#4246418
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }
}
