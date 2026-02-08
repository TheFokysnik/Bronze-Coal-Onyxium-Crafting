package com.thefokysnik.bronzecrafting;

import com.hypixel.hytale.assetstore.map.BlockTypeAssetMap;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.events.ChunkPreLoadProcessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

/**
 * Ore generation handler for Tin, Onyxium, and Coal ores.
 * 
 * Uses ChunkPreLoadProcessEvent to inject custom ores into newly generated chunks.
 * Supports multiple rock types: Stone, Basalt, Sandstone, Shale, Volcanic, Slate.
 * Each rock type gets the appropriate ore variant (e.g. Ore_Tin_Basalt in Basalt).
 * 
 * Tin generates at same heights as Copper (surface to Y=100) and is very common.
 * Onyxium generates underground (Y=5-45), rare like diamond.
 * Coal generates like Tin (Y=20-100), common.
 * 
 * @author TheFokysnik
 * @version 1.9
 */
public class OreWorldGen {

    private final HytaleLogger logger;
    private final Random random;
    
    // Rock type names that can contain ores
    private static final String[] ROCK_TYPES = {
        "Stone", "Basalt", "Sandstone", "Shale", "Volcanic", "Slate"
    };
    
    // Maps: Rock block ID -> Tin ore ID for that rock type
    private Map<Integer, Integer> rockToTinOre = new HashMap<>();
    // Maps: Rock block ID -> Onyxium ore ID for that rock type
    private Map<Integer, Integer> rockToOnyxiumOre = new HashMap<>();
    // Maps: Rock block ID -> Coal ore ID for that rock type
    private Map<Integer, Integer> rockToCoalOre = new HashMap<>();
    
    private boolean initialized = false;
    
    // Tin generation parameters - same as copper, more common than iron
    private static final int TIN_MIN_Y = 20;      // Start from Y20 (surface caves)
    private static final int TIN_MAX_Y = 100;     // Up to Y100 (higher than copper)
    private static final int TIN_VEINS_PER_CHUNK = 18;  // Very common (more than copper)
    private static final int TIN_VEIN_SIZE = 9;   // Slightly larger veins
    
    // Onyxium generation parameters - rare, underground (above lava level)
    private static final int ONYXIUM_MIN_Y = 5;   // Deep
    private static final int ONYXIUM_MAX_Y = 55;  // Up to Y55 (above lava)
    private static final int ONYXIUM_VEINS_PER_CHUNK = 2;  // Rare (diamond-like)
    private static final int ONYXIUM_VEIN_SIZE = 4;  // Small veins
    
    // Coal generation parameters - common like tin
    private static final int COAL_MIN_Y = 20;     // Start from Y20
    private static final int COAL_MAX_Y = 100;    // Up to Y100
    private static final int COAL_VEINS_PER_CHUNK = 16;  // Common
    private static final int COAL_VEIN_SIZE = 10; // Larger veins

    public OreWorldGen(HytaleLogger logger) {
        this.logger = logger;
        this.random = new Random();
    }

    /**
     * Initialize block IDs from asset store for all rock types.
     * Maps each rock type to its corresponding ore variant.
     */
    private boolean initBlockIds() {
        if (initialized) {
            return !rockToTinOre.isEmpty() || !rockToOnyxiumOre.isEmpty();
        }
        initialized = true;
        
        try {
            BlockTypeAssetMap<String, BlockType> assetMap = BlockType.getAssetMap();
            if (assetMap == null) {
                logger.at(Level.WARNING).log("BlockType AssetMap is null, cannot initialize ore generation");
                return false;
            }
            
            logger.at(Level.INFO).log("Initializing ore generation for all rock types...");
            
            // For each rock type, find the rock block and corresponding ores
            for (String rockType : ROCK_TYPES) {
                String rockBlockName = "Rock_" + rockType;
                int rockId = assetMap.getIndex(rockBlockName);
                
                if (rockId <= 0) {
                    logger.at(Level.INFO).log("Rock type not found: %s (id=%d)", rockBlockName, rockId);
                    continue;
                }
                
                // Find Tin ore for this rock type
                String tinOreName = "Ore_Tin_" + rockType;
                int tinOreId = assetMap.getIndex(tinOreName);
                if (tinOreId > 0) {
                    rockToTinOre.put(rockId, tinOreId);
                    logger.at(Level.INFO).log("Mapped %s (id=%d) -> %s (id=%d)", 
                               rockBlockName, rockId, tinOreName, tinOreId);
                }
                
                // Find Onyxium ore for this rock type
                String onyxiumOreName = "Ore_Onyxium_" + rockType;
                int onyxiumOreId = assetMap.getIndex(onyxiumOreName);
                if (onyxiumOreId > 0) {
                    rockToOnyxiumOre.put(rockId, onyxiumOreId);
                    logger.at(Level.INFO).log("Mapped %s (id=%d) -> %s (id=%d)", 
                               rockBlockName, rockId, onyxiumOreName, onyxiumOreId);
                }
                
                // Find Coal ore for this rock type
                String coalOreName = "Ore_Coal_" + rockType;
                int coalOreId = assetMap.getIndex(coalOreName);
                if (coalOreId > 0) {
                    rockToCoalOre.put(rockId, coalOreId);
                    logger.at(Level.INFO).log("Mapped %s (id=%d) -> %s (id=%d)", 
                               rockBlockName, rockId, coalOreName, coalOreId);
                }
            }
            
            logger.at(Level.INFO).log("OreWorldGen initialized: %d Tin, %d Onyxium, %d Coal ore mappings", 
                       rockToTinOre.size(), rockToOnyxiumOre.size(), rockToCoalOre.size());
            
            return !rockToTinOre.isEmpty() || !rockToOnyxiumOre.isEmpty();
        } catch (Exception e) {
            logger.at(Level.WARNING).log("Failed to initialize block IDs: %s", e.getMessage());
            return false;
        }
    }

