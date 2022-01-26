package iskallia.vaultgen.config;

import com.google.gson.annotations.Expose;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModGenConfig extends Config {

	@Expose protected Map<ResourceLocation, Map<Type, Predicate>> entries;

	public boolean isValid(Type type, RegistryKey<World> dimension, ResourceLocation feature) {
		Map<Type, Predicate> entry = this.entries.get(dimension.getLocation());

		if(entry != null) {
			Predicate predicate = entry.get(type);

			if(predicate != null && feature != null) {
				return predicate.isValid(feature.getNamespace());
			}
		}

		return true;
	}

	@Override
	public String getName() {
		return "mod_gen";
	}

	@Override
	protected void reset() {
		this.entries = new LinkedHashMap<>();
		Map<Type, Predicate> entry = new LinkedHashMap<>();

		for(Type value : Type.values()) {
			entry.put(value, value == Type.ENTITIES
				? new Predicate(Filter.BLACKLIST)
				: new Predicate(Filter.WHITELIST, "minecraft", "the_vault"));
		}

		this.entries.put(World.OVERWORLD.getLocation(), entry);
	}

	public static class Predicate {
		@Expose public Filter filter;
		@Expose public List<String> mods;

		public Predicate(Filter filter, String... mods) {
			this.filter = filter;
			this.mods = Arrays.asList(mods);
		}

		public boolean isValid(String mod) {
			switch(this.filter) {
				case WHITELIST: return this.mods.contains(mod);
				case BLACKLIST: return !this.mods.contains(mod);
			};

			return true;
		}
	}

	public enum Type {
		DECORATORS, STRUCTURES, CARVERS, BIOMES, ENTITIES
	}

	public enum Filter {
		WHITELIST, BLACKLIST
	}

}
