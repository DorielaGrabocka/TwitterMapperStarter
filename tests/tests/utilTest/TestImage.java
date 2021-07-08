package tests.utilTest;

import org.junit.jupiter.api.Test;
import util.Util;

import java.awt.image.BufferedImage;


public class TestImage {
    @Test
    public void testImage() {
        BufferedImage norm = Util.imageFromURL("https://www.cs.ubc.ca/~norm");
    }
}
