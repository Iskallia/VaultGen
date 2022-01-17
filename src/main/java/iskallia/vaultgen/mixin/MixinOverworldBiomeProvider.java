package iskallia.vaultgen.mixin;

import iskallia.vaultgen.IDimContext;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OverworldBiomeProvider.class)
public abstract class MixinOverworldBiomeProvider implements IDimContext {

	@Shadow @Final @Mutable private Layer genBiomes;
	@Shadow @Final private long seed;
	@Shadow @Final private boolean legacyBiomes;
	@Shadow @Final private boolean largeBiomes;
	public RegistryKey<World> dimension;

	@Override
	public RegistryKey<World> getDimension() {
		return this.dimension;
	}

	@Override
	public void setDimension(RegistryKey<World> dimension) {
		if(this.dimension != (this.dimension = dimension)) {
			IAreaFactory<LazyArea> factory = LayerUtil.func_237216_a_(this.legacyBiomes, this.largeBiomes ? 6 : 4, 4, i -> {
				LazyAreaLayerContext context = new LazyAreaLayerContext(25, this.seed, i);
				((IDimContext)context).setDimension(this.getDimension());
				return context;
			});

			this.genBiomes = new Layer(factory);
		}
	}

}
