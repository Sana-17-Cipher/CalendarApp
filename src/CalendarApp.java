import javax.swing.*;
import java.awt.*;
import java.time.*;

public class CalendarApp {
    private JFrame frame;
    private JLabel monthLabel;
    private CalendarPanel calendarPanel;
    private YearMonth shownMonth;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarApp().createAndShow());
    }

    private void createAndShow() {
        frame = new JFrame("Calendar with Notes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        shownMonth = YearMonth.now();

        JPanel header = new JPanel(new BorderLayout());
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevBtn = new JButton("◀ Prev");
        JButton todayBtn = new JButton("Today");
        JButton nextBtn = new JButton("Next ▶");
        monthLabel = new JLabel(DateUtils.monthYearTitle(shownMonth), SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        monthLabel.setForeground(new Color(40, 40, 40));

        nav.add(prevBtn);
        nav.add(todayBtn);
        nav.add(nextBtn);
        header.add(monthLabel, BorderLayout.NORTH);
        header.add(nav, BorderLayout.SOUTH);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(header, BorderLayout.NORTH);

        calendarPanel = new CalendarPanel(date -> openNoteDialog(date));
        calendarPanel.showMonth(shownMonth);
        frame.add(calendarPanel, BorderLayout.CENTER);

        JLabel status = new JLabel("Click a date to add/view notes. Notes saved in ./notes/YYYY/MM/DD.txt");
        status.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        frame.add(status, BorderLayout.SOUTH);

        prevBtn.addActionListener(e -> navigate(-1));
        nextBtn.addActionListener(e -> navigate(+1));
        todayBtn.addActionListener(e -> {
            shownMonth = YearMonth.now();
            refresh();
        });

        // 🔔 Reminder: If today has a note, show popup
        LocalDate today = LocalDate.now();
        if (NoteManager.hasNote(today)) {
            JOptionPane.showMessageDialog(frame,
                "You have a note for today!",
                "Reminder",
                JOptionPane.INFORMATION_MESSAGE);
        }

        frame.setSize(720, 620);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void navigate(int deltaMonths) {
        shownMonth = shownMonth.plusMonths(deltaMonths);
        refresh();
    }

    private void refresh() {
        monthLabel.setText(DateUtils.monthYearTitle(shownMonth));
        calendarPanel.showMonth(shownMonth);
    }

    private void openNoteDialog(LocalDate date) {
        NoteDialog dlg = new NoteDialog(frame, date);
        dlg.setVisible(true);
        calendarPanel.showMonth(shownMonth);
    }
}