    /**
     * Handle chunk pre-load event for ore generation.
     */
    public void onChunkPreLoad(ChunkPreLoadProcessEvent event) {
        // Only process newly generated chunks
        if (!event.isNewlyGenerated()) {
            return;
        }
        
        // Initialize block IDs if needed
        if (!initBlockIds()) {
            return;
        }
        
        try {
            WorldChunk worldChunk = event.getChunk();
            if (worldChunk == null) {
                return;
            }
            
            BlockChunk blockChunk = worldChunk.getBlockChunk();
            if (blockChunk == null) {
                return;
            }
            
            int chunkX = blockChunk.getX();
            int chunkZ = blockChunk.getZ();
            
            // Seed random with chunk coordinates for consistent generation
            random.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
            
            // Generate Tin ore veins (Y20-100, common like copper)
            if (!rockToTinOre.isEmpty()) {
                generateOreVeins(blockChunk, rockToTinOre, TIN_MIN_Y, TIN_MAX_Y, 
                                TIN_VEINS_PER_CHUNK, TIN_VEIN_SIZE);
            }
            
            // Generate Onyxium ore veins (Y5-45, rare like diamond)
            if (!rockToOnyxiumOre.isEmpty()) {
                generateOreVeins(blockChunk, rockToOnyxiumOre, ONYXIUM_MIN_Y, ONYXIUM_MAX_Y,
                                ONYXIUM_VEINS_PER_CHUNK, ONYXIUM_VEIN_SIZE);
            }
            
            // Generate Coal ore veins (Y20-100, common)
            if (!rockToCoalOre.isEmpty()) {
                generateOreVeins(blockChunk, rockToCoalOre, COAL_MIN_Y, COAL_MAX_Y,
                                COAL_VEINS_PER_CHUNK, COAL_VEIN_SIZE);
            }
            
        } catch (Exception e) {
            logger.at(Level.WARNING).log("Error during ore generation: %s", e.getMessage());
        }
    }

    /**
     * Generate ore veins in a chunk, using the rock-to-ore mapping.
     */
    private void generateOreVeins(BlockChunk chunk, Map<Integer, Integer> rockToOre, 
                                  int minY, int maxY, int veinsPerChunk, int veinSize) {
        for (int i = 0; i < veinsPerChunk; i++) {
            int x = random.nextInt(16);
            int y = minY + random.nextInt(Math.max(1, maxY - minY));
            int z = random.nextInt(16);
            
            generateSingleVein(chunk, rockToOre, x, y, z, veinSize);
        }
    }

    /**
     * Generate a single ore vein at the specified position.
     * Replaces rock blocks with the appropriate ore variant.
     */
    private void generateSingleVein(BlockChunk chunk, Map<Integer, Integer> rockToOre, 
                                    int startX, int startY, int startZ, int size) {
        for (int i = 0; i < size; i++) {
            int x = startX + random.nextInt(3) - 1;
            int y = startY + random.nextInt(3) - 1;
            int z = startZ + random.nextInt(3) - 1;
            
            // Bounds check
            if (x < 0 || x >= 16 || z < 0 || z >= 16 || y < 0 || y >= 256) {
                continue;
            }
            
            // Get current block and check if it's a replaceable rock type
            int currentBlock = chunk.getBlock(x, y, z);
            Integer oreId = rockToOre.get(currentBlock);
            
            if (oreId != null) {
                // Replace rock with the matching ore variant
                chunk.setBlock(x, y, z, oreId, 0, 0);
            }
            
            // Move starting position slightly for next block in vein
            startX += random.nextInt(3) - 1;
            startY += random.nextInt(3) - 1;
            startZ += random.nextInt(3) - 1;
        }
    }
}
