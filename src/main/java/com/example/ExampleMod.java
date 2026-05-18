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
            while (spawnBotKey.isPressed()) {
                if (client.world != null && client.player != null) {
                    OtherClientPlayerEntity fakeBot = new OtherClientPlayerEntity(client.world, client.player.getGameProfile());
                    fakeBot.copyPositionAndRotation(client.player);
                    fakeBot.setId(botIdCounter--);
                    client.world.addEntity(fakeBot);
                    client.player.sendMessage(Text.literal("§aBot Başarıyla Çağrıldı!"), false);
                }
            }
        });
    }
}
