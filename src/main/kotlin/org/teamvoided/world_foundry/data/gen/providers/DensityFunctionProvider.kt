package org.teamvoided.world_foundry.data.gen.providers

import net.minecraft.registry.*
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.DensityFunctions.HolderHolder
import net.minecraft.world.gen.OreVeinCreator.VeinType
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.noise.NoiseRouter
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.AMPLIFIED_REGION
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.FINAL_DENSITY_MIX
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.CONTINENTS_WF
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.DEPTH_MIX
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.EROSION_WF
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.FACTOR_MIX
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.INITIAL_DENSITY_WITHOUT_JAGGEDNESS
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.I_D_W_J_AMPLIFIED
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.I_D_W_J_NORMAL
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.JAGGEDNESS_MIX
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.NORMAL_REGION
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.OFFSET_MIX
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.SLOPED_CHEESE_MIX
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.TEMPERATURE_WF
import org.teamvoided.world_foundry.data.world.WFDensityFunctions.VEGETATION_WF
import org.teamvoided.world_foundry.data.world.WFNoise.AMPLIFIED_TRANSITION_NOISE
import org.teamvoided.world_foundry.data.world.WFNoise.CONTINENTS_NOISE_WF
import org.teamvoided.world_foundry.data.world.WFNoise.EROSION_NOISE_WF
import org.teamvoided.world_foundry.data.world.WFNoise.TEMPERATURE_NOISE_WF
import org.teamvoided.world_foundry.data.world.WFNoise.VEGETATION_NOISE_WF
import java.util.stream.Stream

