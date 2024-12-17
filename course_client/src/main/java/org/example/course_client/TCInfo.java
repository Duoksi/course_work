package org.example.course_client;

public class TCInfo {
    private final String name;
    private final String address;
    private final int totalSpots;
    private final int pricePerHour;

    public TCInfo(String name, String address, int totalSpots, int pricePerHour) {
        this.name = name;
        this.address = address;
        this.totalSpots = totalSpots;
        this.pricePerHour = pricePerHour;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getTotalSpots() { return totalSpots; }
    public int getPricePerHour() { return pricePerHour; }
}
