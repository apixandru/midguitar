package com.apixandru.midguitar.swing;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class MidguitarFrame {

    public static void main(String[] args) {
        final JFrame midguitar = new JFrame("Midguitar");
        midguitar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        midguitar.getContentPane().setLayout(new GridBagLayout());
        midguitar.add(new MidguitarPanel());
        midguitar.pack();
        midguitar.setLocationRelativeTo(null);
        midguitar.setVisible(true);
    }

}
