package com.github.lunatrius.schematica.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class PreSchematicSaveEvent extends Event {
    private final Map<String, Short> mappings;
    private final NBTTagCompound extendedMetadata;

    public PreSchematicSaveEvent(Map<String, Short> mappings) {


        this.mappings = mappings;
        this.extendedMetadata = new NBTTagCompound();
    }

    public Map<String, Short> getMappings() {
        return mappings;
    }

    public NBTTagCompound getExtendedMetadata() {
        return extendedMetadata ;
    }
}
