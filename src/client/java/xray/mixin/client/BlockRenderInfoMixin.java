package xray.mixin.client;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xray.XRayManager;

@Mixin(value = BlockRenderInfo.class, remap = false)
public abstract class BlockRenderInfoMixin {
    @Shadow
    public BlockPos blockPos;
    @Shadow
    public BlockState blockState;

    /**
     * Changes the behavior of Indigo's block rendering
     */
    @Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
    private void onShouldDrawSide(Direction face, CallbackInfoReturnable<Boolean> cir) {
        XRayManager xray = XRayManager.getInstance();
        Boolean shouldDraw = xray.isVisible(blockState.getBlock(), blockPos);
        cir.setReturnValue(shouldDraw);
    }
}
