package com.app.rally.route.util;

public class ClubDistanceCalculator {
    private final double currentLatitude;
    private final double currentLongitude;

    public ClubDistanceCalculator(double currentLatitude, double currentLongitude) {
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }


    public  double calculateDistance( double lat, double lon) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(lat - currentLatitude);
        double lonDistance = Math.toRadians(lon - currentLongitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(currentLatitude)) * Math.cos(Math.toRadians(lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (R * c * 1000); // Convert to meters

    }
}
