package com.apixandru.midguitar.swing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 25, 2016
 */
final class ImageLoader {

    private ImageLoader() {
    }

    static BufferedImage loadFromClasspath(final String path) {
        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + path, e);
        }
    }

}
