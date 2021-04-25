package me.plobnob.variablevisibility.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.plobnob.variablevisibility.Main;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

public class VisibilityConfigScreen implements ModMenuApi {
	
	@Override
	public String getModId() {
		return Main.MODID;
	}
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
			return parent -> AutoConfig.getConfigScreen(VisibilityConfig.class, parent).get();
	}

}
