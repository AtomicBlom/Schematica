package com.github.lunatrius.schematica.api;

import com.github.lunatrius.core.util.vector.Vector3f;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public interface ISchematic {
    Block getBlock(int x, int y, int z);
    boolean setBlock(int x, int y, int z, Block block, int metadata);

    TileEntity getTileEntity(int x, int y, int z);
    List<TileEntity> getTileEntities();
    void setTileEntity(int x, int y, int z, TileEntity tileEntity);
    void removeTileEntity(int x, int y, int z);

    int getBlockMetadata(int x, int y, int z);
    void setBlockMetadata(int x, int y, int z, int metadata);

    ItemStack getIcon();
    void setIcon(ItemStack icon);

    int getWidth();
    int getLength();
    int getHeight();
}
