package com.github.lunatrius.schematica.api;

import com.github.lunatrius.core.util.vector.Vector3f;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public interface ISchematicaWorldExtensions {
    int getBlockId(int x, int y, int z);

    Block getBlock(int x, int y, int z);

    boolean setBlock(int x, int y, int z, Block block, int metadata);

    TileEntity getTileEntity(int x, int y, int z);

    void setTileEntity(int x, int y, int z, TileEntity tileEntity);

    void removeTileEntity(int x, int y, int z);

    int getWidth();

    int getLength();

    int getHeight();

    boolean blockExists(int x, int y, int z);

    List<TileEntity> getTileEntities();

    boolean toggleRendering();

    Vector3f dimensions();

    World asWorld();
}
