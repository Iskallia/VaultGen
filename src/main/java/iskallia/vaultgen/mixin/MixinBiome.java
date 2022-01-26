package iskallia.vaultgen.mixin;

import iskallia.vaultgen.config.ModGenConfig;
import iskallia.vaultgen.init.ModConfigs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Biome.class)
public abstract class MixinBiome {

	@Redirect(method = "generateFeatures", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/ConfiguredFeature;func_242765_a(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean generateDecorators(ConfiguredFeature<?, ?> feature, ISeedReader reader, ChunkGenerator chunkGen, Random random, BlockPos pos) {
		ServerWorld world = reader.getWorld();
		MutableRegistry<ConfiguredFeature<?, ?>> registry = reader.getWorld().func_241828_r().getRegistry(Registry.CONFIGURED_FEATURE_KEY);

		if(!ModConfigs.MOD_GEN.isValid(ModGenConfig.Type.DECORATORS, world.getDimensionKey(), registry.getKey(feature))) {
			return false;
		}

		return feature.func_242765_a(reader, chunkGen, random, pos);
    }

}
