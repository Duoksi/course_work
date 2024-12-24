package org.example.course_client;

/**
 * Класс, представляющий информацию о торговом центре (ТЦ) и его парковке.
 */
public class TCInfo {

    private final String name;
    private final String address;
    private final int totalSpots;
    private final int pricePerHour;

    /**
     * Конструктор для создания объекта информации о ТЦ.
     *
     * @param name       Название торгового центра.
     * @param address    Адрес торгового центра.
     * @param totalSpots Общее количество парковочных мест.
     * @param pricePerHour Цена за парковку на один час.
     */
    public TCInfo(String name, String address, int totalSpots, int pricePerHour) {
        this.name = name;
        this.address = address;
        this.totalSpots = totalSpots;
        this.pricePerHour = pricePerHour;
    }

    // Геттеры

    /**
     * Получает название торгового центра.
     *
     * @return название ТЦ.
     */
    public String getName() {
        return name;
    }

    /**
     * Получает адрес торгового центра.
     *
     * @return адрес ТЦ.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Получает общее количество парковочных мест.
     *
     * @return количество парковочных мест.
     */
    public int getTotalSpots() {
        return totalSpots;
    }

    /**
     * Получает цену за парковку на один час.
     *
     * @return цена за час.
     */
    public int getPricePerHour() {
        return pricePerHour;
    }
}