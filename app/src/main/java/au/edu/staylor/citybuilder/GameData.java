package au.edu.staylor.citybuilder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.Random;

public class GameData {

    public static final int DB_ID = 1;
    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    private static final Random rng = new Random();

    private static GameData instance = null;
    private Settings userSettings;
    private SQLiteDatabase db;

    private MapElement[][] map;
    private int width;
    private int height;

    private int money;
    private int mostRecentIncome;

    private int population;
    private int nResidential;
    private int nCommercial;
    private double employmentRate;

    private int gameTime;

    public static GameData get(Context context) {
        if (instance == null) {
            instance = new GameData(context);
        }
        return instance;
    }

    protected GameData(Context context) {
        // Open database
        db = new DbHelper( context.getApplicationContext() ).getWritableDatabase();

        // Read in contents for settings, previous game
        loadSettings();
        setMapDimensions(); // Uses the settings configuration to set the dimensions
        loadGameData();
        loadGameElements();
    }

    public MapElement get(int row, int col) {
        MapElement element = null;
        if (row >= 0 && row < height)
            if (col >= 0 && col < width)
                element = map[row][col];
        return element;
    }
    public int getMoney() { return money; }
    public int getMostRecentIncome() { return mostRecentIncome; }
    public int getGameTime() { return gameTime; }
    public int getPopulation() { return population; }
    public int getResidentCount() { return nResidential; }
    public int getCommercialCount() { return nCommercial; }
    public double getEmploymentRate() { return employmentRate; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void buy (int cost) { money -= cost; }
    public void updateRates() {
        // Update populationRate
        population = userSettings.getFamilySize() * nResidential;

        // Update employment rate
        if (population > 0)
            employmentRate = Math.min(1.0, (double)nCommercial * (double)userSettings.getShopSize() / population);
        else
            employmentRate = 0.0;
    }
    public void addGameTime(int time) {
        gameTime += time;
        mostRecentIncome = population *
                (int) Math.round(employmentRate * userSettings.getSalary() * userSettings.getTaxRate() - userSettings.getServiceCost());
        money += mostRecentIncome;
        updateRates();
    }

    public void changeResidentialCount(int changeBy) {
        nResidential += changeBy;
        updateRates();
    }

    public void changeCommercialCount(int changeBy) {
        nCommercial += changeBy;
        updateRates();
    }

    private void setMapDimensions() {
        width = userSettings.getMapWidth();
        height = userSettings.getMapHeight();
    }

    public Settings getSettings() {
        return userSettings;
    }

    public void updateSettings() {
        DbHelper.edit(db, userSettings);
        setMapDimensions();
        loadGameData();
        loadGameElements();
    }

    public void resetSettings() {
        userSettings.setDefaults();
        DbHelper.edit(db, userSettings);
    }

    public void createNewGame() {
        newGameInfo();

        map = generateMap();
        for(int row=0; row < height; row++) {
            for(int col=0; col < width; col++) {
                DbHelper.add(db, map[row][col], row, col);
            }
        }
    }

    public void newGameInfo() {
        money = userSettings.getInitialMoney();
        gameTime = 0;
        population = 0;
        nResidential = 0;
        nCommercial = 0;
        employmentRate = 0;

        DbHelper.add(db, this);
    }

    public void saveGame() {
        // Save game data
        DbHelper.edit(db,this);

        // Save map elements
        for(int row=0; row < height; row++) {
            for(int col=0; col < width; col++) {
                DbHelper.edit(db, map[row][col], row, col);
            }
        }
    }

    //    ****************************** LOAD FROM DB *********************************
    protected void loadSettings() {
        // Read db contents in
        SettingCursor cursor = new SettingCursor(
                db.query(
                        Schema.SettingSchema.SettingTable.NAME, null, null, null, null, null, null
                )
        );

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                userSettings = cursor.getSetting();
            }

            // If no user settings, set the default
            if (userSettings == null) {
                userSettings = new Settings();
                DbHelper.add(db, userSettings);
            }
        }
        finally {
            cursor.close();
        }
    }

    protected void loadGameData() {
        // Read db contents in
        GameCursor cursor = new GameCursor(
                db.query(
                        Schema.GameSchema.GameTable.NAME, null, null, null, null, null, null
                )
        );

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                money = cursor.getMoney();
                gameTime = cursor.getTime();

                population = cursor.getPopulation();
                nResidential = cursor.getResidentCount();
                nCommercial = cursor.getCommercialCount();
                employmentRate = cursor.getEmploymentRate();
            } else {
                money = userSettings.getInitialMoney();
                gameTime = 0;

                population = 0;
                nResidential = 0;
                nCommercial = 0;
                employmentRate = 0;
            }
        }
        finally {
            cursor.close();
        }
    }

    protected void loadGameElements() {
        // Read db contents in
        ElementCursor cursor = new ElementCursor(
                db.query(
                        Schema.MapElementSchema.MapTable.NAME, null, null, null, null, null, null
                )
        );

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                map = new MapElement[height][width];

                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (!cursor.isAfterLast()) {
                            map[row][col] = cursor.getMapElement();
                            cursor.moveToNext();
                        } else {
                            map = generateMap();
                        }

                    }
                }

            } else {
                createNewGame();
            }
        }
        finally {
            cursor.close();
        }
    }

    private MapElement[][] generateMap() {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[height][width];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                heightField[i][j] =
                        rng.nextInt(HEIGHT_RANGE)
                                + INLAND_BIAS * (
                                Math.min(Math.min(i, j), Math.min(height - i - 1, width - j - 1)) -
                                        Math.min(height, width) / 4);
            }
        }

        int[][] newHf = new int[height][width];
        for(int s = 0; s < SMOOTHING_ITERATIONS; s++)
        {
            for(int i = 0; i < height; i++)
            {
                for(int j = 0; j < width; j++)
                {
                    int areaSize = 0;
                    int heightSum = 0;

                    for(int areaI = Math.max(0, i - AREA_SIZE);
                        areaI < Math.min(height, i + AREA_SIZE + 1);
                        areaI++)
                    {
                        for(int areaJ = Math.max(0, j - AREA_SIZE);
                            areaJ < Math.min(width, j + AREA_SIZE + 1);
                            areaJ++)
                        {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] grid = new MapElement[height][width];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {

                if(heightField[i][j] >= WATER_LEVEL)
                {
                    boolean waterN = (i == 0)          || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == width - 1)  || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == height - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0)          || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) ||          (j == 0) ||         (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) ||          (j == width - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == height - 1) || (j == 0) ||         (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == height - 1) || (j == width - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN && waterE && waterS && waterW &&
                            waterNW && waterNE && waterSW && waterSE;

                    grid[i][j] = new MapElement(
                            !coast,
                            choose(waterN, waterW, waterNW,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                            choose(waterN, waterE, waterNE,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                            choose(waterS, waterW, waterSW,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                            choose(waterS, waterE, waterSE,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                            null);
                }
                else
                {
                    grid[i][j] = new MapElement(
                            false, WATER, WATER, WATER, WATER, null);
                }
            }
        }
        return grid;
    }
    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater,
                              int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId)
    {
        int id;
        if(nsWater)
        {
            if(ewWater)
            {
                id = convexCoastId;
            }
            else
            {
                id = nsCoastId;
            }
        }
        else
        {
            if(ewWater)
            {
                id = ewCoastId;
            }
            else if(diagWater)
            {
                id = concaveCoastId;
            }
            else
            {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }
}
