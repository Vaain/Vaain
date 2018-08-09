package me.braedonvillano.vaain;

import java.util.HashMap;

public class WorkSchedules {
    public static HashMap getPrimarySchedule() {
        HashMap slotMapping = new HashMap();
        slotMapping.put("9:00", 0);
        slotMapping.put("9:30", 1);
        slotMapping.put("10:00", 2);
        slotMapping.put("10:30", 3);
        slotMapping.put("11:00", 4);
        slotMapping.put("11:30", 5);
        slotMapping.put("12:00", 6);
        slotMapping.put("12:30", 7);
        slotMapping.put("1:00", 8);
        slotMapping.put("1:30", 9);
        slotMapping.put("2:00", 10);
        slotMapping.put("2:30", 11);
        slotMapping.put("3:00", 12);
        slotMapping.put("3:30", 13);
        slotMapping.put("4:00", 14);
        slotMapping.put("4:30", 15);

        return slotMapping;
    }

    public static String[] getPrimaryMap() {
        String[] times = {
              "9:00",
              "9:30",
              "10:00",
              "10:30",
              "11:00",
              "11:30",
              "12:00",
              "12:30",
              "1:00",
              "1:30",
              "2:00",
              "2:30",
              "3:00",
              "3:30",
              "4:00",
              "4:30"
        };

        return times;
    }
}
