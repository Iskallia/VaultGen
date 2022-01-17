package iskallia.vaultgen.config;

import com.google.gson.annotations.Expose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModGenConfig extends Config {

	@Expose protected Map<ResourceLocation, Entry> entries;

	public boolean isValid(ResourceLocation dimension, ResourceLocation feature) {
		Entry entry = this.entries.get(dimension);

		if(entry != null) {
			return entry.isValid(feature.getNamespace());
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

		Entry entry = new Entry();
		entry.filter = Filter.WHITELIST;
		entry.mods = new ArrayList<>();
		entry.mods.add("minecraft");
		entry.mods.add("the_vault");
		this.entries.put(World.OVERWORLD.getLocation(), entry);
	}

	public static class Entry {
		@Expose public Filter filter;
		@Expose public List<String> mods;

		public boolean isValid(String mod) {
			switch(this.filter) {
				case WHITELIST: return this.mods.contains(mod);
				case BLACKLIST: return !this.mods.contains(mod);
			};

			return true;
		}
	}

	public enum Filter {
		WHITELIST, BLACKLIST
	}

}
