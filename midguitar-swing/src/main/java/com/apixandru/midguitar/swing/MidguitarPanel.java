package com.apixandru.midguitar.swing;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public class MidguitarPanel extends JPanel {

    private static final Font FONT_NOTE = new Font(null, Font.PLAIN, 136);
    private static final String WHOLE_NOTE_UNICODE = "\uD834\uDD5D";

    private static final int DISTANCE_BETWEEN_NOTES = 30;

    private final int fromX;
    private final int toX;

    private static final int START_Y = 20;


    MidguitarPanel() {
        setBorder(new LineBorder(Color.BLACK, 2));
        final int width = 300;

        fromX = 20;
        toX = width - fromX;

        final Dimension minimumSize = new Dimension(width, 430);
        setMinimumSize(minimumSize);
        setPreferredSize(minimumSize);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Color originalColor = g.getColor();
        final Color outOfRangeColor = Color.red;
        g.setColor(outOfRangeColor);
        Graphics2D g2 = (Graphics2D) g;
        final Stroke originalStroke = ((Graphics2D) g).getStroke();
        g2.setStroke(new BasicStroke(2));
        drawLines(6, 0, g);
        g.setColor(originalColor);
        drawLines(5, 6, g);
        g.setColor(outOfRangeColor);
        drawLines(3, 11, g);
        g.setColor(originalColor);
        g2.setStroke(originalStroke);

    }

    /**
     * @param howMany
     * @param startingFrom
     * @param g
     */
    private void drawLines(final int howMany, final int startingFrom, final Graphics g) {
        for (int i = startingFrom, to = startingFrom + howMany; i < to; i++) {
            final int y = START_Y + i * DISTANCE_BETWEEN_NOTES;
            g.drawLine(fromX, y, toX, y);
        }
    }
}
