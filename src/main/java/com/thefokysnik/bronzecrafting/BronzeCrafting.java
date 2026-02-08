package com.thefokysnik.bronzecrafting;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.ChunkPreLoadProcessEvent;
import com.hypixel.hytale.server.core.universe.world.meta.BlockStateModule;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * BronzeCrafting Plugin
 * 
 * Adds Tin ore, Tin bars, Bronze alloy crafting, Coal fuel, and Onyxium tier.
 * Features Java-based ore generation using ChunkPreLoadProcessEvent.
 * Includes auto-ignition for furnaces (no need to press "Turn on").
 * 
 * @author TheFokysnik
 * @version 1.9
 */
public class BronzeCrafting extends JavaPlugin {

    private OreWorldGen oreWorldGen;

    public BronzeCrafting(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        super.setup();
        getLogger().at(Level.INFO).log("BronzeCrafting plugin setting up...");
        
        // Initialize ore generation handler
        oreWorldGen = new OreWorldGen(getLogger());
        
        // Register chunk pre-load event for ore generation
        getEventRegistry().registerGlobal(
            ChunkPreLoadProcessEvent.class,
            oreWorldGen::onChunkPreLoad
        );
        
        getLogger().at(Level.INFO).log("Ore generation registered for ChunkPreLoadProcessEvent");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void start() {
        getLogger().at(Level.INFO).log("=== BronzeOnyxiumCrafting v2.1 Loaded ===");
        getLogger().at(Level.INFO).log("Added: Tin Ore, Coal Ore, Onyxium Ore");
        getLogger().at(Level.INFO).log("Coal: 5x more efficient fuel than Charcoal");
        
        // Register auto-ignition system for furnaces
        ComponentType<ChunkStore, ProcessingBenchState> benchType = 
            (ComponentType<ChunkStore, ProcessingBenchState>) BlockStateModule.get()
                .getComponentType(ProcessingBenchState.class);
        
        if (benchType != null) {
            getChunkStoreRegistry().registerSystem(new AutoIgnitionSystem(benchType));
            getLogger().at(Level.INFO).log("Auto-ignition enabled: Furnaces start automatically!");
        } else {
            getLogger().at(Level.WARNING).log("Could not find ProcessingBenchState - auto-ignition disabled");
        }
    }

    @Override
    protected void shutdown() {
        getLogger().at(Level.INFO).log("BronzeCrafting shutting down...");
    }
}
