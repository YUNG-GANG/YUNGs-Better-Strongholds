package com.yungnickyoung.minecraft.betterstrongholds.module;

public class ConfigModule {
    public General general = new General();

    public static class General {
        public double cobwebReplacementChanceNormal = 0.1;
        public double cobwebReplacementChanceSpawner = 0.3;
        public double torchSpawnRate = 0.1;
        public double lanternSpawnRate = 0.2;
    }
}
