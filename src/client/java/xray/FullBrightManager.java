package xray;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import xray.mixin.client.SimpleOptionAccessor;

@SuppressWarnings("unchecked")
public class FullBrightManager {
    private static FullBrightManager instance;
    private static final double MAX_GAMMA = 16.0;
    private static Double originalGamma;
    private boolean enabled = false;

    private FullBrightManager() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static FullBrightManager getInstance() {
        if (instance == null) {
            instance = new FullBrightManager();
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

    public void enable() {
        enabled = true;
        setMaxGamma();
    }

    public void setMaxGamma() {
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions options = client.options;
        if (options == null) {
            return;
        }
        SimpleOptionAccessor<Double> gammaOption = (SimpleOptionAccessor<Double>) (Object) options.getGamma();

        if (gammaOption != null && gammaOption.getValue() < MAX_GAMMA) {
            originalGamma = gammaOption.getValue();
            gammaOption.setValue(MAX_GAMMA);
        }
    }

    public void disable() {
        enabled = false;
        restoreOriginalGamma();
    }

    public void restoreOriginalGamma() {
        if (originalGamma != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            GameOptions options = client.options;
            if (options == null) {
                return;
            }
            SimpleOptionAccessor<Double> gammaOption = (SimpleOptionAccessor<Double>) (Object) options.getGamma();

            if (gammaOption != null) {
                gammaOption.setValue(originalGamma);
                originalGamma = null;
            }
        }
    }
}
