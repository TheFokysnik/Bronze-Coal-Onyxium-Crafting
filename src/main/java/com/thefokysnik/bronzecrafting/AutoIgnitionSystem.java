package com.thefokysnik.bronzecrafting;

import com.hypixel.hytale.builtin.crafting.state.ProcessingBenchState;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nonnull;
/**
 * AutoIgnitionSystem for Processing Benches (Furnaces)
 * 
 * Automatically ignites furnaces when valid recipes are present,
 * eliminating the need for manual "Turn on" interaction.
 * 
 * @author TheFokysnik
 * @version 1.8
 */
public class AutoIgnitionSystem extends EntityTickingSystem<ChunkStore> {
    
    private final ComponentType<ChunkStore, ProcessingBenchState> benchComponentType;

    public AutoIgnitionSystem(ComponentType<ChunkStore, ProcessingBenchState> benchComponentType) {
        this.benchComponentType = benchComponentType;
    }

    @Override
    public void tick(float dt, int entityIndex, ArchetypeChunk<ChunkStore> archetypeChunk, 
                     @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBuffer) {
        
        ProcessingBenchState bench = archetypeChunk.getComponent(entityIndex, this.benchComponentType);
        
        // Skip if bench is null or already active
        if (bench == null || bench.isActive()) {
            return;
        }
        
        // If there's a valid recipe ready (fuel + input items), activate the bench
        if (bench.getRecipe() != null) {
            bench.setActive(true);
        }
    }

    @Override
    public Query<ChunkStore> getQuery() {
        return this.benchComponentType;
    }

    @Override
    public boolean isParallel(int archetypeChunkSize, int taskCount) {
        return true;
    }
}
