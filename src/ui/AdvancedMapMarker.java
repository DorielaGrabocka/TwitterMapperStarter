package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import twitter4j.Status;
import util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**This is the advanced marker that will be shown in map. It contains the image of the
 * author of the status as well as the color specified for a filter.
 * */
public class AdvancedMapMarker extends MapMarkerCircle {

    private static final double DEFAULT_SIZE = 17.0;
    private String tweet;
    private String author;
    private String authorImageURL;
    private BufferedImage authorImage;

    public AdvancedMapMarker(Layer layer, Coordinate coord, Status s, Color color) {
        super(layer, null,coord, DEFAULT_SIZE, STYLE.FIXED, getDefaultStyle());
        setBackColor(color);
        setColor(Color.BLACK);
        author = s.getUser().getName();
        authorImageURL = s.getUser().getMiniProfileImageURL();
        authorImage = Util.imageFromURL(authorImageURL);
        tweet = s.getText();
    }

    public String getTweet() {
        return tweet;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorImageURL() {
        return authorImageURL;
    }

    public BufferedImage getAuthorImage() {
        return authorImage;
    }

    /**Method used to further modify the map  marker with an image as well as the color
     * and other elements required
     * @param graphics - the graphics used for painting
     * @param position is the position where the marker should be shown on the map
     * @param radius is the radius in pixels.
     * */
    @Override
    public void paint(Graphics graphics, Point position, int radius) {
        if (!(graphics instanceof Graphics2D) || this.getBackColor() == null) {
            return;
        }

        if(authorImage==null || authorImageURL.isEmpty() || authorImageURL==null)
            authorImage = Util.defaultImage;

        Graphics2D graphics2D = (Graphics2D) graphics;
        Composite temporaryComposite = graphics2D.getComposite();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        graphics2D.setPaint(this.getBackColor());

        int size = radius * 2;
        graphics.fillOval(position.x - radius, position.y - radius, size, size);
        graphics2D.setComposite(temporaryComposite);
        graphics.drawImage(authorImage, position.x - 10, position.y - 10, 20, 20, null);
    }
}
