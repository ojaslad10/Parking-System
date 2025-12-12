import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParkingSystemUI extends JFrame {
    private ParkingManager parkingManager;
    
    // UI Components
    private JTextField nameField, vehicleNumberField, mobileField;
    private JComboBox<String> vehicleTypeCombo;
    private JTextArea slipArea;
    private JPanel slotsPanel;
    private JTable vehiclesTable;
    private DefaultTableModel tableModel;
    
    public ParkingSystemUI() {
        // Initialize parking manager
        parkingManager = new ParkingManager();
        
        // Set up the UI
        setTitle("Parking Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main layout
        setLayout(new BorderLayout(10, 10));
        
        // Create panels
        JPanel inputPanel = createInputPanel();
        JPanel statusPanel = createStatusPanel();
        
        add(inputPanel, BorderLayout.WEST);
        add(statusPanel, BorderLayout.CENTER);
        
        // Update UI
        updateSlotsDisplay();
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Vehicle Entry"));
        panel.setPreferredSize(new Dimension(300, 500));
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Driver name
        formPanel.add(new JLabel("Driver Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        // Vehicle number
        formPanel.add(new JLabel("Vehicle Number:"));
        vehicleNumberField = new JTextField();
        formPanel.add(vehicleNumberField);
        
        // Mobile number
        formPanel.add(new JLabel("Mobile Number:"));
        mobileField = new JTextField();
        formPanel.add(mobileField);
        
        // Vehicle type
        formPanel.add(new JLabel("Vehicle Type:"));
        vehicleTypeCombo = new JComboBox<>(new String[]{"Two Wheeler", "Four Wheeler"});
        formPanel.add(vehicleTypeCombo);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton parkButton = new JButton("Park Vehicle");
        JButton exitButton = new JButton("Exit Vehicle");
        
        parkButton.addActionListener(new ActionListener() {
  
            public void actionPerformed(ActionEvent e) {
                parkVehicle();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
          
            public void actionPerformed(ActionEvent e) {
                exitVehicle();
            }
        });
        
        buttonPanel.add(parkButton);
        buttonPanel.add(exitButton);
        
        // Parking slip area
        slipArea = new JTextArea(10, 25);
        slipArea.setEditable(false);
        JScrollPane slipScrollPane = new JScrollPane(slipArea);
        slipScrollPane.setBorder(BorderFactory.createTitledBorder("Parking Slip"));
        
        panel.add(formPanel);
        panel.add(buttonPanel);
        panel.add(slipScrollPane);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Slots visualization
        slotsPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        slotsPanel.setBorder(BorderFactory.createTitledBorder("Parking Slots"));
        
        // Parked vehicles table
        String[] columns = {"Slot", "Vehicle Number", "Driver", "Type", "Fee"};
        tableModel = new DefaultTableModel(columns, 0);
        vehiclesTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(vehiclesTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Parked Vehicles"));
        
        panel.add(slotsPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void parkVehicle() {
        // Get input values
        String driverName = nameField.getText().trim();
        String vehicleNumber = vehicleNumberField.getText().trim().toUpperCase();
        String mobileNumber = mobileField.getText().trim();
        boolean isTwoWheeler = vehicleTypeCombo.getSelectedIndex() == 0;
        
        // Validate inputs
        if (driverName.isEmpty() || vehicleNumber.isEmpty() || mobileNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Park vehicle
            Vehicle vehicle = parkingManager.parkVehicle(
                driverName, vehicleNumber, mobileNumber, isTwoWheeler
            );
            
            // Generate and display parking slip
            generateParkingSlip(vehicle);
            
            // Update UI
            updateSlotsDisplay();
            updateVehicleTable();
            clearInputFields();
            
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Parking Error", JOptionPane.WARNING_MESSAGE);
        }
        
    }
    
    private void exitVehicle() {
        String slotNumber = JOptionPane.showInputDialog(this, "Enter Parking Slot Number (P1-P10):");
        
        if (slotNumber != null && !slotNumber.isEmpty()) {
            // Validate slot format
            if (!slotNumber.matches("P[1-9]|P10")) {
                JOptionPane.showMessageDialog(this, "Invalid slot number! Please enter P1-P10", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Remove vehicle
                Vehicle vehicle = parkingManager.exitVehicle(slotNumber);
                
                // Show exit message
                JOptionPane.showMessageDialog(this, 
                        "Vehicle " + vehicle.getVehicleNumber() + " has left the parking.\n" +
                        "Fee paid: Rs. " + vehicle.getFee(), 
                        "Vehicle Exit", JOptionPane.INFORMATION_MESSAGE);
                
                // Update UI
                updateSlotsDisplay();
                updateVehicleTable();
                slipArea.setText("");
                
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Exit Error", JOptionPane.WARNING_MESSAGE);
            }
            
        }
    }
    
    private void generateParkingSlip(Vehicle vehicle) {
        StringBuilder slip = new StringBuilder();
        slip.append("======= PARKING SLIP =======\n");
        slip.append("Driver Name: ").append(vehicle.getDriverName()).append("\n");
        slip.append("Vehicle Number: ").append(vehicle.getVehicleNumber()).append("\n");
        slip.append("Mobile Number: ").append(vehicle.getMobileNumber()).append("\n");
        slip.append("Vehicle Type: ").append(vehicle.getVehicleType()).append("\n");
        slip.append("Parking Fee: Rs. ").append(vehicle.getFee()).append("\n");
        slip.append("Slot Number: ").append(vehicle.getSlotNumber()).append("\n");
        slip.append("==========================\n");
        
        slipArea.setText(slip.toString());
    }
    
    private void updateSlotsDisplay() {
        slotsPanel.removeAll();
        
        for (int i = 1; i <= ParkingManager.totalSlots; i++) {
            String slotId = "P" + i;
            JButton slotButton = new JButton(slotId);
            
            if (parkingManager.isSlotOccupied(slotId)) {
                // Occupied slot
                Vehicle v = parkingManager.getVehicleAtSlot(slotId);
                slotButton.setText(slotId + " - " + v.getVehicleNumber());
                slotButton.setBackground(Color.RED);
                slotButton.setForeground(Color.WHITE);
            } else {
                // Empty slot
                slotButton.setText(slotId + " - Empty");
                slotButton.setBackground(Color.GREEN);
            }
            
            final String slot = slotId;
            slotButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (parkingManager.isSlotOccupied(slot)) {
                        // Show vehicle details if slot is occupied
                        Vehicle v = parkingManager.getVehicleAtSlot(slot);
                        JOptionPane.showMessageDialog(ParkingSystemUI.this, 
                                "Driver: " + v.getDriverName() + "\n" +
                                "Vehicle Number: " + v.getVehicleNumber() + "\n" +
                                "Mobile: " + v.getMobileNumber() + "\n" +
                                "Type: " + v.getVehicleType() + "\n" +
                                "Fee: Rs. " + v.getFee(), 
                                "Vehicle in Slot " + slot, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            
            slotsPanel.add(slotButton);
        }
        
        slotsPanel.revalidate();
        slotsPanel.repaint();
    }
    
    private void updateVehicleTable() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Add rows for each parked vehicle
        for (Vehicle v : parkingManager.getAllParkedVehicles()) {
            tableModel.addRow(new Object[] {
                v.getSlotNumber(),
                v.getVehicleNumber(),
                v.getDriverName(),
                v.getVehicleType(),
                "Rs. " + v.getFee()
            });
        }
    }
    
    private void clearInputFields() {
        nameField.setText("");
        vehicleNumberField.setText("");
        mobileField.setText("");
        vehicleTypeCombo.setSelectedIndex(0);
    }
}