package com.yungnickyoung.minecraft.betterstrongholds.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;

public class BannerFactory {
    public static CompoundNBT createBannerNBT(List<Pattern> patterns) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT patternList = new ListNBT();

        // Construct list of patterns from args
        for (Pattern pattern : patterns) {
            CompoundNBT patternNBT = new CompoundNBT();
            patternNBT.putString("Pattern", pattern.getPattern());
            patternNBT.putInt("Color", pattern.getColor());
            patternList.add(patternNBT);
        }

        // Add tags to NBT
        nbt.put("Patterns", patternList);
        nbt.putString("id", "minecraft:banner");

        return nbt;
    }

    public static class Banner {
        private List<Pattern> patterns;
        private BlockState state;
        private CompoundNBT nbt;

        private Banner(List<Pattern> _patterns, BlockState _state, CompoundNBT _nbt) {
            this.patterns = _patterns;
            this.state = _state;
            this.nbt = _nbt;
        }

        public List<Pattern> getPatterns() {
            return patterns;
        }

        public void setPatterns(List<Pattern> patterns) {
            this.patterns = patterns;
        }

        public BlockState getState() {
            return state;
        }

        public void setState(BlockState state) {
            this.state = state;
        }

        public CompoundNBT getNbt() {
            return nbt;
        }

        public void setNbt(CompoundNBT nbt) {
            this.nbt = nbt;
        }

        public static class Builder {
            private final List<Pattern> patterns = new ArrayList<>();
            private BlockState state = Blocks.BLACK_WALL_BANNER.getDefaultState();

            public Builder() {}

            public Builder blockState(BlockState state) {
                this.state = state;
                return this;
            }

            public Builder pattern(Pattern pattern) {
                patterns.add(pattern);
                return this;
            }

            public Builder pattern(String pattern, int color) {
                patterns.add(new Pattern(pattern, color));
                return this;
            }

            public Banner build() {
                CompoundNBT nbt = createBannerNBT(patterns);
                return new Banner(patterns, state, nbt);
            }
        }
    }

    public static class Pattern {
        private String pattern;
        private int color;

        public Pattern(String _pattern, int _color) {
            this.pattern = _pattern;
            this.color = _color;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
