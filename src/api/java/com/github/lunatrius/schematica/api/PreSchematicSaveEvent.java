package com.github.lunatrius.schematica.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Map;

public class PreSchematicSaveEvent extends Event {
    private final Map<String, Short> mappings;
    private final NBTTagCompound extendedMetadata;

    public PreSchematicSaveEvent(Map<String, Short> mappings, NBTTagCompound extendedMetadata) {


        this.mappings = mappings;
        this.extendedMetadata = extendedMetadata;
    }

    public Map<String, Short> getMappings() {
        return mappings;
    }

    public NBTTagCompound getExtendedMetadata() {
        return extendedMetadata;
    }
}
