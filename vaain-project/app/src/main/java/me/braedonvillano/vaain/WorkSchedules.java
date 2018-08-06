package me.braedonvillano.vaain;

import java.util.HashMap;

public class WorkSchedules {
    public static HashMap getPrimarySchedule() {
        HashMap slotMapping = new HashMap();
        slotMapping.put("9:0", 0);
        slotMapping.put("9:30", 1);
        slotMapping.put("10:0", 2);
        slotMapping.put("10:30", 3);
        slotMapping.put("11:0", 4);
        slotMapping.put("11:30", 5);
        slotMapping.put("12:0", 6);
        slotMapping.put("12:30", 7);
        slotMapping.put("13:0", 8);
        slotMapping.put("13:30", 9);
        slotMapping.put("14:0", 10);
        slotMapping.put("14:30", 11);
        slotMapping.put("15:0", 12);
        slotMapping.put("15:30", 13);
        slotMapping.put("16:0", 14);
        slotMapping.put("16:30", 15);

        return slotMapping;
    }
}
