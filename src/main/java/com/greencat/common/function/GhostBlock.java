package com.greencat.common.function;

import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.key.KeyLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class GhostBlock {
    public GhostBlock(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void CreateGhostBlock(InputEvent.KeyInputEvent event){
        if(FunctionManager.getStatus("GhostBlock")) {
            if (KeyLoader.GhostBlock.isPressed()) {
                MovingObjectPosition position = Minecraft.getMinecraft().thePlayer.rayTrace(6.0D, 0.0F);
                if (Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock() != Blocks.chest &&
                        Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock() != Blocks.stone_button &&
                        Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock() != Blocks.wooden_button &&
                        Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock() != Blocks.skull &&
                        Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock() != Blocks.lever &&
                        Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(position.getBlockPos()).getBlock() != Blocks.command_block) {
                    Minecraft.getMinecraft().thePlayer.worldObj.setBlockToAir(position.getBlockPos());
                }
            }
        }
    }
}
