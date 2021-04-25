package me.plobnob.variablevisibility.config;

import org.lwjgl.glfw.GLFW;

import me.plobnob.variablevisibility.Main;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class VisibilityKeybinds implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// Create bindings
		KeyBinding enableBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + Main.MODID + ".enableKey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "category." + Main.MODID + ".general"));
		
		// Client tick event to listen to binding
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// Check the binding is pressed
			while (enableBinding.wasPressed()) {
				// Get config instance
				VisibilityConfig config = AutoConfig.getConfigHolder(VisibilityConfig.class).getConfig();
							
				// Enable/disable visibility effects
				config.enabled = !config.enabled;
			}
		});
	}

}
