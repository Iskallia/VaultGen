package iskallia.vaultgen.mixin;

import iskallia.vaultgen.init.ModConfigs;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(StructureStart.class)
public abstract class MixinStructureStart {

	@Shadow public abstract Structure<?> getStructure();

	@Inject(method = "func_230366_a_", at = @At("HEAD"), cancellable = true)
	public void func_230366_a_(ISeedReader reader, StructureManager manager, ChunkGenerator chunkGen,
	                           Random random, MutableBoundingBox aabb, ChunkPos chunkPos, CallbackInfo ci) {
		ServerWorld world = reader.getWorld();

		if(!ModConfigs.MOD_GEN.isValid(world.getDimensionKey().getLocation(), this.getStructure().getRegistryName())) {
			ci.cancel();
		}
	}

}