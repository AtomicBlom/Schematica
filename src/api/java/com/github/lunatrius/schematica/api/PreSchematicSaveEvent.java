package com.github.lunatrius.schematica.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Map;

public class PreSchematicSaveEvent extends Event {
    private final Map<String, Short> mappings;

    public PreSchematicSaveEvent(Map<String, Short> mappings) {


        this.mappings = mappings;
    }

    public Map<String, Short> getMappings() {
        return mappings;
    }
}
