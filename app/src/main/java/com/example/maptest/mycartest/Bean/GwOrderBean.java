package com.example.maptest.mycartest.Bean;

import android.os.Parcelable;



/**
 * Created by ${Author} on 2017/11/24.
 * Use to
 */

public class GwOrderBean {
    private int status;
    private Data Data;

    @Override
    public String toString() {
        return "GwOrderBean{" +
                "status=" + status +
                ", Data=" + Data +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return Data;
    }

    public void setData(Data data) {
        Data = data;
    }

    public GwOrderBean(int status, Data data) {
        this.status = status;
        Data = data;
    }

    public GwOrderBean() {
    }

    public static class Data{
        private String _id;
        private BJSSetting BJSSetting;

        public Data() {
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public BJSSetting getBJSSetting() {
            return BJSSetting;
        }

        public void setBJSSetting(BJSSetting BJSSetting) {
            this.BJSSetting = BJSSetting;
        }

        public Data(String _id, BJSSetting BJSSetting) {
            this._id = _id;
            this.BJSSetting = BJSSetting;
        }

        public static class BJSSetting{
            private Clock Clock;
            private Timming Timming;
            private Week Week;

            public BJSSetting() {
            }

            public Clock getClock() {
                return Clock;
            }

            public void setClock(Clock clock) {
                Clock = clock;
            }

            public Timming getTimming() {
                return Timming;
            }

            public void setTimming(Timming timming) {
                Timming = timming;
            }

            public Week getWeek() {
                return Week;
            }

            public void setWeek(Week week) {
                Week = week;
            }

            public BJSSetting(Clock clock, Timming timming, Week week) {
                Clock = clock;
                Timming = timming;
                Week = week;
            }
            public static class Clock{
                private boolean ClockState;
                private boolean Group1State;
                private String Group1;
                private boolean Group2State;
                private String Group2;
                private boolean Group3State;
                private String Group3;
                private boolean Group4State;
                private String Group4;

                @Override
                public String toString() {
                    return "Clock{" +
                            "ClockState=" + ClockState +
                            ", Group1State=" + Group1State +
                            ", Group1='" + Group1 + '\'' +
                            ", Group2State=" + Group2State +
                            ", Group2='" + Group2 + '\'' +
                            ", Group3State=" + Group3State +
                            ", Group3='" + Group3 + '\'' +
                            ", Group4State=" + Group4State +
                            ", Group4='" + Group4 + '\'' +
                            '}';
                }

                public boolean isClockState() {
                    return ClockState;
                }

                public void setClockState(boolean clockState) {
                    ClockState = clockState;
                }

                public boolean isGroup1State() {
                    return Group1State;
                }

                public void setGroup1State(boolean group1State) {
                    Group1State = group1State;
                }

                public String getGroup1() {
                    return Group1;
                }

                public void setGroup1(String group1) {
                    Group1 = group1;
                }

                public boolean isGroup2State() {
                    return Group2State;
                }

                public void setGroup2State(boolean group2State) {
                    Group2State = group2State;
                }

                public String getGroup2() {
                    return Group2;
                }

                public void setGroup2(String group2) {
                    Group2 = group2;
                }

                public boolean isGroup3State() {
                    return Group3State;
                }

                public void setGroup3State(boolean group3State) {
                    Group3State = group3State;
                }

                public String getGroup3() {
                    return Group3;
                }

                public void setGroup3(String group3) {
                    Group3 = group3;
                }

                public boolean isGroup4State() {
                    return Group4State;
                }

                public void setGroup4State(boolean group4State) {
                    Group4State = group4State;
                }

                public String getGroup4() {
                    return Group4;
                }

                public void setGroup4(String group4) {
                    Group4 = group4;
                }

                public Clock() {
                }

                public Clock(boolean clockState, boolean group1State, String group1, boolean group2State, String group2, boolean group3State, String group3, boolean group4State, String group4) {
                    ClockState = clockState;
                    Group1State = group1State;
                    Group1 = group1;
                    Group2State = group2State;
                    Group2 = group2;
                    Group3State = group3State;
                    Group3 = group3;
                    Group4State = group4State;
                    Group4 = group4;
                }
            }
            public static class  Timming{
                private boolean TimingState;
                private String TimingValue;

                @Override
                public String toString() {
                    return "Timming{" +
                            "TimingState=" + TimingState +
                            ", TimingValue='" + TimingValue + '\'' +
                            '}';
                }

                public boolean isTimingState() {
                    return TimingState;
                }

                public void setTimingState(boolean timingState) {
                    TimingState = timingState;
                }

                public String getTimingValue() {
                    return TimingValue;
                }

                public void setTimingValue(String timingValue) {
                    TimingValue = timingValue;
                }

                public Timming(boolean timingState, String timingValue) {
                    TimingState = timingState;
                    TimingValue = timingValue;
                }

                public Timming() {
                }
            }
            public static class Week{
                private boolean WeekState;
                private boolean Week1;
                private boolean Week2;
                private boolean Week3;
                private boolean Week4;
                private boolean Week5;
                private boolean Week6;
                private boolean Week7;
                private String WeekValue;

                @Override
                public String toString() {
                    return "Week{" +
                            "WeekState=" + WeekState +
                            ", Week1=" + Week1 +
                            ", Week2=" + Week2 +
                            ", Week3=" + Week3 +
                            ", Week4=" + Week4 +
                            ", Week5=" + Week5 +
                            ", Week6=" + Week6 +
                            ", Week7=" + Week7 +
                            ", WeekValue='" + WeekValue + '\'' +
                            '}';
                }

                public boolean isWeekState() {
                    return WeekState;
                }

                public void setWeekState(boolean weekState) {
                    WeekState = weekState;
                }

                public boolean isWeek1() {
                    return Week1;
                }

                public void setWeek1(boolean week1) {
                    Week1 = week1;
                }

                public boolean isWeek2() {
                    return Week2;
                }

                public void setWeek2(boolean week2) {
                    Week2 = week2;
                }

                public boolean isWeek3() {
                    return Week3;
                }

                public void setWeek3(boolean week3) {
                    Week3 = week3;
                }

                public boolean isWeek4() {
                    return Week4;
                }

                public void setWeek4(boolean week4) {
                    Week4 = week4;
                }

                public boolean isWeek5() {
                    return Week5;
                }

                public void setWeek5(boolean week5) {
                    Week5 = week5;
                }

                public boolean isWeek6() {
                    return Week6;
                }

                public void setWeek6(boolean week6) {
                    Week6 = week6;
                }

                public boolean isWeek7() {
                    return Week7;
                }

                public void setWeek7(boolean week7) {
                    Week7 = week7;
                }

                public String getWeekValue() {
                    return WeekValue;
                }

                public void setWeekValue(String weekValue) {
                    WeekValue = weekValue;
                }

                public Week(boolean weekState, boolean week1, boolean week2, boolean week3, boolean week4, boolean week5, boolean week6, boolean week7, String weekValue) {
                    WeekState = weekState;
                    Week1 = week1;
                    Week2 = week2;
                    Week3 = week3;
                    Week4 = week4;
                    Week5 = week5;
                    Week6 = week6;
                    Week7 = week7;
                    WeekValue = weekValue;
                }

                public Week() {
                }
            }
        }
    }
}
