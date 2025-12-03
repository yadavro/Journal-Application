package com.rohit.journalApp.Entity;


public class Wether {
    public Location location;
    public Current current;

    public class Current {
        public double temp_c;
        public double wind_kph;
        public double feelslike_c;
    }

    public class Location {
        public String name;
        public String country;
    }
}







