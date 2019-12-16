package au.edu.staylor.citybuilder;

public class Schema {
    public static class SettingSchema {
        public static class SettingTable {
            public static final String NAME = "settings"; // Table name
            public static class Cols {
                public static final String ID = "settingsId";
                public static final String MAP_WIDTH = "mapWidth";
                public static final String MAP_HEIGHT = "mapHeight";
                public static final String INITIAL_MONEY = "initialMoney";
                public static final String FAMILY_SIZE = "familySize";
                public static final String SHOP_SIZE = "shopSize";
                public static final String SALARY = "salary";
                public static final String TAX_RATE = "taxRate";
                public static final String SERVICE_COST = "serviceCost";
                public static final String HOUSE_BUILD_COST = "houseBuildingCost";
                public static final String COMM_BUILD_COST = "commBuildingCost";
                public static final String ROAD_BUILD_COST = "roadBuildingCost";
            }
        }
    }

    public static class MapElementSchema {
        public static class MapTable {
            public static final String NAME = "mapElements"; // Table name
            public static class Cols {
                public static final String ROW = "idRow";
                public static final String COL = "idCol";
                public static final String BUILDABLE = "buildable";

                public static final String NORTH_WEST = "northWest";
                public static final String SOUTH_WEST = "southWest";
                public static final String NORTH_EAST = "northEast";
                public static final String SOUTH_EAST = "southEast";

                // STRUCTURE
                public static final String STRUCTURE_DRAWABLE = "structureDrawable";
                public static final String STRUCTURE_LABEL = "structureLabel";
                public static final String STRUCTURE_TYPE = "structureType";
                public static final String STRUCTURE_NAME = "structureName";
            }
        }
    }

    public static class GameSchema {
        public static class GameTable {
            public static final String NAME = "gameData"; // Table name
            public static class Cols {
                public static final String ID = "gameId";
                public static final String MONEY = "money";
                public static final String TIME = "gameTime";
                public static final String POPULATION = "population";
                public static final String RESIDENT_COUNT = "nResidential";
                public static final String COMMERCIAL_COUNT = "nCommercial";
                public static final String EMPLOYMENT_RATE = "employmentRate";
            }
        }
    }
}

