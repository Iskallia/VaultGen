package iskallia.vaultgen.mixin;

import iskallia.vaultgen.IDimContext;
import iskallia.vaultgen.config.ModGenConfig;
import iskallia.vaultgen.init.ModConfigs;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.BiomeLayer;
import net.minecraftforge.common.BiomeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(BiomeLayer.class)
public class MixinBiomeLayer {

	private static final List<BiomeManager.BiomeEntry>[] VANILLA = setupVanilla();

	@Shadow @Final private boolean legacyDesert;
	@Shadow private List<BiomeManager.BiomeEntry>[] biomes;

	@Inject(method = "getBiome", at = @At("HEAD"), cancellable = true, remap = false)
	public void getBiome(BiomeManager.BiomeType type, INoiseRandom context, CallbackInfoReturnable<RegistryKey<Biome>> ci) {
		if(type == BiomeManager.BiomeType.DESERT && this.legacyDesert) {
			type = BiomeManager.BiomeType.DESERT_LEGACY;
		}

		RegistryKey<World> dimension = ((IDimContext)context).getDimension();

		if(dimension != null) {
			List<BiomeManager.BiomeEntry> biomeList = new ArrayList<>(this.biomes[type.ordinal()]);
			List<BiomeManager.BiomeEntry> vanillaList = VANILLA[type.ordinal()];
			biomeList.removeIf(entry -> !ModConfigs.MOD_GEN.isValid(ModGenConfig.Type.BIOMES, dimension, entry.getKey().getLocation()));

			int totalWeight = WeightedRandom.getTotalWeight(biomeList);

			if(totalWeight != 0) {
				int weight = equals(vanillaList, biomeList) ? context.random(totalWeight / 10) * 10 : context.random(totalWeight);
				ci.setReturnValue(WeightedRandom.getRandomItem(biomeList, weight).getKey());
			} else {
				ci.setReturnValue(Biomes.THE_VOID);
			}
		}
	}

	private boolean equals(List<BiomeManager.BiomeEntry> a, List<BiomeManager.BiomeEntry> b) {
		if(a.size() != b.size()) return false;

		for(int i = 0; i < a.size(); i++) {
			BiomeManager.BiomeEntry e1 = a.get(i), e2 = b.get(i);

			if(e1.getKey() != e2.getKey() || e1.itemWeight != e2.itemWeight) {
				return false;
			}
		}

		return true;
	}

	private static List<BiomeManager.BiomeEntry>[] setupVanilla() {
		List<BiomeManager.BiomeEntry>[] biomes = new List[BiomeManager.BiomeType.values().length];

		biomes[BiomeManager.BiomeType.DESERT_LEGACY.ordinal()] = Arrays.asList(
			new BiomeManager.BiomeEntry(Biomes.DESERT, 10),
			new BiomeManager.BiomeEntry(Biomes.FOREST, 10),
			new BiomeManager.BiomeEntry(Biomes.MOUNTAINS, 10),
			new BiomeManager.BiomeEntry(Biomes.SWAMP, 10),
			new BiomeManager.BiomeEntry(Biomes.PLAINS, 10),
			new BiomeManager.BiomeEntry(Biomes.TAIGA, 10)
		);

		biomes[BiomeManager.BiomeType.DESERT.ordinal()] = Arrays.asList(
			new BiomeManager.BiomeEntry(Biomes.DESERT, 30),
			new BiomeManager.BiomeEntry(Biomes.SAVANNA, 20),
			new BiomeManager.BiomeEntry(Biomes.PLAINS, 10)
		);

		biomes[BiomeManager.BiomeType.WARM.ordinal()] = Arrays.asList(
			new BiomeManager.BiomeEntry(Biomes.FOREST, 10),
			new BiomeManager.BiomeEntry(Biomes.DARK_FOREST, 10),
			new BiomeManager.BiomeEntry(Biomes.MOUNTAINS, 10),
			new BiomeManager.BiomeEntry(Biomes.PLAINS, 10),
			new BiomeManager.BiomeEntry(Biomes.BIRCH_FOREST, 10),
			new BiomeManager.BiomeEntry(Biomes.SWAMP, 10)
		);

		biomes[BiomeManager.BiomeType.COOL.ordinal()] = Arrays.asList(
			new BiomeManager.BiomeEntry(Biomes.FOREST, 10),
			new BiomeManager.BiomeEntry(Biomes.MOUNTAINS, 10),
			new BiomeManager.BiomeEntry(Biomes.TAIGA, 10),
			new BiomeManager.BiomeEntry(Biomes.PLAINS, 10)
		);

		biomes[BiomeManager.BiomeType.ICY.ordinal()] = Arrays.asList(
			new BiomeManager.BiomeEntry(Biomes.SNOWY_TUNDRA, 30),
			new BiomeManager.BiomeEntry(Biomes.SNOWY_TAIGA, 10)
		);

		return biomes;
	}
	
}
