import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

class NoteDialog extends JDialog {
    private final JTextArea text;
    private final LocalDate date;

    NoteDialog(Frame owner, LocalDate date) {
        super(owner, "Notes for " + date.toString(), true);
        this.date = date;
        this.text = new JTextArea(15, 40);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        String existing = NoteManager.readNote(date);
        text.setText(existing);

        JScrollPane scroll = new JScrollPane(text);

        JButton save = new JButton("Save");
        JButton delete = new JButton("Delete");
        JButton close = new JButton("Close");

        save.addActionListener(e -> {
            NoteManager.saveNote(date, text.getText());
            JOptionPane.showMessageDialog(this, "Saved!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        delete.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Delete note for " + date + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                NoteManager.deleteNote(date);
                text.setText("");
            }
        });

        close.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(delete);
        buttons.add(save);
        buttons.add(close);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }
}
