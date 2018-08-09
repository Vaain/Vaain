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

    public static String[] getPrimaryMap() {
        String[] times = {
              "9:0",
              "9:30",
              "10:0",
              "10:30",
              "11:0",
              "11:30",
              "12:0",
              "12:30",
              "13:0",
              "13:30",
              "14:0",
              "14:30",
              "15:0",
              "15:30",
              "16:0",
              "16:30"
        };

        return times;
    }

    public static String[] getAllTags() {
        String[] tags = {
                "acrylics",
                "french manicure",
                "fade",
                "caesar",
                "blowout",
                "male",
                "female",
                "near",
                "middle-distance",
                "far",
                "expensive",
                "middle-price",
                "cheap",
                "nails",
                "hair",
                "tattoo",
                "makeup",
                "eyebrows"
        };

        return tags;
    }

    public static String[] getLogisticsTags() {
        String[] logTags = {
                "male",
                "female",
                "near",
                "middle-distance",
                "far",
                "expensive",
                "middle-price",
                "cheap"
        };

        return logTags;
    }

    public static String[] getMainTags() {
        String[] mainTags = {
                "nails",
                "hair",
                "tattoo",
                "makeup",
                "eyebrows"
        };

        return mainTags;
    }

    public static String[] getOtherTags() {
        String[] tags = {
            "acrylics",
            "french manicure",
            "fade",
            "caesar",
            "blowout",
        };

        return tags;
    }
}
