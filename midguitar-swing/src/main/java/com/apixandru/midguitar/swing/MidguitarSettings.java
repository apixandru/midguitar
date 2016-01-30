package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidiDevices;
import com.apixandru.midguitar.model.MidiInput;
import com.apixandru.midguitar.model.MidiInputRealDevice;
import com.apixandru.midguitar.model.Notes;
import com.apixandru.midguitar.model.SynthNoteListener;
import com.apixandru.midguitar.model.matcher.NoteMatcher;
import com.apixandru.midguitar.model.matcher.NoteMatcherListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
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

    private final DefaultComboBoxModel<String> fromModel = newModel(40);
    private final DefaultComboBoxModel<String> toModel = newModel(86);

    private final JCheckBox chkEnableInput = new JCheckBox("Enable Input");
    private final JCheckBox chkEnableOutput = new JCheckBox("Enable Output");

    private final JCheckBox chkIncludeSharp = new JCheckBox("Include sharp notes");

    private MidiInput input;
    private final NoteMatcherListener noteListener;

    MidguitarSettings(final MidiDevices deviceProvider, final NoteMatcherListener noteListener) {
        this.noteListener = noteListener;

        final Dimension minimumSize = new Dimension(500, 460);
        setMinimumSize(minimumSize);
        setPreferredSize(minimumSize);
        setBorder(new LineBorder(Color.BLACK, 2));
        setLayout(new BorderLayout());
        final JPanel jPanel = new JPanel();
        add(jPanel, BorderLayout.NORTH);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(createPanel(chkEnableInput, modelInput, deviceProvider::getInputDevices));
        jPanel.add(createPanel(chkEnableOutput, modelOuput, deviceProvider::getSynthesizers));
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
                if (null != input) {
                    input.close();
                }
                input = new MidiInputRealDevice((MidiDevice) modelInput.getSelectedItem());
                if (chkEnableOutput.isSelected()) {
                    final Synthesizer selectedItem = (Synthesizer) modelOuput.getSelectedItem();
                    this.input.addListener(new SynthNoteListener(selectedItem));
                    selectedItem.open();
                }

                final int from = Notes.getNoteNames().indexOf(fromModel.getSelectedItem());
                final int to = Notes.getNoteNames().indexOf(toModel.getSelectedItem());

                final NoteMatcher noteMatcher = new NoteMatcher(from, to, chkIncludeSharp.isSelected());
                noteMatcher.addNoteMatchListener(noteListener);
                input.addListener(noteMatcher);


                input.open();
            } catch (MidiUnavailableException e1) {
                error("Cannot start device");
            }
        });
        jPanel.add(start);
        return jPanel;
    }

    private JPanel createConfig() {
        final JPanel noteControl = new JPanel();
        noteControl.setLayout(new BoxLayout(noteControl, BoxLayout.X_AXIS));
        noteControl.add(chkIncludeSharp);
        noteControl.add(new JLabel("   From   "));
        noteControl.add(new JComboBox<>(fromModel));
        noteControl.add(new JLabel("   To   "));
        noteControl.add(new JComboBox<>(toModel));
        noteControl.add(Box.createHorizontalStrut(88));
        noteControl.setBorder(new EmptyBorder(5, 5, 5, 5));
        return noteControl;
    }

    /**
     * @param note
     * @return
     */
    private static DefaultComboBoxModel<String> newModel(final int note) {
        final String[] notes = Notes.getNoteNames().toArray(new String[128]);
        final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(notes);
        model.setSelectedItem(notes[note]);
        return model;
    }

    /**
     * @param checkbox
     * @param model
     * @param deviceMethod
     * @return
     */
    private static JPanel createPanel(
            final JCheckBox checkbox,
            final DefaultComboBoxModel<MidiDevice> model,
            final Callable<List<? extends MidiDevice>> deviceMethod) {

        final JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

        jPanel.add(checkbox);
        final JComboBox<MidiDevice> cmbOutput = new JComboBox<>(model);
        cmbOutput.setRenderer(new MidiCellRenderer());

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
