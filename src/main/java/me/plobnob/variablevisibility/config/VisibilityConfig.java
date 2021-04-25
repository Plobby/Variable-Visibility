package me.plobnob.variablevisibility.config;

import me.plobnob.variablevisibility.Main;
import me.plobnob.variablevisibility.struct.NameTagRender;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;

@Config(name = Main.MODID)
public class VisibilityConfig implements ConfigData {
	
	/**
	 * General enabled/disabled
	 */
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public boolean enabled = false;
	
	/**
	 * General visibility
	 */
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public boolean allShadows = false;
	
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
	public NameTagRender allNameTags = NameTagRender.SHIFTING;
	
	/**
	 * Transparency
	 */
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public float transparentRange = 16.0F;
	
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public boolean transparentShadows = false;
	
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
	public NameTagRender transparentNameTags = NameTagRender.SHIFTING;
	
	/**
	 * Invisibility
	 */
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public float invisibilityRange = 4.0F;
	
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	public boolean invisibilityShadows = false;
	
	@ConfigEntry.Category("general")
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
	public NameTagRender invisibilityNameTags = NameTagRender.NO;
	
}
