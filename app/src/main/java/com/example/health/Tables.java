package com.example.health;

public final class Tables {

    public static class StepsEveryMinute {
        public static final String TABLE_NAME = "stepsEveryMinute";
        public static final String STEPSEVERYMINUTE_ID = "_id";
        public static final String TIME = "time";
        public static final String STEPS = "steps_per_minute";
    }

    public static class StepsPerDay {
        public static final String TABLE_NAME = "stepsPerDay";
        public static final String STEPSPERDAY_ID = "_id";
        public static final String DAY = "day";
        public static final String STEPS = "steps_per_day";
        public static final String KM = "km";
        public static final String GOALS_STEPS = "goals_steps";
    }

    public static class HealthInfo {
        public static final String TABLE_NAME = "healthInfo";
        public static final String HEALTHINFO_ID = "_id";
        public static final String DATE = "date";
        public static final String WEIGHT = "weight";
        public static final String HEIGHT = "height";
        public static final String WATER = "water";
    }
}
