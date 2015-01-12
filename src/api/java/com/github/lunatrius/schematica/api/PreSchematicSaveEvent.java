package com.github.lunatrius.schematica.api;

import com.sun.javaws.exceptions.InvalidArgumentException;
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

    public boolean replaceMapping(String oldName, String newName) throws DuplicateMappingException {
        if (this.mappings.containsKey(newName)) {
            throw new DuplicateMappingException("You the block type %s already exists in the schematic. If this is intended, you should replace the blocks in the PostSchematicCaptureEvent before attempting a mapping change.");
        }

        Short id = this.mappings.get(oldName);
        if (id != null) {
            this.mappings.remove(oldName);
            this.mappings.put(newName, id);
            return true;
        }

        return false;
    }

    public NBTTagCompound getExtendedMetadata() {
        return extendedMetadata ;
    }
}
