/*******************************************************************************
 * Copyright 2014 stuntguy3000 (Luke Anderson) and coasterman10.
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 ******************************************************************************/
package net.PlayFriik.Annihilation.maps;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {
    @Override
    public Location getFixedSpawnLocation(World world, Random rand) {
    return new Location(world, 0, world.getHighestBlockYAt(0, 0), 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[] generate(World world, Random rand, int chunkX, int chunkZ) {
    byte[] chunk = new byte[16 * 16 * 128];

    if ((chunkX == 0) && (chunkZ == 0)) {
        chunk[0] = (byte) Material.BEDROCK.getId();
    }

    for (int x = 0; x < 16; x++)
        for (int z = 0; z < 16; z++)
        world.setBiome(x, z, Biome.PLAINS);
    
    return chunk;
    }
}
