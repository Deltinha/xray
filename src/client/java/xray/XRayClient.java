package xray;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class XRayClient implements ClientModInitializer {

    private static final KeyBinding xRayKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.xray.toggle",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category.xray")
    );

    private static final KeyBinding fullbrightKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.fullbright.toggle",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_C,
                    "category.xray")
    );


    @Override
	public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(
                client -> {
                    while (xRayKey.wasPressed()) {
                        toggleXRay();
                    }

                    while (fullbrightKey.wasPressed()) {
                        toggleFullBright();
                    }
                }
        );
    }

    private void toggleXRay() {
        XRayManager.getInstance().toggle();

        if (FullBrightManager.getInstance().isEnabled()) {
            return;
        }

        if (XRayManager.getInstance().isEnabled()) {
            FullBrightManager.getInstance().setMaxGamma();
        } else {
            FullBrightManager.getInstance().restoreOriginalGamma();
        }
    }

    private void toggleFullBright() {
        if (XRayManager.getInstance().isEnabled()) {
            return;
        }

        FullBrightManager.getInstance().toggle();
    }
}
