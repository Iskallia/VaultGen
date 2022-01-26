package iskallia.vaultgen.mixin;

import iskallia.vaultgen.IDimContext;
import iskallia.vaultgen.config.ModGenConfig;
import iskallia.vaultgen.init.ModConfigs;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator implements IDimContext {

	@Shadow @Final protected BiomeProvider biomeProvider;
	@Shadow @Final protected BiomeProvider field_235949_c_;
	private RegistryKey<World> dimension;

	@Override
	public RegistryKey<World> getDimension() {
		return this.dimension;
	}

	@Override
	public void setDimension(RegistryKey<World> dimension) {
		if(this.dimension != (this.dimension = dimension)) {
			if(this.biomeProvider instanceof OverworldBiomeProvider) {
				((IDimContext)this.biomeProvider).setDimension(dimension);
			}

			if(this.field_235949_c_ instanceof OverworldBiomeProvider) {
				((IDimContext)this.field_235949_c_).setDimension(dimension);
			}
		}
	}

	@Redirect(method = "func_230350_a_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/carver/ConfiguredCarver;shouldCarve(Ljava/util/Random;II)Z"))
	public boolean func_230350_a_(ConfiguredCarver<?> carver, Random rand, int chunkX, int chunkZ) {
		ResourceLocation key = ((AccessorConfiguredCarver)carver).getCarver().getRegistryName();

		if(!ModConfigs.MOD_GEN.isValid(ModGenConfig.Type.CARVERS, this.getDimension(), key)) {
			return false;
		}

		return carver.shouldCarve(rand, chunkX, chunkZ);
	}

}
