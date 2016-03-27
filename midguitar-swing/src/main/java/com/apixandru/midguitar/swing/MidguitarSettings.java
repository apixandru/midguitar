package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.Notes;
import com.apixandru.midguitar.model.matcher.NoteMatcher;
import com.apixandru.midguitar.model.matcher.NoteMatcherListener;
import com.apixandru.utils.midi.MidiInput;
import com.apixandru.utils.midi.javasound.JsMidiDevices;
import com.apixandru.utils.midi.javasound.JsSynthNoteListener;
import com.apixandru.utils.swing.components.AxComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 29, 2016
 */
class MidguitarSettings extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(MidguitarSettings.class);

    //    private final AxComboBox<MidiInput> modelInput = new AxComboBox<>(this, "modelInput");
    private final DefaultComboBoxModel<MidiInput> modelInput = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<MidiDevice> modelOuput = new DefaultComboBoxModel<>();

    private final AxComboBox<String> cmbFrom = new AxComboBox<>(this, "cmbFrom");
    private final AxComboBox<String> cmbTo = new AxComboBox<>(this, "cmbTo");

    private final JCheckBox chkEnableOutput = new JCheckBox("Enable Output");

    private final JCheckBox chkIncludeSharp = new JCheckBox("Include sharp notes");
    private final transient List<NoteMatcherListener> listeners = new ArrayList<>();
    private final NoteTable noteTable = new NoteTable();
    private transient MidiInput input;
    private transient NoteMatcher noteMatcher;

    MidguitarSettings(final JsMidiDevices deviceProvider) {

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
        jPanel.add(Box.createVerticalStrut(128));
        jPanel.add(noteTable);
        modelInput.addElement(noteTable);
    }

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

    private static <T> void refreshSynthesizers(final DefaultComboBoxModel<T> model, final Callable<List<? extends T>> deviceProvider) {
        try {
            model.removeAllElements();
            deviceProvider.call()
                    .forEach(model::addElement);
        } catch (final Exception e) {
            error("Cannot reload devices", e);
        }
    }

    private static void error(String message, Exception e) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        log.error(message, e);
    }

    private Component createStart() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.add(Box.createHorizontalGlue());
        final JButton start = new JButton("Start");
        start.addActionListener(listener -> {
            try {
                if (null != input) {
                    input.close();
                }
                input = (MidiInput) modelInput.getSelectedItem();
                if (chkEnableOutput.isSelected()) {
                    final Synthesizer selectedItem = (Synthesizer) modelOuput.getSelectedItem();
                    this.input.addListener(new JsSynthNoteListener(selectedItem));
                    selectedItem.open();
                }

                final List<String> allNotes = Notes.getNoteNames();
                final int from = allNotes.indexOf(cmbFrom.getSelectedItem());
                final int to = allNotes.indexOf(cmbTo.getSelectedItem());

                noteMatcher = new NoteMatcher(from, to, chkIncludeSharp.isSelected());
                this.listeners.forEach(noteMatcher::addNoteMatchListener);
                noteMatcher.addNoteMatchListener(noteTable);
                noteTable.configure(noteMatcher.getAllNotes());
                input.addListener(noteMatcher);


                input.open();
            } catch (MidiUnavailableException e) {
                error("Cannot start device", e);
            }
        });
        jPanel.add(start);
        return jPanel;
    }

    private void adjust(final AxComboBox<String> model) {
        final boolean includeSharpNotes = chkIncludeSharp.isSelected();
        model.setup(Notes.getSupportedNoteNames(includeSharpNotes));
    }

    private JPanel createConfig() {
        final JPanel noteControl = new JPanel();
        noteControl.setLayout(new BoxLayout(noteControl, BoxLayout.X_AXIS));
        noteControl.add(chkIncludeSharp);
        chkIncludeSharp.addActionListener(e -> {
            adjust(cmbFrom);
            adjust(cmbTo);
        });
        adjust(cmbFrom);
        adjust(cmbTo);
        noteControl.add(new JLabel("   From   "));
        noteControl.add(cmbFrom);
        noteControl.add(new JLabel("   To   "));
        noteControl.add(cmbTo);
        noteControl.add(Box.createHorizontalStrut(88));
        noteControl.setBorder(new EmptyBorder(5, 5, 5, 5));
        return noteControl;
    }

    void addNoteMatcherListener(NoteMatcherListener listener) {
        this.listeners.add(listener);
    }
}
