package au.edu.staylor.citybuilder;

import android.database.Cursor;
import android.database.CursorWrapper;

import au.edu.staylor.citybuilder.Schema.SettingSchema.SettingTable;

public class SettingCursor extends CursorWrapper {
    public SettingCursor(Cursor cursor) {
        super(cursor);
    }

    public Settings getSetting() {
        int id = getInt(getColumnIndex(SettingTable.Cols.ID));
        int mapWidth = getInt(getColumnIndex(SettingTable.Cols.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(SettingTable.Cols.MAP_HEIGHT));
        int initialMoney = getInt(getColumnIndex(SettingTable.Cols.INITIAL_MONEY));
        int familySize = getInt(getColumnIndex(SettingTable.Cols.FAMILY_SIZE));
        int shopSize = getInt(getColumnIndex(SettingTable.Cols.SHOP_SIZE));
        int salary = getInt(getColumnIndex(SettingTable.Cols.SALARY));
        double taxRate = getDouble(getColumnIndex(SettingTable.Cols.TAX_RATE));
        int serviceCost = getInt(getColumnIndex(SettingTable.Cols.SERVICE_COST));
        int houseBuildingCost = getInt(getColumnIndex(SettingTable.Cols.HOUSE_BUILD_COST));
        int commBuildingCost = getInt(getColumnIndex(SettingTable.Cols.COMM_BUILD_COST));
        int roadBuildingCost = getInt(getColumnIndex(SettingTable.Cols.ROAD_BUILD_COST));

        return new Settings(id, mapWidth, mapHeight, initialMoney, familySize, shopSize, salary,
                taxRate, serviceCost, houseBuildingCost, commBuildingCost, roadBuildingCost);
    }
}
