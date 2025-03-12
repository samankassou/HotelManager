package hotelmanager;
import hotelmanager.ui.HotelManager;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new HotelManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}