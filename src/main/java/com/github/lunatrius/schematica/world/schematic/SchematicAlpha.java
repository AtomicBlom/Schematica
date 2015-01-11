package com.github.lunatrius.schematica.world.schematic;

import com.github.lunatrius.schematica.api.NBTNames;
import com.github.lunatrius.schematica.reference.Reference;
import com.github.lunatrius.schematica.world.SchematicWorld;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SchematicAlpha extends SchematicFormat {
    @Override
    public SchematicWorld readFromNBT(NBTTagCompound tagCompound) {
        ItemStack icon = SchematicUtil.getIconFromNBT(tagCompound);

        byte localBlocks[] = tagCompound.getByteArray(NBTNames.BLOCKS);
        byte localMetadata[] = tagCompound.getByteArray(NBTNames.DATA);

        boolean extra = false;
        byte extraBlocks[] = null;
        byte extraBlocksNibble[] = null;
        if (tagCompound.hasKey(NBTNames.ADD_BLOCKS)) {
            extra = true;
            extraBlocksNibble = tagCompound.getByteArray(NBTNames.ADD_BLOCKS);
            extraBlocks = new byte[extraBlocksNibble.length * 2];
            for (int i = 0; i < extraBlocksNibble.length; i++) {
                extraBlocks[i * 2 + 0] = (byte) ((extraBlocksNibble[i] >> 4) & 0xF);
                extraBlocks[i * 2 + 1] = (byte) (extraBlocksNibble[i] & 0xF);
            }
        } else if (tagCompound.hasKey(NBTNames.ADD_BLOCKS_SCHEMATICA)) {
            extra = true;
            extraBlocks = tagCompound.getByteArray(NBTNames.ADD_BLOCKS_SCHEMATICA);
        }

        short width = tagCompound.getShort(NBTNames.WIDTH);
        short length = tagCompound.getShort(NBTNames.LENGTH);
        short height = tagCompound.getShort(NBTNames.HEIGHT);

        short[][][] blocks = new short[width][height][length];
        byte[][][] metadata = new byte[width][height][length];

        Short id = null;
        Map<Short, Short> oldToNew = new HashMap<Short, Short>();
        if (tagCompound.hasKey(NBTNames.MAPPING_SCHEMATICA)) {
            NBTTagCompound mapping = tagCompound.getCompoundTag(NBTNames.MAPPING_SCHEMATICA);
            Set<String> names = mapping.func_150296_c();
            for (String name : names) {
                oldToNew.put(mapping.getShort(name), (short) GameData.getBlockRegistry().getId(name));
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int index = x + (y * length + z) * width;
                    blocks[x][y][z] = (short) ((localBlocks[index] & 0xFF) | (extra ? ((extraBlocks[index] & 0xFF) << 8) : 0));
                    metadata[x][y][z] = (byte) (localMetadata[index] & 0xFF);
                    if ((id = oldToNew.get(blocks[x][y][z])) != null) {
                        blocks[x][y][z] = id;
                    }
                }
            }
        }

        List<TileEntity> tileEntities = new ArrayList<TileEntity>();
        NBTTagList tileEntitiesList = tagCompound.getTagList(NBTNames.TILE_ENTITIES, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tileEntitiesList.tagCount(); i++) {
            try {
                TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntitiesList.getCompoundTagAt(i));
                if (tileEntity != null) {
                    tileEntities.add(tileEntity);
                }
            } catch (Exception e) {
                Reference.logger.error("TileEntity failed to load properly!", e);
            }
        }

        return new SchematicWorld(icon, blocks, metadata, tileEntities, width, height, length);
    }

    @Override
    public boolean writeToNBT(NBTTagCompound tagCompound, SchematicWorld world) {
        NBTTagCompound tagCompoundIcon = new NBTTagCompound();
        ItemStack icon = world.getIcon();
        icon.writeToNBT(tagCompoundIcon);
        tagCompound.setTag(NBTNames.ICON, tagCompoundIcon);

        tagCompound.setShort(NBTNames.WIDTH, (short) world.getWidth());
        tagCompound.setShort(NBTNames.LENGTH, (short) world.getLength());
        tagCompound.setShort(NBTNames.HEIGHT, (short) world.getHeight());

        int size = world.getWidth() * world.getLength() * world.getHeight();
        byte localBlocks[] = new byte[size];
        byte localMetadata[] = new byte[size];
        byte extraBlocks[] = new byte[size];
        byte extraBlocksNibble[] = new byte[(int) Math.ceil(size / 2.0)];
        boolean extra = false;
        NBTTagCompound mapping = new NBTTagCompound();

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                for (int z = 0; z < world.getLength(); z++) {
                    int index = x + (y * world.getLength() + z) * world.getWidth();
                    int blockId = world.getBlockIdRaw(x, y, z);
                    localBlocks[index] = (byte) blockId;
                    localMetadata[index] = (byte) world.getBlockMetadata(x, y, z);
                    if ((extraBlocks[index] = (byte) (blockId >> 8)) > 0) {
                        extra = true;
                    }

                    String name = GameData.getBlockRegistry().getNameForObject(world.getBlockRaw(x, y, z));
                    if (!mapping.hasKey(name)) {
                        mapping.setShort(name, (short) blockId);
                    }
                }
            }
        }

        for (int i = 0; i < extraBlocksNibble.length; i++) {
            if (i * 2 + 1 < extraBlocks.length) {
                extraBlocksNibble[i] = (byte) ((extraBlocks[i * 2 + 0] << 4) | extraBlocks[i * 2 + 1]);
            } else {
                extraBlocksNibble[i] = (byte) (extraBlocks[i * 2 + 0] << 4);
            }
        }

        int count = 20;
        NBTTagList tileEntitiesList = new NBTTagList();
        for (TileEntity tileEntity : world.getTileEntities()) {
            NBTTagCompound tileEntityTagCompound = new NBTTagCompound();
            try {
                tileEntity.writeToNBT(tileEntityTagCompound);
                tileEntitiesList.appendTag(tileEntityTagCompound);
            } catch (Exception e) {
                int pos = tileEntity.xCoord + (tileEntity.yCoord * world.getLength() + tileEntity.zCoord) * world.getWidth();
                if (--count > 0) {
                    Block block = world.getBlockRaw(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                    Reference.logger.error(String.format("Block %s[%s] with TileEntity %s failed to save! Replacing with bedrock...", block, block != null ? GameData.getBlockRegistry().getNameForObject(block) : "?", tileEntity.getClass().getName()), e);
                }
                localBlocks[pos] = (byte) GameData.getBlockRegistry().getId(Blocks.bedrock);
                localMetadata[pos] = 0;
                extraBlocks[pos] = 0;
            }
        }

        tagCompound.setString(NBTNames.MATERIALS, NBTNames.FORMAT_ALPHA);
        tagCompound.setByteArray(NBTNames.BLOCKS, localBlocks);
        tagCompound.setByteArray(NBTNames.DATA, localMetadata);
        if (extra) {
            tagCompound.setByteArray(NBTNames.ADD_BLOCKS, extraBlocksNibble);
        }
        tagCompound.setTag(NBTNames.ENTITIES, new NBTTagList());
        tagCompound.setTag(NBTNames.TILE_ENTITIES, tileEntitiesList);
        tagCompound.setTag(NBTNames.MAPPING_SCHEMATICA, mapping);

        return true;
    }
}
