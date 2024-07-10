package org.teamvoided.world_foundry.mixin;

import net.minecraft.util.function.ToFloatFunction;
import net.minecraft.util.math.Spline;
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.world_foundry.data.gen.providers.MixinFunctions;

@Mixin(VanillaTerrainParametersCreator.class)
public abstract class VanillaTerrainParametersCreatorMixin {

    @Shadow
    public static <C, I extends ToFloatFunction<C>> Spline<C, I> method_42051(I locationFunction, I toFloatFunction, float f, float g, float h, float i, float j, float k, boolean bl, boolean bl2, ToFloatFunction<Float> toFloatFunction2) {
        return null;
    }

    @Shadow
    @Final
    private static ToFloatFunction<Float> field_38029;

    @Shadow
    @Final
    private static ToFloatFunction<Float> IDENTITY;

    @Inject(method = "method_42056", at = @At("RETURN"), cancellable = true)
    private static <C, I extends ToFloatFunction<C>> void offsetSplineWithDeeperOcean(
            I toFloatFunction,
            I toFloatFunction2,
            I toFloatFunction3,
            boolean bl,
            CallbackInfoReturnable<Spline<C, I>> cir) {
        ToFloatFunction<Float> toFloatFunction4 = bl ? field_38029 : IDENTITY;
        Spline<C, I> mushroomIslandSpline = MixinFunctions.oceanFloorSpline(toFloatFunction3, -0.075F, 0.04F, 0.048F, 0.088F, 0.1F, toFloatFunction4);
        Spline<C, I> deeperOceanSpline = MixinFunctions.oceanFloorSpline(toFloatFunction3, -0.066F, -0.56F, -0.52F, -0.48F, -0.44F, toFloatFunction4);
        Spline<C, I> deepOceanSpline = MixinFunctions.oceanFloorSpline(toFloatFunction3, -0.56F, -0.41F, -0.4F, -0.36F, -0.33F, toFloatFunction4);
        Spline<C, I> oceanSpline = MixinFunctions.oceanFloorSpline(toFloatFunction3, -0.45F, -0.31F, -0.3F, -0.27F, -0.22F, toFloatFunction4);
        Spline<C, I> shallowOceanSpline = MixinFunctions.oceanFloorSpline(toFloatFunction3, -0.33F, -0.23F, -0.2F, -0.155F, -0.11F, toFloatFunction4);
        Spline<C, I> shorelineSpline = method_42051(toFloatFunction2, toFloatFunction3, -0.15F, 0.0F, 0.0F, 0.1F, 0.0F, -0.03F, false, false, toFloatFunction4);
        Spline<C, I> outlandSpline = method_42051(toFloatFunction2, toFloatFunction3, -0.1F, 0.03F, 0.1F, 0.1F, 0.01F, -0.03F, false, false, toFloatFunction4);
        Spline<C, I> midlandSpline = method_42051(toFloatFunction2, toFloatFunction3, -0.1F, 0.03F, 0.1F, 0.7F, 0.01F, -0.03F, true, true, toFloatFunction4);
        Spline<C, I> inlandSpline = method_42051(toFloatFunction2, toFloatFunction3, -0.05F, 0.03F, 0.1F, 1.0F, 0.01F, 0.01F, true, true, toFloatFunction4);
        cir.setReturnValue(
                Spline.builder(toFloatFunction, toFloatFunction4)
                        .method_41295(-1.1F, mushroomIslandSpline)
                        .method_41295(-1.02F, deeperOceanSpline)
                        .method_41295(-0.51F, deepOceanSpline)
                        .method_41295(-0.44F, oceanSpline)
                        .method_41295(-0.18F, shallowOceanSpline)
                        .method_41295(-0.16F, shorelineSpline)
                        .method_41295(-0.15F, shorelineSpline)
                        .method_41295(-0.1F, outlandSpline)
                        .method_41295(0.25F, midlandSpline)
                        .method_41295(1.0F, inlandSpline)
                        .build()
        );

    }

}
