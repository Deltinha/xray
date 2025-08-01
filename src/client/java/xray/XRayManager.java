package xray;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class XRayManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("XRayManager");
    private static XRayManager instance;
    private boolean enabled = false;
    private final Set<Block> visibleBlocks = new HashSet<>();
    private static final Set<String> visibleBlockIds = Set.of(
        "minecraft:diamond_ore",
        "minecraft:deepslate_diamond_ore",
        "minecraft:emerald_ore",
        "minecraft:deepslate_emerald_ore",
        "minecraft:gold_ore",
        "minecraft:deepslate_gold_ore",
        "minecraft:iron_ore",
        "minecraft:deepslate_iron_ore",
        "minecraft:coal_ore",
        "minecraft:deepslate_coal_ore",
        "minecraft:copper_ore",
        "minecraft:deepslate_copper_ore",
        "minecraft:lapis_ore",
        "minecraft:deepslate_lapis_ore",
        "minecraft:redstone_ore",
        "minecraft:deepslate_redstone_ore",
        "minecraft:ancient_debris",
        "minecraft:nether_gold_ore",
        "minecraft:nether_quartz_ore",
        "minecraft:chest",
        "minecraft:ender_chest",
        "minecraft:spawner",
        "minecraft:beacon",
        "minecraft:end_portal_frame",
        "minecraft:glowstone",
        "minecraft:lava",
        "minecraft:water"
    );

    private XRayManager() {
        initializeDefaultBlocks();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisible(Block block, BlockPos pos) {
        if (!enabled) {
            return true;
        }

        boolean visible = visibleBlocks.contains(block);

        // TODO: Maybe review the interaction with exposed blocks; This would bypass anti-X-Ray plugins that check for exposed blocks.

        return visible;
    }

    private void initializeDefaultBlocks() {
        visibleBlockIds.forEach(this::addVisibleBlock);
    }

    public void addVisibleBlock(Block block) {
        visibleBlocks.add(block);
        if (enabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.worldRenderer != null) {
                client.worldRenderer.reload();
            }
        }
    }

    public void removeVisibleBlock(Block block) {
        visibleBlocks.remove(block);
        if (enabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.worldRenderer != null) {
                client.worldRenderer.reload();
            }
        }
    }

    public Set<Block> getVisibleBlocks() {
        return new HashSet<>(visibleBlocks);
    }

    private void addVisibleBlock(String blockId) {
        try {
            Block block = Registries.BLOCK.get(Identifier.of(blockId));
            visibleBlocks.add(block);
        } catch (Exception e) {
            LOGGER.warn("Could not add block to X-Ray list: {}", blockId);
        }
    }

    public static XRayManager getInstance() {
        if (instance == null) {
            instance = new XRayManager();
        }
        return instance;
    }

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    private void enable() {
        enabled = true;
        MinecraftClient client = MinecraftClient.getInstance();
        client.chunkCullingEnabled = false;
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
        LOGGER.info("X-Ray mode enabled");
    }

    private void disable() {
        enabled = false;
        MinecraftClient client = MinecraftClient.getInstance();
        client.chunkCullingEnabled = true;
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
        LOGGER.info("X-Ray mode disabled");
    }
}
