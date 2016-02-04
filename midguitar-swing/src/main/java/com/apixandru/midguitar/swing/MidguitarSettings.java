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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 29, 2016
 */
public class MidguitarSettings extends JPanel {

    private final DefaultComboBoxModel<MidiInput> modelInput = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<MidiDevice> modelOuput = new DefaultComboBoxModel<>();

    private final DefaultComboBoxModel<String> fromModel = newModel(40);
    private final DefaultComboBoxModel<String> toModel = newModel(86);

    private final JCheckBox chkEnableOutput = new JCheckBox("Enable Output");

    private final JCheckBox chkIncludeSharp = new JCheckBox("Include sharp notes");

    private MidiInput input;
    private final NoteMatcherListener noteListener;

    MidguitarSettings(final MidiDevices deviceProvider, final NoteMatcherListener noteListener) {
        this.noteListener = noteListener;

        final Dimension minimumSize = new Dimension(640, 460);
        setMinimumSize(minimumSize);
        setPreferredSize(minimumSize);
        setBorder(new LineBorder(Color.BLACK, 2));
        setLayout(new BorderLayout());
        final JPanel jPanel = new JPanel();
        add(jPanel, BorderLayout.NORTH);
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(createPanel(null, modelInput, deviceProvider::getInputDevices));
        jPanel.add(createPanel(chkEnableOutput, modelOuput, deviceProvider::getSynthesizers));
        jPanel.add(createConfig());
        jPanel.add(createStart());
        jPanel.add(Box.createVerticalStrut(130));
        final NoteTable noteTable = new NoteTable();
        jPanel.add(noteTable);
        modelInput.addElement(noteTable);
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
                input = (MidiInput) modelInput.getSelectedItem();
                if (chkEnableOutput.isSelected()) {
                    final Synthesizer selectedItem = (Synthesizer) modelOuput.getSelectedItem();
                    this.input.addListener(new SynthNoteListener(selectedItem));
                    selectedItem.open();
                }

                final List<String> allNotes = Notes.getNoteNames();
                final int from = allNotes.indexOf(fromModel.getSelectedItem());
                final int to = allNotes.indexOf(toModel.getSelectedItem());

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

    private void adjust(final DefaultComboBoxModel<String> model) {
        final Object selectedItem = model.getSelectedItem();
        model.removeAllElements();
        Notes.getSupportedNoteNames(chkIncludeSharp.isSelected()).forEach(model::addElement);
        model.setSelectedItem(selectedItem);
    }

    private JPanel createConfig() {
        final JPanel noteControl = new JPanel();
        noteControl.setLayout(new BoxLayout(noteControl, BoxLayout.X_AXIS));
        noteControl.add(chkIncludeSharp);
        chkIncludeSharp.addActionListener(e -> {
            adjust(fromModel);
            adjust(toModel);
        });
        adjust(fromModel);
        adjust(toModel);
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
    private static <T> JPanel createPanel(
            final JCheckBox checkbox,
            final DefaultComboBoxModel<T> model,
            final Callable<List<? extends T>> deviceMethod) {

        final JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

        if (null != checkbox) {
            jPanel.add(checkbox);
        }
        final JComboBox<T> cmbOutput = new JComboBox<>(model);
        cmbOutput.setRenderer(new MidiCellRenderer());

        jPanel.add(cmbOutput);
        jPanel.add(Box.createHorizontalStrut(5));
        final JButton reload = new JButton("Rescan");
        reload.addActionListener(e -> refreshSynthesizers(model, deviceMethod));
        jPanel.add(reload);
        refreshSynthesizers(model, deviceMethod);
        return jPanel;
    }

    /**
     * @param model
     * @param deviceProvider
     */
    private static <T> void refreshSynthesizers(final DefaultComboBoxModel<T> model, final Callable<List<? extends T>> deviceProvider) {
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
