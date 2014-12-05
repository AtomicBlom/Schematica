package com.github.lunatrius.schematica.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class PreSchematicSaveEvent extends Event {
    private final World schematic;

    public PreSchematicSaveEvent(World schematic) {

        this.schematic = schematic;
    }

    public World getSchematic() {
        return schematic;
    }
}
