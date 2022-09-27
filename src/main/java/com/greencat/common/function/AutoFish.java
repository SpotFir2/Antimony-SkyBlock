package com.greencat.common.function;

import com.greencat.Antimony;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.config.ConfigLoader;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Random;

public class AutoFish {
    public void AutoFishEventRegiser(){
        MinecraftForge.EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.getMinecraft();
    Utils utils = new Utils();
    Robot robot = new Robot();
    static int Tick = 40;
    Random randomYaw = new Random();
    Random randomPitch = new Random();
    int RandomNumber1 = randomYaw.nextInt(20);
    int RandomNumber2 = randomPitch.nextInt(20);
    static Boolean AutoFishStatus = false;


    public AutoFish() throws AWTException {
        try{
            if (FunctionManager.getStatus("AutoFish")) {
                if (mc.thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                    this.Tick = 0;
                    //utils.print("Fish now Up");
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @SubscribeEvent
    public void StartTriggerAutoFish(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (!FunctionManager.getStatus("AutoFish")){
                AutoFishStatus = false;
            }
        }
    }
    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld != null) {
            if (Tick < 40) {
                if (Tick == 5) {
                    mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw + RandomNumber1;
                    mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch + RandomNumber2;
                } else if (Tick == 10) {
                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                } else if (Tick == 29) {
                    mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch - RandomNumber2;
                } else if(Tick == 37){
                    if(Antimony.AutoFishYawState) {
                        mc.thePlayer.rotationYaw = (float) (mc.thePlayer.rotationYaw - RandomNumber1 + 0.3);
                        Antimony.AutoFishYawState = false;
                    } else {
                        mc.thePlayer.rotationYaw = (float) (mc.thePlayer.rotationYaw - RandomNumber1 - 0.3);
                        Antimony.AutoFishYawState = true;
                    }
                } else if (Tick == 39) {
                    Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getHeldItem());
                }
                Tick = Tick + 1;
            } else {
                Tick = 40;
            }
        }
    }
    @SubscribeEvent
    public void onPacketReceived(PlaySoundEvent event) throws AWTException {
        if(Minecraft.getMinecraft().theWorld != null) {
            if (FunctionManager.getStatus("AutoFish")) {
                if (event.name.equals("game.player.swim.splash")) {
                    if (AutoFishStatus) {
                        new AutoFish();
                        AutoFishStatus = false;
                        if(ConfigLoader.getAutoFishNotice()) {
                            utils.print("钓鱼检测状态:关闭");
                        }
                    } else {
                        AutoFishStatus = true;
                        if(ConfigLoader.getAutoFishNotice()) {
                            utils.print("钓鱼检测状态:开启");
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void PLayerInteract(PlayerInteractEvent event) {
        if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            try{
                if (Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == Items.fishing_rod) {
                    if (AutoFishStatus) {
                        AutoFishStatus = false;
                        if(ConfigLoader.getAutoFishNotice()) {
                            utils.print("钓鱼检测状态:关闭");
                        }
                    }
                }
            } catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
