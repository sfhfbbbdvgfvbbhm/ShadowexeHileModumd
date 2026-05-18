package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer {

    private static KeyBinding spawnBotKey;
    private static int botIdCounter = -2000;

    @Override
    public void onInitialize() {
        spawnBotKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cheat.spawnbot",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.cheat.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (spawnBotKey.wasPressed()) {
                if (client.world != null && client.player != null) {
                    
                    OtherClientPlayerEntity fakePlayerEntity = new OtherClientPlayerEntity(
                            client.world, 
                            client.player.getGameProfile()
                    );

                    fakePlayerEntity.refreshPositionAndAngles(
                            client.player.getX(), 
                            client.player.getY(), 
                            client.player.getZ(), 
                            client.player.getYaw(), 
                            client.player.getPitch()
                    );

                    botIdCounter--;
                    client.world.addEntity(botIdCounter, fakePlayerEntity);

                    client.player.sendMessage(Text.literal("[SYSTEM] Fake player successfully deployed."), false);
                }
            }
        });
    }
}
