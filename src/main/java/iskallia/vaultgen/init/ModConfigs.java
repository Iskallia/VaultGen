package iskallia.vaultgen.init;

import iskallia.vaultgen.config.ModGenConfig;

public class ModConfigs {

	public static ModGenConfig MOD_GEN;

	public static void register() {
		MOD_GEN = new ModGenConfig().readConfig();
	}

}
