package xray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XRayManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("XRayManager");
    private static XRayManager instance;
    private boolean isEnabled = false;

    private XRayManager() {}

    public static XRayManager getInstance() {
        if (instance == null) {
            instance = new XRayManager();
        }
        return instance;
    }

    public void toggle() {
        isEnabled = !isEnabled;
        if (isEnabled) {
            enable();
        } else {
            disable();
        }
    }

    private void enable() {
        // Logic to enable X-Ray mode
        LOGGER.info("X-Ray mode enabled");
    }

    private void disable() {
        // Logic to disable X-Ray mode
        LOGGER.info("X-Ray mode disabled");
    }
}
