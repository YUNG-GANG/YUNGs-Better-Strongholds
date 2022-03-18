package com.yungnickyoung.minecraft.betterstrongholds.module;

public class ConfigModule {
    public General general = new General();
    public PieceSettings pieceSettings = new PieceSettings();

    public static class General {
        public int strongholdStartMinY = -30;
        public int strongholdStartMaxY = 11;
        public int strongholdMaxY = 60;
        public double cobwebReplacementChanceNormal = 0.1;
        public double cobwebReplacementChanceSpawner = 0.3;
        public double torchSpawnRate = 0.1;
        public double lanternSpawnRate = 0.2;
    }

    public static class PieceSettings {
        public int grandLibraryMaxCount = 1;
        public int smallLibraryMaxCount = 2;
        public int prisonMaxCount = 2;
        public int cmdAcariiMaxCount = 1;
        public int cmdYungMaxCount = 1;
        public int treasureRoomMaxCount = 2;
        public int armouryLargeRoomMaxCount = 2;
        public int armourySmallRoomMaxCount = 2;
        public int portalRoomMaxCount = 1;
    }
}
