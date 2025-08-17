import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;

class NoteManager {
    private static final Path ROOT = Paths.get("notes");

    private static Path pathFor(LocalDate date) {
        Path dir = ROOT.resolve(String.format("%04d", date.getYear()))
                       .resolve(String.format("%02d", date.getMonthValue()));
        return dir.resolve(String.format("%02d.txt", date.getDayOfMonth()));
    }

    static boolean hasNote(LocalDate date) {
        Path p = pathFor(date);
        try {
            return Files.exists(p) && Files.size(p) > 0;
        } catch (IOException e) {
            return Files.exists(p);
        }
    }

    static String readNote(LocalDate date) {
        Path p = pathFor(date);
        if (Files.exists(p)) {
            try {
                return Files.readString(p, StandardCharsets.UTF_8);
            } catch (IOException ignored) { }
        }
        return "";
    }

    static void saveNote(LocalDate date, String content) {
        Path p = pathFor(date);
        try {
            Files.createDirectories(p.getParent());
            Files.writeString(p, content == null ? "" : content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save note: " + e.getMessage(), e);
        }
    }

    static void deleteNote(LocalDate date) {
        Path p = pathFor(date);
        try {
            Files.deleteIfExists(p);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete note: " + e.getMessage(), e);
        }
    }
}