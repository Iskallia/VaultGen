package iskallia.vaultgen.mixin;

import iskallia.vaultgen.config.ModGenConfig;
import iskallia.vaultgen.init.ModConfigs;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(WorldEntitySpawner.class)
public class MixinWorldEntitySpawner {

	private static final ThreadLocal<IServerWorld> WORLD_REF = ThreadLocal.withInitial(() -> null);

	@Inject(method = "performWorldGenSpawning", at = @At("HEAD"))
	private static void performWorldGenSpawningHead(IServerWorld world, Biome biome, int centerX, int centerZ,
	                                                Random diameterX, CallbackInfo ci) {
		WORLD_REF.set(world);
	}

	@Redirect(method = "performWorldGenSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/MobSpawnInfo;getSpawners(Lnet/minecraft/entity/EntityClassification;)Ljava/util/List;"))
	private static List<MobSpawnInfo.Spawners> performWorldGenSpawning(MobSpawnInfo info, EntityClassification type) {
		List<MobSpawnInfo.Spawners> list = new ArrayList<>(info.getSpawners(type));

		list.removeIf(entity -> {
			RegistryKey<World> dimension = WORLD_REF.get().getWorld().getDimensionKey();
			return !ModConfigs.MOD_GEN.isValid(ModGenConfig.Type.ENTITIES, dimension, entity.type.getRegistryName());
		});

		return list;
	}

}
