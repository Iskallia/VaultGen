package iskallia.vaultgen;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

	@Override
	public void connect() {
		Mixins.addConfigurations("assets/" + VaultGen.MOD_ID + "/" + VaultGen.MOD_ID + ".mixins.json");
	}

}
