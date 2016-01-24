package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.NoteSource;
import com.apixandru.midguitar.model.Notes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public class MidguitarPanel extends JPanel implements NoteSource {

    private static final int DISTANCE_BETWEEN_LINES = 30;
    private static final int DISTANCE_SEMITONE = DISTANCE_BETWEEN_LINES / 2;
    private static final int DISTANCE_BETWEEN_NOTES = 70;

    private static final int POS_E2 = 420;
    private static final int NOTE_E2 = 40;

    private final int fromX;
    private final int toX;

    private static final int START_Y = 35;

    private BufferedImage imgClefG;
    private BufferedImage imgWholeNote;
    private BufferedImage imgModSharp;

    private Integer noteExpected = 58;
    private Integer noteActual = 64;

    MidguitarPanel() {
        setBorder(new LineBorder(Color.BLACK, 2));
        final int width = 300;

        fromX = 20;
        toX = width - fromX;

        setBackground(Color.WHITE);
        setOpaque(true);

        final Dimension minimumSize = new Dimension(width, 460);
        setMinimumSize(minimumSize);
        setPreferredSize(minimumSize);
        loadImages();
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);


        drawNote(g, noteExpected, false);
        drawNote(g, noteActual, true);

        drawLines((Graphics2D) g);
        g.drawImage(imgClefG, 20, 183, null);

    }

    private void drawNote(Graphics g, final Integer note, final boolean actual) {
        if (null == note) {
            return;
        }
        int x = 120;
        if (actual) {
            x += DISTANCE_BETWEEN_NOTES;
        }

        int y = POS_E2 - Notes.getFullNotesInBetween(NOTE_E2, note) * DISTANCE_SEMITONE;
        g.drawImage(imgWholeNote, x, y, null);
        boolean isSharp = Notes.isSharp(note);
        if (isSharp) {
            g.drawImage(imgModSharp, x - 29, y - 14, null);
        }
    }

    /**
     * @param g
     */
    private void drawLines(final Graphics2D g) {
        final Color originalColor = g.getColor();
        final Color outOfRangeColor = new Color(0x11000000, true);
        g.setColor(outOfRangeColor);
        final Stroke originalStroke = g.getStroke();
        g.setStroke(new BasicStroke(4f));
        drawLines(6, 0, g);
        g.setColor(Color.BLACK);
        drawLines(5, 6, g);
        g.setColor(outOfRangeColor);
        drawLines(3, 11, g);
        g.setColor(originalColor);
        g.setStroke(originalStroke);
    }


    private void loadImages() {
        try (final InputStream streamCg = MidguitarFrame.class.getResourceAsStream("/clef_g.png");
             final InputStream streamNw = MidguitarFrame.class.getResourceAsStream("/note_whole.png");
             final InputStream streamMs = MidguitarFrame.class.getResourceAsStream("/mod_sharp.png")) {
            imgClefG = ImageIO.read(streamCg);
            imgWholeNote = ImageIO.read(streamNw);
            imgModSharp = ImageIO.read(streamMs);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param howMany
     * @param startingFrom
     * @param g
     */
    private void drawLines(final int howMany, final int startingFrom, final Graphics g) {
        for (int i = startingFrom, to = startingFrom + howMany; i < to; i++) {
            final int y = START_Y + i * DISTANCE_BETWEEN_LINES;
            g.drawLine(fromX, y, toX, y);
        }
    }

    @Override
    public void noteStart(final int noteNumber) {
        SwingUtilities.invokeLater(() -> {
            noteActual = noteNumber;
            repaint();
        });
    }

}
