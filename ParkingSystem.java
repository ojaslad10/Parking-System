import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ParkingSystem {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ParkingSystemUI().setVisible(true);
            }
        });
    }
}
