package com.github.lunatrius.schematica.api;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class PostSchematicCaptureEvent extends Event {
    private final ISchematicaWorldExtensions schematic;

    public PostSchematicCaptureEvent(ISchematicaWorldExtensions schematic) {
        this.schematic = schematic;
    }

    public ISchematicaWorldExtensions getSchematic() {
        return schematic;
    }
}
