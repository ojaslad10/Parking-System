public class Vehicle {
    String driverName;
    String vehicleNumber;
    String mobileNumber;
    String type;
    int fee;
    String slot;

    public Vehicle(String d, String vechnum, String mob, String t, int f, String s) {
        driverName = d;
        vehicleNumber = vechnum;
        mobileNumber = mob;
        type = t;
        fee = f;
        slot = s;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getVehicleType() {
        return type;
    }

    public int getFee() {
        return fee;
    }

    public String getSlotNumber() {
        return slot;
    }

    // just printing it simply
    public String toString() {
        return "Driver: " + driverName + ", Vehicle: " + vehicleNumber + ", Type: " + type;
    }
}
