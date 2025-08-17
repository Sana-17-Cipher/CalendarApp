import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.function.Consumer;

class CalendarPanel extends JPanel {
    private final JButton[] dayButtons = new JButton[42];
    private final Consumer<LocalDate> onDateClick;

    CalendarPanel(Consumer<LocalDate> onDateClick) {
        this.onDateClick = onDateClick;
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new GridLayout(1, 7));
        String[] weekdays = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        for (String w : weekdays) {
            JLabel lbl = new JLabel(w, SwingConstants.CENTER);
            lbl.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.add(lbl);
        }
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(6, 7, 4, 4));
        grid.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        for (int i = 0; i < dayButtons.length; i++) {
            JButton b = new JButton();
            b.setFocusPainted(false);
            b.setMargin(new Insets(6, 6, 6, 6));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            b.addActionListener(e -> {
                LocalDate d = (LocalDate) b.getClientProperty("date");
                if (d != null) {
                    onDateClick.accept(d);
                }
            });
            dayButtons[i] = b;
            grid.add(b);
        }
        add(grid, BorderLayout.CENTER);
    }

    void showMonth(YearMonth ym) {
        LocalDate first = ym.atDay(1);
        int length = ym.lengthOfMonth();
        int lead = first.getDayOfWeek().getValue() - 1;

        for (int i = 0; i < 42; i++) {
            JButton b = dayButtons[i];
            b.putClientProperty("date", null);
            b.setEnabled(false);
            b.setText("");
            b.setToolTipText(null);
            b.setBackground(UIManager.getColor("Button.background"));
            b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

        }

        for (int i = 0; i < length; i++) {
            int slot = lead + i;
            JButton b = dayButtons[slot];
            LocalDate date = first.plusDays(i);
            b.putClientProperty("date", date);
            b.setEnabled(true);

            String label = Integer.toString(i + 1);
            if (NoteManager.hasNote(date)) {
                label += " 📌";
                b.setToolTipText("Note exists");
            }
            b.setText(label);

            if (date.equals(LocalDate.now())) {
                b.setBackground(new Color(255, 255, 180));
            } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                b.setBackground(new Color(220, 235, 255));
            } else {
                b.setBackground(Color.WHITE);
            }

            b.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
    }
}
