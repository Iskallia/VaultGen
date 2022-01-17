package iskallia.vaultgen;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public interface IDimContext {

	RegistryKey<World> getDimension();

	void setDimension(RegistryKey<World> dimension);

}
