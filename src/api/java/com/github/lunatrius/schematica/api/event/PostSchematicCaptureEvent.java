package com.github.lunatrius.schematica.api.event;

import com.github.lunatrius.schematica.api.ISchematic;
import cpw.mods.fml.common.eventhandler.Event;

public class PostSchematicCaptureEvent extends Event {
    private final ISchematic schematic;

    public PostSchematicCaptureEvent(ISchematic schematic) {
        this.schematic = schematic;
    }

    public ISchematic getSchematic() {
        return schematic;
    }
}
