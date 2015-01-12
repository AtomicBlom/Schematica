package com.github.lunatrius.schematica.api.event;

import com.github.lunatrius.schematica.api.DuplicateMappingException;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Map;

/**
 * This event is fired after the schematic has been Captured, but before it is serialized to the schematic format.
 * This is your opportunity to add Metadata.
 * Register to this event using MinecraftForge.EVENT_BUS
 */
public class PreSchematicSaveEvent extends Event {
    private final Map<String, Short> mappings;
    private final NBTTagCompound extendedMetadata;

    public PreSchematicSaveEvent(Map<String, Short> mappings) {
        this.mappings = mappings;
        this.extendedMetadata = new NBTTagCompound();
    }

    /**
     * Replaces the block mapping from one name to another. Use this method with care as it is possible that the schematic
     * will not be usable or will have blocks missing if you use an invalid value.
     *
     * Attempting to remap two blocks to the same name will result in a DuplicateMappingException. If you wish for this
     * type of collision, you can work around it by merging the two sets of block into a single BlockType in the
     * PostSchematicCaptureEvent.
     * @param oldName The old name of the block mapping.
     * @param newName The new name of the block Mapping.
     * @return true if a mapping was replaced.
     * @throws DuplicateMappingException
     */
    public boolean replaceMapping(String oldName, String newName) throws DuplicateMappingException {
        if (this.mappings.containsKey(newName)) {
            throw new DuplicateMappingException(
                    String.format(
                            "While replacing block type %s, the block type %s already exists in the schematic. " +
                                    "If this is intended, you should replace the blocks in the PostSchematicCaptureEvent " +
                                    "before attempting a mapping change.",
                            oldName, newName
                    )
            );
        }

        Short id = this.mappings.get(oldName);
        if (id != null) {
            this.mappings.remove(oldName);
            this.mappings.put(newName, id);
            return true;
        }

        return false;
    }

    /**
     * Gets the Extended Metadata tag compound. It is possible to add custom metadata to the schematic via this methods.
     * @return
     */
    public NBTTagCompound getExtendedMetadata() {
        return extendedMetadata ;
    }
}
