package me.scwunge.mods.portalgun.client;

import me.scwunge.mods.portalgun.network.SetChannelPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public class PortalChannelScreen extends Screen {
   private final String initialChannel;
   private EditBox channelBox;

   public PortalChannelScreen(String currentChannel) {
      super(Component.translatable("orespawn.screen.channel"));
      this.initialChannel = currentChannel != null && !currentChannel.isBlank() ? currentChannel : "Chell";
   }

   protected void init() {
      int centerX = this.width / 2;
      int centerY = this.height / 2;
      this.channelBox = new EditBox(this.font, centerX - 100, centerY - 10, 200, 20, Component.translatable("orespawn.screen.channel"));
      this.channelBox.setMaxLength(24);
      this.channelBox.setValue(this.initialChannel);
      this.channelBox.setFilter(s -> {
         for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < ' ' || c == 127) {
               return false;
            }
         }

         return true;
      });
      this.addRenderableWidget(this.channelBox);
      this.setInitialFocus(this.channelBox);
      this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> this.onDone()).bounds(centerX - 100, centerY + 20, 95, 20).build());
      this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> this.onClose()).bounds(centerX + 5, centerY + 20, 95, 20).build());
   }

   private void onDone() {
      String value = this.channelBox != null ? this.channelBox.getValue() : this.initialChannel;
      PacketDistributor.sendToServer(new SetChannelPayload(value), new CustomPacketPayload[0]);
      this.onClose();
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(graphics, mouseX, mouseY, partialTick);
      super.render(graphics, mouseX, mouseY, partialTick);
      graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 40, 16777215);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.minecraft != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
         return true;
      } else if (keyCode != 257 && keyCode != 335) {
         return super.keyPressed(keyCode, scanCode, modifiers);
      } else {
         this.onDone();
         return true;
      }
   }
}
