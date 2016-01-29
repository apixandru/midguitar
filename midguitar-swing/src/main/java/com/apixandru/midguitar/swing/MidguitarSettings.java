package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidguitarModel;
import com.apixandru.midguitar.model.MidiDevices;
import com.apixandru.midguitar.model.Notes;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 29, 2016
 */
public class MidguitarSettings extends JPanel {

    private final DefaultComboBoxModel<MidiDevice> modelInput = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<MidiDevice> modelOuput = new DefaultComboBoxModel<>();

    private final MidguitarModel model = new MidguitarModel();

    MidguitarSettings(final MidiDevices deviceProvider) {
        final Dimension minimumSize = new Dimension(500, 460);
        setMinimumSize(minimumSize);
        setPreferredSize(minimumSize);
        setBorder(new LineBorder(Color.BLACK, 2));
        setLayout(new BorderLayout());
        final JPanel jPanel = new JPanel();
        add(jPanel, BorderLayout.NORTH);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(createPanel("Enable Input", modelInput, deviceProvider::getInputDevices));
        jPanel.add(createPanel("Enable Output", modelOuput, deviceProvider::getSynthesizers));
        jPanel.add(createConfig());
        jPanel.add(createStart());
    }

    private Component createStart() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.add(Box.createHorizontalGlue());
        final JButton start = new JButton("Start");
        start.addActionListener(e -> {
            try {
                model.start();
            } catch (MidiUnavailableException e1) {
                error("Cannot start device");
            }
        });
        jPanel.add(start);
        return jPanel;
    }

    private JPanel createConfig() {
        final String[] notes = Notes.getNoteNames().toArray(new String[128]);
        final DefaultComboBoxModel<String> fromModel = new DefaultComboBoxModel<>(notes);
        fromModel.setSelectedItem(notes[40]);
        final DefaultComboBoxModel<String> toModel = new DefaultComboBoxModel<>(notes);
        toModel.setSelectedItem(notes[86]);

        final JPanel noteControl = new JPanel();
        noteControl.setLayout(new BoxLayout(noteControl, BoxLayout.X_AXIS));
        final JCheckBox chk = new JCheckBox();
        chk.setText("Include sharp notes");
        noteControl.add(chk);
        noteControl.add(new JLabel("   From   "));
        noteControl.add(new JComboBox<>(fromModel));
        noteControl.add(new JLabel("   To   "));
        noteControl.add(new JComboBox<>(toModel));
        noteControl.add(Box.createHorizontalStrut(88));
        noteControl.setBorder(new EmptyBorder(5, 5, 5, 5));
        return noteControl;
    }

    /**
     * @param enableText
     * @param model
     * @param deviceMethod
     * @return
     */
    private static JPanel createPanel(
            final String enableText,
            final DefaultComboBoxModel<MidiDevice> model,
            final Callable<List<? extends MidiDevice>> deviceMethod) {

        final JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

        JCheckBox cbUseOutputDevice = new JCheckBox();
        cbUseOutputDevice.setText(enableText);
        jPanel.add(cbUseOutputDevice);
        final JComboBox<MidiDevice> cmbOutput = new JComboBox<>(model);
        cmbOutput.setRenderer(new MidiCellRenderer());
//        cmbOutput.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
//        cmbOutput.setPreferredSize(new Dimension(100, cmbOutput.getPreferredSize().height));

        jPanel.add(cmbOutput);
        jPanel.add(Box.createHorizontalStrut(5));
        jPanel.add(new JButton("Reload"));
        refreshSynthesizers(model, deviceMethod);
        return jPanel;
    }

    /**
     * @param model
     * @param deviceProvider
     */
    private static void refreshSynthesizers(final DefaultComboBoxModel<MidiDevice> model, final Callable<java.util.List<? extends MidiDevice>> deviceProvider) {
        try {
            model.removeAllElements();
            deviceProvider.call()
                    .forEach(model::addElement);
        } catch (final Exception e) {
            e.printStackTrace();
            error("Cannot reload devices");
        }
    }

    private static void error(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
