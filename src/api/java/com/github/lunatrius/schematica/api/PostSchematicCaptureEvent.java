package com.github.lunatrius.schematica.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class PostSchematicCaptureEvent extends Event {
    private final World schematic;

    public PostSchematicCaptureEvent(World schematic) {

        this.schematic = schematic;
    }

    public World getSchematic() {
        return schematic;
    }
}
