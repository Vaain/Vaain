package me.braedonvillano.vaain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import me.braedonvillano.vaain.models.Appointment;

public class LocationSchedule {
    public final static int MAX_DAYS_TO_CHECK = 30;
    public final static int DAYS_IN_WEEK = 7;
    public final static int INCREMENT_DATE = 1;
    public final static int MAX_SEATS_IN_STORE = 8;
    public final static int MAX_SLOTS_IN_DAY = 16;
    public final static int MAX_TOTAL_APPOINTMENTS = 2400;

    public String address;
    public Date today;
    public Calendar nextDay;
    public ArrayList<Day> workDays;
    public HashMap slotMap;
    public static int weekdays[] = {
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY
    };

    public LocationSchedule(String address, int numSeats) {
        this.address = address;
        today = new Date();
        nextDay = Calendar.getInstance();
        slotMap = WorkSchedules.getPrimarySchedule();

        workDays = new ArrayList<>(MAX_DAYS_TO_CHECK);
        for (int i = 0; i < MAX_DAYS_TO_CHECK; i++) {
            nextDay.add(Calendar.DAY_OF_MONTH, INCREMENT_DATE);
            workDays.add(i, new Day(nextDay, numSeats));
        }
    }

    /* public sub-classes for making rest of tree */
    public class Day {
        public Calendar day;
        public List<Seat> seats;
        public Boolean active;

        public Day(Calendar day, int numSeats) {
            this.active = true;
            this.day = Calendar.getInstance();
            this.day.set(
                    day.get(Calendar.YEAR),
                    day.get(Calendar.MONTH),
                    day.get(Calendar.DAY_OF_MONTH)
            );
            seats = new ArrayList<>();
            // construct all of the seats
            for (int i = 0; i < numSeats; i++) {
                seats.add(i, new Seat(i + 1));
            }
        }
    }

    public class Seat {
        public Boolean slots[];
        public int seatId;

        public Seat() {
            slots = new Boolean[MAX_SLOTS_IN_DAY];
            for (int i = 0; i < MAX_SLOTS_IN_DAY; i++) {
                slots[i] = true;
            }
        }

        public Seat(int seatId) {
            this.seatId = seatId;
            slots = new Boolean[MAX_SLOTS_IN_DAY];
            for (int i = 0; i < MAX_SLOTS_IN_DAY; i++) {
                slots[i] = true;
            }
        }
    }

    public static class PotentialAppointment {
        public int seatId;
        public int startTime;
        public String start;
        public Calendar appDate;

        public PotentialAppointment(int seatId, int start, Calendar date) {
            this.seatId = seatId;
            this.startTime = start;
            this.appDate = date;
        }

        public PotentialAppointment(int seatId, String start, Calendar date) {
            this.seatId = seatId;
            this.start = start;
            this.appDate = date;
        }
    }

    /* public methods for location-schedule object to determine appointments */
    public void removeOffDays(Boolean weekPrefs[]) {
        List<Day> offDays = new ArrayList<>(MAX_DAYS_TO_CHECK);
        for (Day curDay : workDays) {
            for (int j = 0; j < DAYS_IN_WEEK; j++) {
                if (!weekPrefs[j] && curDay.day.get(Calendar.DAY_OF_WEEK) == weekdays[j]) {
                    offDays.add(curDay);
                }
            }
        }
        workDays.removeAll(offDays);
    }

    public ArrayList<PotentialAppointment> generateAppointments(int length) {
        Boolean isAppointment = false;
        ArrayList<PotentialAppointment> appTimes = new ArrayList<>(MAX_TOTAL_APPOINTMENTS);

        // check all possible appointments in object
        for (Day workday : workDays) {
            for (Seat seat : workday.seats) {
                for (int i = 0; i < MAX_SLOTS_IN_DAY; i++) {
                    if (!seat.slots[i]) { continue; }
                    for (int j = i; j < i + length && j < MAX_SLOTS_IN_DAY; j++) {
                        if (!seat.slots[j]) { i = j; break; }
                        if (seat.slots[j] && j - i == length - 1) {
                            isAppointment = true;
                        }
                    }
                    if (isAppointment) {
                        isAppointment = false;
                        appTimes.add(new PotentialAppointment(seat.seatId, i, workday.day));
                    }
                }
            }
        }
        return appTimes;
    }

    public ArrayList<ArrayList<PotentialAppointment>> generateAppointmentsDaily(int length) {
        Boolean isAppointment = false;
        String[] timeMap = WorkSchedules.getPrimaryMap();
        ArrayList<ArrayList<PotentialAppointment>> appDays = new ArrayList<>(MAX_DAYS_TO_CHECK);

        // check all possible appointments in object
        for (Day workday : workDays) {
            ArrayList<PotentialAppointment> appTimes = new ArrayList<>();
            for (Seat seat : workday.seats) {
                for (int i = 0; i < MAX_SLOTS_IN_DAY; i++) {
                    if (!seat.slots[i]) { continue; }
                    for (int j = i; j < i + length && j < MAX_SLOTS_IN_DAY; j++) {
                        if (!seat.slots[j]) { i = j; break; }
                        if (seat.slots[j] && j - i == length - 1) {
                            isAppointment = true;
                        }
                    }
                    if (isAppointment) {
                        isAppointment = false;
                        appTimes.add(new PotentialAppointment(seat.seatId, timeMap[i], workday.day));
                    }
                }
            }
            if (appTimes.size() != 0) {
                appDays.add(appTimes);
            }
        }
        return appDays;
    }

    public void removeAppointmentList(List<Appointment> appointments) {
        for (Appointment apmnt : appointments) {
            Calendar datetime = Calendar.getInstance();
            datetime.setTime(apmnt.getDateTime());
            datetime.setTimeZone(TimeZone.getTimeZone("PST"));
            removeAppointment(datetime, (int) apmnt.getSeat(), (int) apmnt.getLength());
        }
    }

    public void removeAppointment(Calendar dt, int seatId, int len) {
        for (Day workday : workDays) {
            if (!sameDate(workday.day, dt)) { continue; }
            for (Seat seat : workday.seats) {
                if (seat.seatId != seatId) { continue; }
                String time = dt.get(Calendar.HOUR_OF_DAY) + ":" + dt.get(Calendar.MINUTE);
                int start = (int) slotMap.get(time);
                for (int i = start; i < start + len; i++) {
                    seat.slots[i] = false;
                }
                break;
            }
            break;
        }
    }

    private boolean sameDate(Calendar workday, Calendar checkDate) {
        return (workday.get(Calendar.DAY_OF_MONTH) == checkDate.get(Calendar.DAY_OF_MONTH) &&
                workday.get(Calendar.DAY_OF_WEEK) == checkDate.get(Calendar.DAY_OF_WEEK) &&
                workday.get(Calendar.DAY_OF_MONTH) == checkDate.get(Calendar.DAY_OF_MONTH));
    }

    /* this is a retired function that can be used for scaling or different approach */
    public void setSeatIds(int[] seatIds /* should be string[] */) {
        Seat curSeat;
        List<Seat> rmSeats = new ArrayList<>();
        for (int j = 0; j < workDays.size(); j++) {
            for (int i = 0; i < MAX_SEATS_IN_STORE; i++) {
                curSeat = workDays.get(j).seats.get(i);
                if (i < seatIds.length) {
                    curSeat.seatId = seatIds[i];
                } else {
                    rmSeats.add(curSeat);
                }
            }
            workDays.get(j).seats.remove(rmSeats);
        }
    }

}

