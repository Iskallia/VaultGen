package iskallia.vaultgen.mixin;

import iskallia.vaultgen.IDimContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.SaveFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Shadow @Final private ServerChunkProvider field_241102_C_;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void ctor(MinecraftServer server, Executor executor, SaveFormat.LevelSave save, IServerWorldInfo info,
	                 RegistryKey<World> dimension, DimensionType type, IChunkStatusListener listener,
	                 ChunkGenerator chunkGen, boolean p_i241885_9_, long p_i241885_10_, List<ISpecialSpawner> spawners,
	                 boolean p_i241885_13_, CallbackInfo ci) {
		((IDimContext)this.field_241102_C_.getChunkGenerator()).setDimension(dimension);
	}

}
