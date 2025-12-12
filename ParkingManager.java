import java.util.*;

public class ParkingManager {
    public static final int totalSlots = 10;
    public static final int twowhelerFees = 50;
    public static final int fourwhelerFees = 100;

    HashMap<String, Boolean> slotStatus;
    HashMap<String, Vehicle> slotVehicle;

    public ParkingManager() {
        slotStatus = new HashMap<>();
        slotVehicle = new HashMap<>();
        for (int i = 1; i <= totalSlots; i++) {
            slotStatus.put("P" + i, false);
        }
    }

    public String findSlot() {
        
        for (int i = 1; i <= totalSlots; i++) {
            String slot = "P" + i;
            Boolean full = slotStatus.get(slot); 
    
            if (full == false) {
                return slot;
            }
        }
        return null;
    }
    
    public Vehicle parkVehicle(String name, String vnum, String mobile, boolean typeofvech) {
        String slot = findSlot();
        if (slot == null) {
            // can't park if full
            throw new RuntimeException("No empty slots");
        }

        String type = typeofvech ? "Two Wheeler" : "Four Wheeler";
        int fee = typeofvech ? twowhelerFees : fourwhelerFees;

        Vehicle v = new Vehicle(name, vnum, mobile, type, fee, slot);

        slotStatus.put(slot, true);
        slotVehicle.put(slot, v);

        return v;
    }

    public Vehicle exitVehicle(String slot) {

        if (slotStatus.get(slot) == null) {
            throw new RuntimeException("Slot doesn't exist");
        }
    
        if (slotStatus.get(slot) == false) {
            throw new RuntimeException("Nothing parked here");
        }
    
        Vehicle v = slotVehicle.get(slot);
    
        slotStatus.put(slot, false);
        slotVehicle.remove(slot); 
    
        return v;
    }
    

    public boolean isSlotOccupied(String slot) {
        return slotStatus.get(slot);
    }

    public Vehicle getVehicleAtSlot(String slot) {
        return slotVehicle.get(slot);
    }

    public List<Vehicle> getAllParkedVehicles() {
        return new ArrayList<>(slotVehicle.values());
    }
}
