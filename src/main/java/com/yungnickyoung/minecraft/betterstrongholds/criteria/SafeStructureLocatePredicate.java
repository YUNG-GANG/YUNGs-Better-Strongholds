package com.yungnickyoung.minecraft.betterstrongholds.criteria;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SafeStructureLocatePredicate {
    @Nullable
    private final Structure<?> feature;

    public SafeStructureLocatePredicate(@Nullable Structure<?> feature) {
        this.feature = feature;
    }

    public boolean test(ServerWorld world, double x, double y, double z) {
        return this.test(world, (float)x, (float)y, (float)z);
    }

    public boolean test(ServerWorld world, float x, float y, float z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        return this.feature != null && world.isBlockPresent(blockpos) && world.func_241112_a_().getStructureStart(blockpos, true, this.feature).isValid();
    }

    public JsonElement serialize() {
        JsonObject jsonobject = new JsonObject();
        if (this.feature != null) {
            jsonobject.addProperty("feature", this.feature.getStructureName());
        }
        return jsonobject;
    }

    public static SafeStructureLocatePredicate deserialize(@Nullable JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            JsonObject jsonobject = JSONUtils.getJsonObject(element, "location");
            Structure<?> structure = jsonobject.has("feature") ? ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(JSONUtils.getString(jsonobject, "feature"))) : null;

            return new SafeStructureLocatePredicate(structure);
        } else {
            return new SafeStructureLocatePredicate(null);
        }
    }
}
