package iskallia.vaultgen.mixin;

import iskallia.vaultgen.IDimContext;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.LazyAreaLayerContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LazyAreaLayerContext.class)
public class MixinLazyAreaLayerContext implements IDimContext {

	public RegistryKey<World> dimension;

	@Override
	public RegistryKey<World> getDimension() {
		return this.dimension;
	}

	@Override
	public void setDimension(RegistryKey<World> dimension) {
		this.dimension = dimension;
	}

}
