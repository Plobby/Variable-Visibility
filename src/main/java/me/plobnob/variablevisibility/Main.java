package me.plobnob.variablevisibility;

import me.plobnob.variablevisibility.config.VisibilityConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	
	public static final String MODID = "variablevisibility";
	
	@Override
	public void onInitialize() {
		// Register mod config
		AutoConfig.register(VisibilityConfig.class, GsonConfigSerializer::new);
	}
	
}
