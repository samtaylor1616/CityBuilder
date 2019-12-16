package au.edu.staylor.citybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.edu.staylor.citybuilder.Schema.SettingSchema.SettingTable;
import au.edu.staylor.citybuilder.Schema.MapElementSchema.MapTable;
import au.edu.staylor.citybuilder.Schema.GameSchema.GameTable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String NO_STRUCTURE = "NO STRUCTURE";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "games.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSettingsTable(db);
        createMapElementTable(db);
        createGameTable(db);
    }

    // ************************************ CREATE TABLES ************************************ // */
    private void createSettingsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SettingTable.NAME + "(" +
                SettingTable.Cols.ID + " INTEGER, " +
                SettingTable.Cols.MAP_WIDTH + " INTEGER DEFAULT 55, " +
                SettingTable.Cols.MAP_HEIGHT + " INTEGER DEFAULT 10, " +
                SettingTable.Cols.INITIAL_MONEY + " INTEGER DEFAULT 1000, " +
                SettingTable.Cols.FAMILY_SIZE + " INTEGER DEFAULT 4, " +
                SettingTable.Cols.SHOP_SIZE + " INTEGER DEFAULT 6, " +
                SettingTable.Cols.SALARY + " INTEGER DEFAULT 10, " +
                SettingTable.Cols.TAX_RATE + " REAL DEFAULT 0.3, " +
                SettingTable.Cols.SERVICE_COST + " INTEGER DEFAULT 2, " +
                SettingTable.Cols.HOUSE_BUILD_COST + " INTEGER DEFAULT 100, " +
                SettingTable.Cols.COMM_BUILD_COST + " INTEGER DEFAULT 500, " +
                SettingTable.Cols.ROAD_BUILD_COST + " INTEGER DEFAULT 20)");
    }

    private void createMapElementTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MapTable.NAME + "(" +
                MapTable.Cols.ROW + " INTEGER, " +
                MapTable.Cols.COL + " INTEGER, " +
                MapTable.Cols.BUILDABLE + " INTEGER, " +

                MapTable.Cols.NORTH_WEST + " INTEGER, " +
                MapTable.Cols.SOUTH_WEST + " INTEGER, " +
                MapTable.Cols.NORTH_EAST + " INTEGER, " +
                MapTable.Cols.SOUTH_EAST + " INTEGER, " +

                MapTable.Cols.STRUCTURE_DRAWABLE + " INTEGER, " +
                MapTable.Cols.STRUCTURE_LABEL + " TEXT, " +
                MapTable.Cols.STRUCTURE_TYPE + " INTEGER, " +
                MapTable.Cols.STRUCTURE_NAME + " TEXT)");
    }

    private void createGameTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GameTable.NAME + "(" +
                GameTable.Cols.ID + " INTEGER, " +
                GameTable.Cols.MONEY + " INTEGER, " +
                GameTable.Cols.TIME + " INTEGER, " +
                GameTable.Cols.POPULATION + " INTEGER, " +
                GameTable.Cols.RESIDENT_COUNT + " INTEGER, " +
                GameTable.Cols.COMMERCIAL_COUNT + " INTEGER, " +
                GameTable.Cols.EMPLOYMENT_RATE + " INTEGER)");
    }

    // ************************************ Settings ************************************ // */
    public static void add(SQLiteDatabase db, Settings newSettings) {
        ContentValues cv = addValues(newSettings);
        db.insert(SettingTable.NAME, null, cv);
    }

    public static void edit(SQLiteDatabase db, Settings updatedSettings) {
        ContentValues cv = addValues(updatedSettings);

        String[] whereValue = {String.valueOf(updatedSettings.getId())};
        db.update(SettingTable.NAME, cv, SettingTable.Cols.ID + " = ?", whereValue);
    }

    protected static ContentValues addValues(Settings setting) {
        ContentValues cv = new ContentValues();

        cv.put(SettingTable.Cols.ID, setting.getId());
        cv.put(SettingTable.Cols.MAP_WIDTH, setting.getMapWidth());
        cv.put(SettingTable.Cols.MAP_HEIGHT, setting.getMapHeight());
        cv.put(SettingTable.Cols.INITIAL_MONEY, setting.getInitialMoney());
        cv.put(SettingTable.Cols.FAMILY_SIZE, setting.getFamilySize());
        cv.put(SettingTable.Cols.SHOP_SIZE, setting.getShopSize());
        cv.put(SettingTable.Cols.SALARY, setting.getSalary());
        cv.put(SettingTable.Cols.TAX_RATE, setting.getTaxRate());
        cv.put(SettingTable.Cols.SERVICE_COST, setting.getServiceCost());
        cv.put(SettingTable.Cols.HOUSE_BUILD_COST, setting.getHouseBuildingCost());
        cv.put(SettingTable.Cols.COMM_BUILD_COST, setting.getCommBuildingCost());
        cv.put(SettingTable.Cols.ROAD_BUILD_COST, setting.getRoadBuildingCost());

        return cv;
    }

    // ************************************ Map Elements ************************************ // */
    public static void add(SQLiteDatabase db, MapElement newElement, int row, int col) {
        ContentValues cv = addValues(newElement, row, col);
        db.insert(MapTable.NAME, null, cv);
    }

    public static void edit(SQLiteDatabase db, MapElement updatedElement, int row, int col) {
        ContentValues cv = addValues(updatedElement, row, col);

        String[] whereValue = {String.valueOf(row), String.valueOf(col)};
        db.update(MapTable.NAME, cv, MapTable.Cols.ROW +
                " = ? and " + MapTable.Cols.COL + " = ?", whereValue);
    }

    protected static ContentValues addValues(MapElement mapElement, int row, int col) {
        ContentValues cv = new ContentValues();

        cv.put(MapTable.Cols.ROW, row);
        cv.put(MapTable.Cols.COL, col);
        cv.put(MapTable.Cols.BUILDABLE, mapElement.isBuildable());

        cv.put(MapTable.Cols.NORTH_WEST, mapElement.getNorthWest());
        cv.put(MapTable.Cols.SOUTH_WEST, mapElement.getSouthWest());
        cv.put(MapTable.Cols.NORTH_EAST, mapElement.getNorthEast());
        cv.put(MapTable.Cols.SOUTH_EAST, mapElement.getSouthEast());

        Structure structure = mapElement.getStructure();
        if (structure != null) {
            // If there is a structure
            cv.put(MapTable.Cols.STRUCTURE_DRAWABLE, structure.getDrawableId());
            cv.put(MapTable.Cols.STRUCTURE_LABEL, structure.getLabel());

            int type = (structure.getType()).getValue();
            cv.put(MapTable.Cols.STRUCTURE_TYPE, type);
            cv.put(MapTable.Cols.STRUCTURE_NAME, mapElement.getName());
        } else {
            // Marks the row to state there is no structure on this map element
            cv.put(MapTable.Cols.STRUCTURE_LABEL, NO_STRUCTURE);
        }

        return cv;
    }


    // ************************************ Game Data ************************************ // */
    public static void add(SQLiteDatabase db, GameData game) {
        ContentValues cv = addValues(game);
        db.insert(GameTable.NAME, null, cv);
    }

    public static void edit(SQLiteDatabase db, GameData updatedGame) {
        ContentValues cv = addValues(updatedGame);

        String[] whereValue = {String.valueOf(GameData.DB_ID)};
        db.update(GameTable.NAME, cv, GameTable.Cols.ID + " = ?", whereValue);
    }

    protected static ContentValues addValues(GameData game) {
        ContentValues cv = new ContentValues();

        cv.put(GameTable.Cols.ID, GameData.DB_ID);
        cv.put(GameTable.Cols.MONEY, game.getMoney());
        cv.put(GameTable.Cols.TIME, game.getGameTime());
        cv.put(GameTable.Cols.POPULATION, game.getPopulation());
        cv.put(GameTable.Cols.RESIDENT_COUNT, game.getResidentCount());
        cv.put(GameTable.Cols.COMMERCIAL_COUNT, game.getCommercialCount());
        cv.put(GameTable.Cols.EMPLOYMENT_RATE, game.getEmploymentRate());
        return cv;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {

    }
}