object DensityFunctionProvider {
    fun bootstrap(c: BootstrapContext<DensityFunction>) {
        val noiseParams = c.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS)
        val densityFuns = c.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION)
        val shiftX = getFunction(densityFuns, NoiseRouterData.SHIFT_X)
        val shiftZ = getFunction(densityFuns, NoiseRouterData.SHIFT_Z)
        c.register(
            AMPLIFIED_REGION,
            DensityFunctions.cache2D(
                DensityFunctions.multiply(
                    DensityFunctions.constant(3.0),
                    DensityFunctions.noise(noiseParams.getHolderOrThrow(AMPLIFIED_TRANSITION_NOISE), 0.0, 0.25)
                ).clamp(0.0, 1.0)
            )
        )
        c.register(
            NORMAL_REGION,
            DensityFunctions.cache2D(
                DensityFunctions.multiply(
                    DensityFunctions.constant(-1.0),
                    DensityFunctions.add(
                        DensityFunctions.constant(-1.0),
                        getFunction(densityFuns, AMPLIFIED_REGION)
                    )
                )
            )
        )

        c.register(
            TEMPERATURE_WF, DensityFunctions.shiftedNoise2d(
                shiftX,
                shiftZ,
                0.25,
                noiseParams.getHolderOrThrow(TEMPERATURE_NOISE_WF)
            )
        )
        c.register(
            VEGETATION_WF, DensityFunctions.shiftedNoise2d(
                shiftX,
                shiftZ,
                0.25,
                noiseParams.getHolderOrThrow(VEGETATION_NOISE_WF)
            )
        )
        c.register(
            CONTINENTS_WF, DensityFunctions.shiftedNoise2d(
                shiftX,
                shiftZ,
                0.25,
                noiseParams.getHolderOrThrow(CONTINENTS_NOISE_WF)
            )
        )
        c.register(
            EROSION_WF, DensityFunctions.shiftedNoise2d(
                shiftX,
                shiftZ,
                0.25,
                noiseParams.getHolderOrThrow(EROSION_NOISE_WF)
            )
        )


        val slopedCheeseFunction = getFunction(densityFuns, SLOPED_CHEESE_MIX)
        val caveEntrancesFunction = DensityFunctions.min(
            slopedCheeseFunction,
            DensityFunctions.multiply(
                DensityFunctions.constant(5.0),
                getFunction(densityFuns, NoiseRouterData.CAVES_ENTRANCES_OVERWORLD)
            )
        )
        val cavesMainFunction = DensityFunctions.rangeChoice(
            slopedCheeseFunction,
            -1000000.0,
            1.5625,
            caveEntrancesFunction,
            NoiseRouterData.underground(densityFuns, noiseParams, slopedCheeseFunction)
        )
        c.register(
            FINAL_DENSITY_MIX,
            DensityFunctions.min(
                NoiseRouterData.postProcess(
                    transitionAmplified(
                        densityFuns,
                        NoiseRouterData.method_42366(
                            false,
                            cavesMainFunction
                        ),
                        NoiseRouterData.method_42366(
                            true,
                            cavesMainFunction
                        )
                    )
                ), getFunction(densityFuns, NoiseRouterData.CAVES_NOODLE_OVERWORLD)
            )
        )



        c.register(
            INITIAL_DENSITY_WITHOUT_JAGGEDNESS,
            transitionAmplified(
                densityFuns,
                getFunction(densityFuns, I_D_W_J_NORMAL),
                getFunction(densityFuns, I_D_W_J_AMPLIFIED)
            )
        )

        val idwjFlesh = DensityFunctions.add(
            NoiseRouterData.noiseGradientDensity(
                DensityFunctions.cache2D(getFunction(densityFuns, FACTOR_MIX)),
                getFunction(densityFuns, DEPTH_MIX)
            ),
            DensityFunctions.constant(-0.703125)
        ).clamp(-64.0, 64.0)
        c.register(
            I_D_W_J_NORMAL,
            initialDensityWithoutJaggedness(
                false,
                idwjFlesh
            )
        )
        c.register(
            I_D_W_J_AMPLIFIED,
            initialDensityWithoutJaggedness(
                true,
                idwjFlesh
            )
        )

        createGenerationSplinesAmplifiedMixture(
            c,
            densityFuns,
            DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.JAGGED), 1500.0, 0.0),
            densityFuns.getHolderOrThrow(CONTINENTS_WF),
            densityFuns.getHolderOrThrow(EROSION_WF),
            OFFSET_MIX,
            FACTOR_MIX,
            JAGGEDNESS_MIX,
            DEPTH_MIX,
            SLOPED_CHEESE_MIX
        )
    }

    private fun createGenerationSplinesAmplifiedMixture(
        c: BootstrapContext<DensityFunction>,
        densityFuns: HolderProvider<DensityFunction>,
        jaggedNoise: DensityFunction,
        continents: Holder<DensityFunction>,
        erosion: Holder<DensityFunction>,
        offset: RegistryKey<DensityFunction>,
        factor: RegistryKey<DensityFunction>,
        jaggedness: RegistryKey<DensityFunction>,
        depth: RegistryKey<DensityFunction>,
        cheese: RegistryKey<DensityFunction>
    ) {
        val continentsNoise = DensityFunctions.Spline.FunctionWrapper(continents)
        val erosionNoise = DensityFunctions.Spline.FunctionWrapper(erosion)
        val ridgesNoise =
            DensityFunctions.Spline.FunctionWrapper(densityFuns.getHolderOrThrow(NoiseRouterData.RIDGES_OVERWORLD))
        val ridgesFoldedNoise =
            DensityFunctions.Spline.FunctionWrapper(densityFuns.getHolderOrThrow(NoiseRouterData.RIDGES_FOLDED_OVERWORLD))
        val offsetSpline = NoiseRouterData.method_41551(
            c,
            offset,
            NoiseRouterData.splineWithBlending(
                DensityFunctions.add(
                    DensityFunctions.constant(-0.50375),
                    transitionAmplified(
                        densityFuns,
                        DensityFunctions.copySpline(
                            VanillaTerrainParametersCreator.method_42056(
                                continentsNoise,
                                erosionNoise,
                                ridgesFoldedNoise,
                                false
                            )
                        ),
                        DensityFunctions.copySpline(
                            VanillaTerrainParametersCreator.method_42056(
                                continentsNoise,
                                erosionNoise,
                                ridgesFoldedNoise,
                                true
                            )
                        )
                    )
                ), DensityFunctions.getBlendOffset()
            )
        )
        val factorSpline = NoiseRouterData.method_41551(
            c,
            factor,
            NoiseRouterData.splineWithBlending(
                transitionAmplified(
                    densityFuns,
                    DensityFunctions.copySpline(
                        VanillaTerrainParametersCreator.method_42055(
                            continentsNoise,
                            erosionNoise,
                            ridgesNoise,
                            ridgesFoldedNoise,
                            false
                        )
                    ),
                    DensityFunctions.copySpline(
                        VanillaTerrainParametersCreator.method_42055(
                            continentsNoise,
                            erosionNoise,
                            ridgesNoise,
                            ridgesFoldedNoise,
                            true
                        )
                    )
                ), NoiseRouterData.BLENDING_FACTOR
            )
        )
        val depthFunction = NoiseRouterData.method_41551(
            c,
            depth,
            DensityFunctions.add(DensityFunctions.clampedGradientY(-64, 320, 1.5, -1.5), offsetSpline)
        )
        val jaggednessSpline = NoiseRouterData.method_41551(
            c,
            jaggedness,
            NoiseRouterData.splineWithBlending(
                transitionAmplified(
                    densityFuns,
                    DensityFunctions.copySpline(
                        VanillaTerrainParametersCreator.method_42058(
                            continentsNoise,
                            erosionNoise,
                            ridgesNoise,
                            ridgesFoldedNoise,
                            false
                        )
                    ),
                    DensityFunctions.copySpline(
                        VanillaTerrainParametersCreator.method_42058(
                            continentsNoise,
                            erosionNoise,
                            ridgesNoise,
                            ridgesFoldedNoise,
                            true
                        )
                    )
                ), NoiseRouterData.BLENDING_JAGGEDNESS
            )
        )
        val jagged = DensityFunctions.multiply(jaggednessSpline, jaggedNoise.halfNegative())
        val depthAndJaggedness = NoiseRouterData.noiseGradientDensity(
            factorSpline,
            DensityFunctions.add(depthFunction, jagged)
        )
        c.register(
            cheese,
            DensityFunctions.add(
                depthAndJaggedness,
                getFunction(densityFuns, NoiseRouterData.BASE_3D_NOISE_OVERWORLD)
            )
        )
    }

    fun OverworldNoiseSettingsMakerAmplifiedMixture(
        densityFuns: HolderProvider<DensityFunction>,
        noiseParams: HolderProvider<NoiseParameters>
    ): NoiseRouter {
        val aquiferBarrierFunction =
            DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.AQUIFER_BARRIER), 0.5)
        val aquiferFluidLevelFloodedness = DensityFunctions.noise(
            noiseParams.getHolderOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_FLOODEDNESS),
            0.67
        )
        val aquiferFluidLevelSpread = DensityFunctions.noise(
            noiseParams.getHolderOrThrow(NoiseParametersKeys.AQUIFER_FLUID_LEVEL_SPREAD),
            0.7142857142857143
        )
        val aquiferLava = DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.AQUIFER_LAVA))
        val yLevel = getFunction(densityFuns, NoiseRouterData.Y)
        val veinTypeMinY = Stream.of(*VeinType.entries.toTypedArray()).mapToInt { type: VeinType -> type.minY }
            .min().orElse(-DimensionType.MIN_Y * 2)
        val veinTypeMaxY = Stream.of(*VeinType.entries.toTypedArray()).mapToInt { type: VeinType -> type.maxY }
            .max().orElse(-DimensionType.MIN_Y * 2)
        val oreVeininessFunction = NoiseRouterData.yLimitedInterpolatable(
            yLevel,
            DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.ORE_VEININESS), 1.5, 1.5),
            veinTypeMinY,
            veinTypeMaxY,
            0
        )
        val veinScale = 4.0
        val veinFunctionA = NoiseRouterData.yLimitedInterpolatable(
            yLevel,
            DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.ORE_VEIN_A), veinScale, veinScale),
            veinTypeMinY,
            veinTypeMaxY,
            0
        ).abs()
        val veinFunctionB = NoiseRouterData.yLimitedInterpolatable(
            yLevel,
            DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.ORE_VEIN_B), veinScale, veinScale),
            veinTypeMinY,
            veinTypeMaxY,
            0
        ).abs()
        val veinFunction = DensityFunctions.add(
            DensityFunctions.constant(-0.07999999821186066),
            DensityFunctions.max(veinFunctionA, veinFunctionB)
        )
        val veinGapFunction = DensityFunctions.noise(noiseParams.getHolderOrThrow(NoiseParametersKeys.ORE_GAP))
        return NoiseRouter(
            aquiferBarrierFunction,
            aquiferFluidLevelFloodedness,
            aquiferFluidLevelSpread,
            aquiferLava,
            getFunction(densityFuns, TEMPERATURE_WF),
            getFunction(densityFuns, VEGETATION_WF),
            getFunction(densityFuns, CONTINENTS_WF),
            getFunction(densityFuns, EROSION_WF),
            getFunction(densityFuns, DEPTH_MIX),
            getFunction(densityFuns, NoiseRouterData.RIDGES_OVERWORLD),
            getFunction(densityFuns, INITIAL_DENSITY_WITHOUT_JAGGEDNESS),
            getFunction(densityFuns, FINAL_DENSITY_MIX),
            oreVeininessFunction,
            veinFunction,
            veinGapFunction
        )
    }

    private fun initialDensityWithoutJaggedness(amplified: Boolean, densityFunction: DensityFunction): DensityFunction {
        return createIDWJ(
            densityFunction,
            -64,
            384,
            if (amplified) 16 else 80,
            if (amplified) 0 else 64,
            -0.078125,
            0,
            24,
            if (amplified) 0.4 else 0.1171875
        )
    }

    private fun createIDWJ(
        function1: DensityFunction,
        yMin: Int,
        yHeight: Int,
        upperYRoof: Int,
        lowerYRoof: Int,
        clampOffset: Double,
        lowerYFloor: Int,
        upperYfloor: Int,
        addToAll: Double
    ): DensityFunction {
        val densityFunction =
            DensityFunctions.clampedGradientY(yMin + yHeight - upperYRoof, yMin + yHeight - lowerYRoof, 1.0, 0.0)
        val densityFunction2 = DensityFunctions.method_42359(densityFunction, clampOffset, function1)
        val densityFunction3 = DensityFunctions.clampedGradientY(yMin + lowerYFloor, yMin + upperYfloor, 0.0, 1.0)
        return DensityFunctions.method_42359(densityFunction3, addToAll, densityFunction2)
    }


    fun transitionAmplified(
        densityFuns: HolderProvider<DensityFunction>,
        normal: DensityFunction,
        amplified: DensityFunction
    ): DensityFunction {
        return DensityFunctions.add(
            DensityFunctions.multiply(
                normal,
                getFunction(densityFuns, NORMAL_REGION)
            ),
            DensityFunctions.multiply(
                amplified,
                getFunction(densityFuns, AMPLIFIED_REGION)
            )
        )
    }

    private fun getFunction(
        holderProvider: HolderProvider<DensityFunction>,
        key: RegistryKey<DensityFunction>
    ): DensityFunction {
        return HolderHolder(holderProvider.getHolderOrThrow(key))
    }
}