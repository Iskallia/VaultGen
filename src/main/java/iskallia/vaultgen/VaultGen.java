package iskallia.vaultgen;

import iskallia.vaultgen.init.ModConfigs;
import net.minecraftforge.fml.common.Mod;

@Mod(VaultGen.MOD_ID)
public class VaultGen {

	public static final String MOD_ID = "vault_gen";

	public VaultGen() {
		ModConfigs.register();
	}

}
