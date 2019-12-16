package au.edu.staylor.citybuilder;

public class Settings {
    private int id;

    private int mapWidth;
    private int mapHeight;
    private int initialMoney;
    private int familySize;
    private int shopSize;
    private int salary;
    private double taxRate;
    private int serviceCost;
    private int houseBuildingCost;
    private int commBuildingCost;
    private int roadBuildingCost;

    public Settings() {
        this.id = 1;
        setDefaults();
    }

    public Settings(int id, int mapWidth, int mapHeight, int initialMoney, int familySize, int shopSize, int salary,
                    double taxRate, int serviceCost, int houseBuildingCost, int commBuildingCost, int roadBuildingCost) {
        this.id = id;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.initialMoney = initialMoney;
        this.familySize = familySize;
        this.shopSize = shopSize;
        this.salary = salary;
        this.taxRate = taxRate;
        this.serviceCost = serviceCost;
        this.houseBuildingCost = houseBuildingCost;
        this.commBuildingCost = commBuildingCost;
        this.roadBuildingCost = roadBuildingCost;
    }

    public void setDefaults() {
        mapWidth = 50;
        mapHeight = 10;
        initialMoney = 1000;
        familySize = 4;
        shopSize = 6;
        salary = 10;
        taxRate = 0.3;
        serviceCost = 2;
        houseBuildingCost = 100;
        commBuildingCost = 500;
        roadBuildingCost = 20;
    }

    public void setMapWidth(int width) { mapWidth = width; }
    public void setMapHeight(int height) { mapHeight = height; }
    public void setInitialMoney(int money) { initialMoney = money; }
    public void setFamilySize(int familySize) { this.familySize = familySize; }
    public void setShopSize(int shopSize) { this.shopSize = shopSize; }
    public void setSalary(int salary) { this.salary = salary; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }
    public void setServiceCost(int serviceCost) { this.serviceCost = serviceCost; }
    public void setHouseBuildingCost(int houseCost) { houseBuildingCost = houseCost; }
    public void setCommBuildingCost(int commCost) { commBuildingCost = commCost; }
    public void setRoadBuildingCost(int roadCost) { roadBuildingCost = roadCost; }

    public int getId() { return id; }
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
    public int getInitialMoney() { return initialMoney; }
    public int getFamilySize() { return familySize; }
    public int getShopSize() { return shopSize; }
    public int getSalary() { return salary; }
    public double getTaxRate() { return taxRate; }
    public int getServiceCost() { return serviceCost; }
    public int getHouseBuildingCost() { return houseBuildingCost; }
    public int getCommBuildingCost() { return commBuildingCost; }
    public int getRoadBuildingCost() { return roadBuildingCost; }

    // Cost for nature and deleting is free
    public int getCostFromType(Type type) {
        int cost = 0;
        switch (type) {
            case RESIDENTIAL:
                cost = getHouseBuildingCost();
                break;
            case COMMERCIAL:
                cost = getCommBuildingCost();
                break;
            case ROAD:
                cost = getRoadBuildingCost();
                break;
        }
        return cost;
    }

    public String toString() {
        return "Settings information: \n\tMap Width: " + mapWidth + "\n\tMap Height: " + mapHeight +
                "\n\tInitial Money: " + initialMoney + "\n\tFamily size: " + familySize +
                "\n\tShop size: " + shopSize + "\n\tSalary: " + salary + "\n\tTax rate: " + taxRate +
                "\n\tService cost: " + serviceCost + "\n\tHouse building cost: " + houseBuildingCost +
                "\n\tCommercial building cost: " + commBuildingCost + "\n\tRoad building cost: " +
                roadBuildingCost;
    }
}

