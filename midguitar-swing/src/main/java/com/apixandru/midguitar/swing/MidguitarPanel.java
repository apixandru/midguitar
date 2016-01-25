package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.NoteGenerator;
import com.apixandru.midguitar.model.NoteListener;
import com.apixandru.midguitar.model.Notes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public class MidguitarPanel extends JPanel implements NoteListener {

    private static final int DISTANCE_BETWEEN_LINES = 30;
    private static final int DISTANCE_SEMITONE = DISTANCE_BETWEEN_LINES / 2;
    private static final int DISTANCE_BETWEEN_NOTES = 90;

    private final NoteGenerator noteGenerator = new NoteGenerator(49, 76);

    private static final Color COLOR_GRID = new Color(0x77000000, true);
    private static final BasicStroke STROKE_GRID = new BasicStroke(4f);

    private static final int POS_E2 = 420;
    private static final int NOTE_E2 = 40;

    private final int fromX;
    private final int toX;

    private static final int START_Y = 35;

    private final BiConsumer<Integer, Integer> stats;

    private int correct;
    private int wrong;

    private BufferedImage imgClefG;
    private BufferedImage imgWholeNote;
    private BufferedImage imgModSharp;

    private Integer noteExpected = noteGenerator.nextNote();
    private Integer noteActual;

    MidguitarPanel(final BiConsumer<Integer, Integer> stats) {
        setBorder(new LineBorder(Color.BLACK, 2));
        this.stats = stats;
        final int width = 330;

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

        if (Notes.isSharp(note)) {
            g.drawImage(imgModSharp, x - 29, y - 14, null);
        }
        addNoteSupport(g, note, x);
    }

    /**
     * @param g
     * @param note
     * @param x
     */
    private void addNoteSupport(final Graphics g, final int note, final int x) {
        final int relationToMainGrid = getRelationToMainGrid(note);
        if (0 < relationToMainGrid) {
            drawLines(relationToMainGrid, 6 - relationToMainGrid, x, x + imgWholeNote.getWidth(), g);
        } else if (0 > relationToMainGrid) {
            drawLines(-relationToMainGrid, 11, x, x + imgWholeNote.getWidth(), g);
        }
    }

    /**
     * @param note the note
     * @return the relation to the main grid
     */
    private int getRelationToMainGrid(final int note) {
        if (note > 67) {
            return Notes.getFullNotesInBetween(66, note) / 2;
        }
        if (note < 51) {
            return Notes.getFullNotesInBetween(52, note) / 2;
        }
        return 0;
    }

    /**
     * @param g
     */
    private void drawLines(final Graphics2D g) {
        drawLines(5, 6, fromX, toX, g);
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
     * @param fromX
     * @param toX
     * @param g
     */
    private static void drawLines(final int howMany, final int startingFrom, final int fromX, final int toX, final Graphics g) {
        final Color originalColor = g.getColor();
        final Stroke originalStroke = ((Graphics2D) g).getStroke();
        ((Graphics2D) g).setStroke(STROKE_GRID);
        g.setColor(COLOR_GRID);
        for (int i = startingFrom, to = startingFrom + howMany; i < to; i++) {
            final int y = START_Y + i * DISTANCE_BETWEEN_LINES;
            g.drawLine(fromX, y, toX, y);
        }
        ((Graphics2D) g).setStroke(originalStroke);
        g.setColor(originalColor);
    }

    @Override
    public void noteStart(final int noteNumber) {
        SwingUtilities.invokeLater(() -> {
            if (noteNumber == noteExpected) {
                noteActual = null;
                noteExpected = noteGenerator.nextNote();
                correct++;
            } else {
                noteActual = noteNumber;
                wrong++;
            }
            stats.accept(correct, wrong);
            repaint();
        });
    }

}
