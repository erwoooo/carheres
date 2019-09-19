package com.example.maptest.mycartest.UI.TyreUi;

/**
 * Created by ${Author} on 2018/3/21.
 * Use to
 */

public class TyreListStatuBean {
    private four four;
    private String id;
    private one one;
    private three three;
    private two two;

    @Override
    public String toString() {
        return "TyreListStatuBean{" +
                "four=" + four +
                ", id='" + id + '\'' +
                ", one=" + one +
                ", three=" + three +
                ", two=" + two +
                '}';
    }

    public four getFour() {
        return four;
    }

    public void setFour(four four) {
        this.four = four;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public one getOne() {
        return one;
    }

    public void setOne(one one) {
        this.one = one;
    }

    public three getThree() {
        return three;
    }

    public void setThree(three three) {
        this.three = three;
    }

    public two getTwo() {
        return two;
    }

    public void setTwo(two two) {
        this.two = two;
    }

    public TyreListStatuBean(four four, String id, one one, three three, two two) {
        this.four = four;
        this.id = id;
        this.one = one;
        this.three = three;
        this.two = two;
    }

    public TyreListStatuBean() {
    }
    public static class four{
        private int electricState;
        private float pressure;
        private int pressureState;
        private int temperature;
        private int temperatureState;
        private int tireState;

        public int getElectricState() {
            return electricState;
        }

        public void setElectricState(int electricState) {
            this.electricState = electricState;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public int getPressureState() {
            return pressureState;
        }

        public void setPressureState(int pressureState) {
            this.pressureState = pressureState;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getTemperatureState() {
            return temperatureState;
        }

        public void setTemperatureState(int temperatureState) {
            this.temperatureState = temperatureState;
        }

        public int getTireState() {
            return tireState;
        }

        public void setTireState(int tireState) {
            this.tireState = tireState;
        }

        @Override
        public String toString() {
            return "four{" +
                    "electricState=" + electricState +
                    ", pressure=" + pressure +
                    ", pressureState=" + pressureState +
                    ", temperature=" + temperature +
                    ", temperatureState=" + temperatureState +
                    ", tireState=" + tireState +
                    '}';
        }

        public four(int electricState, float pressure, int pressureState, int temperature, int temperatureState, int tireState) {
            this.electricState = electricState;
            this.pressure = pressure;
            this.pressureState = pressureState;
            this.temperature = temperature;
            this.temperatureState = temperatureState;
            this.tireState = tireState;
        }

        public four() {
        }
    }
    public static class three{
        private int electricState;
        private float pressure;
        private int pressureState;
        private int temperature;
        private int temperatureState;
        private int tireState;

        @Override
        public String toString() {
            return "three{" +
                    "electricState=" + electricState +
                    ", pressure=" + pressure +
                    ", pressureState=" + pressureState +
                    ", temperature=" + temperature +
                    ", temperatureState=" + temperatureState +
                    ", tireState=" + tireState +
                    '}';
        }

        public int getElectricState() {
            return electricState;
        }

        public void setElectricState(int electricState) {
            this.electricState = electricState;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public int getPressureState() {
            return pressureState;
        }

        public void setPressureState(int pressureState) {
            this.pressureState = pressureState;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getTemperatureState() {
            return temperatureState;
        }

        public void setTemperatureState(int temperatureState) {
            this.temperatureState = temperatureState;
        }

        public int getTireState() {
            return tireState;
        }

        public void setTireState(int tireState) {
            this.tireState = tireState;
        }

        public three() {
        }

        public three(int electricState, float pressure, int pressureState, int temperature, int temperatureState, int tireState) {
            this.electricState = electricState;
            this.pressure = pressure;
            this.pressureState = pressureState;
            this.temperature = temperature;
            this.temperatureState = temperatureState;
            this.tireState = tireState;
        }
    }
    public static class two{
        private int electricState;
        private float pressure;
        private int pressureState;
        private int temperature;
        private int temperatureState;
        private int tireState;

        @Override
        public String toString() {
            return "two{" +
                    "electricState=" + electricState +
                    ", pressure=" + pressure +
                    ", pressureState=" + pressureState +
                    ", temperature=" + temperature +
                    ", temperatureState=" + temperatureState +
                    ", tireState=" + tireState +
                    '}';
        }

        public int getElectricState() {
            return electricState;
        }

        public void setElectricState(int electricState) {
            this.electricState = electricState;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public int getPressureState() {
            return pressureState;
        }

        public void setPressureState(int pressureState) {
            this.pressureState = pressureState;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getTemperatureState() {
            return temperatureState;
        }

        public void setTemperatureState(int temperatureState) {
            this.temperatureState = temperatureState;
        }

        public int getTireState() {
            return tireState;
        }

        public void setTireState(int tireState) {
            this.tireState = tireState;
        }


        public two(int electricState, float pressure, int pressureState, int temperature, int temperatureState, int tireState) {
            this.electricState = electricState;
            this.pressure = pressure;
            this.pressureState = pressureState;
            this.temperature = temperature;
            this.temperatureState = temperatureState;
            this.tireState = tireState;
        }

        public two() {
        }
    }
    public static class one{
        private int electricState;
        private float pressure;
        private int pressureState;
        private int temperature;
        private int temperatureState;
        private int tireState;

        @Override
        public String toString() {
            return "one{" +
                    "electricState=" + electricState +
                    ", pressure=" + pressure +
                    ", pressureState=" + pressureState +
                    ", temperature=" + temperature +
                    ", temperatureState=" + temperatureState +
                    ", tireState=" + tireState +
                    '}';
        }

        public int getElectricState() {
            return electricState;
        }

        public void setElectricState(int electricState) {
            this.electricState = electricState;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float pressure) {
            this.pressure = pressure;
        }

        public int getPressureState() {
            return pressureState;
        }

        public void setPressureState(int pressureState) {
            this.pressureState = pressureState;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getTemperatureState() {
            return temperatureState;
        }

        public void setTemperatureState(int temperatureState) {
            this.temperatureState = temperatureState;
        }

        public int getTireState() {
            return tireState;
        }

        public void setTireState(int tireState) {
            this.tireState = tireState;
        }

        public one() {
        }

        public one(int electricState, float pressure, int pressureState, int temperature, int temperatureState, int tireState) {
            this.electricState = electricState;
            this.pressure = pressure;
            this.pressureState = pressureState;
            this.temperature = temperature;
            this.temperatureState = temperatureState;
            this.tireState = tireState;
        }
    }
}
