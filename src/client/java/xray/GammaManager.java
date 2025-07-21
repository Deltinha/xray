package xray;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import xray.mixin.client.SimpleOptionAccessor;

@SuppressWarnings("unchecked")
public class GammaManager {
    private static GammaManager instance;
    private static final double MAX_GAMMA = 16.0;
    private static Double originalGamma;

    private GammaManager() {
    }

    public static GammaManager getInstance() {
        if (instance == null) {
            instance = new GammaManager();
        }
        return instance;
    }

    public void setMaxGamma() {
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions options = client.options;
        if (options == null) {
            return;
        }
        SimpleOptionAccessor<Double> gammaOption = (SimpleOptionAccessor<Double>) (Object) options.getGamma();

        if (gammaOption != null) {
            originalGamma = gammaOption.getValue();
            gammaOption.setValue(MAX_GAMMA);
        }
    }

    public void resetGamma() {
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions options = client.options;
        if (options == null || originalGamma == null) {
            return;
        }
        SimpleOptionAccessor<Double> gammaOption = (SimpleOptionAccessor<Double>) (Object) options.getGamma();

        if (gammaOption != null) {
            gammaOption.setValue(originalGamma);
            originalGamma = null;
        }
    }
}
