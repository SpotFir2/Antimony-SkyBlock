package com.greencat.antimony.common.function;

import com.greencat.antimony.core.FunctionManager.FunctionManager;
import com.greencat.antimony.core.Pathfinder;
import com.greencat.antimony.core.config.getConfigByFunctionName;
import com.greencat.antimony.core.event.CustomEventHandler;
import com.greencat.antimony.core.nukerCore2;
import com.greencat.antimony.core.nukerWrapper;
import com.greencat.antimony.utils.SmoothRotation;
import com.greencat.antimony.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class PowderBot {
    public PowderBot() {
        MinecraftForge.EVENT_BUS.register(this);
        CustomEventHandler.EVENT_BUS.register(this);
    }
    static List<Vec3> ignoreList = new ArrayList<Vec3>();
    private static Vec3 closestChest = null;
    static boolean looking = false;
    nukerCore2 nuker = nukerWrapper.nuker;
    BlockPos pos;

    @SubscribeEvent
    public void move(TickEvent.ClientTickEvent event){
        if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
            if (FunctionManager.getStatus("PowderBot")) {
                if (!((Boolean)getConfigByFunctionName.get("PowderBot","chestOnly"))) {
                    if (!looking) {
                        if (!FunctionManager.getStatus("NukerWrapper")) {
                            nukerWrapper.enable();
                        }
                        nuker.rotation = nukerCore2.RotationType.SERVER_ROTATION;
                        Vec3 stoneVec = closestStoneUpperThanPlayer();
                        if (stoneVec != null) {
                            pos = new BlockPos(stoneVec.xCoord, stoneVec.yCoord, stoneVec.zCoord);
                        } else {
                            pos = null;
                        }
                        if (nuker.requestBlock) {
                            nuker.putBlock(pos);
                        }
                        if (!hasStone() && !FunctionManager.getStatus("Pathfinding")) {
                            Vec3 stone = closestStoneUpperThanPlayer(20);
                            Pathfinder.setup(new BlockPos(Utils.floorVec(Minecraft.getMinecraft().thePlayer.getPositionVector())), new BlockPos(stone.xCoord, stone.yCoord, stone.zCoord), 0.0D);
                            if (Pathfinder.hasPath()) {
                                if (!FunctionManager.getStatus("Pathfinding")) {
                                    FunctionManager.setStatus("Pathfinding", true);
                                }
                            }
                        }
                    } else {
                        nukerWrapper.disable();
                        if (FunctionManager.getStatus("Pathfinding")) {
                            FunctionManager.setStatus("Pathfinding", false);
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onDisable(CustomEventHandler.FunctionDisabledEvent event) {
        if(event.function.getName().equals("PowderBot")){
            nukerWrapper.enable = false;
            looking = false;
            nukerWrapper.disable();
        }
    }
    @SubscribeEvent
    public void onSwitch(CustomEventHandler.FunctionSwitchEvent event){
        if(event.function.getName().equals("PowderBot")) {
            if (!event.status) {
                nukerWrapper.enable = false;
                looking = false;
                nukerWrapper.disable();
            } else {
                nukerWrapper.enable = true;
                looking = false;
                nukerWrapper.enable();
            }
        }
    }
    @SubscribeEvent
    public void onEnable(CustomEventHandler.FunctionEnableEvent event){
        if(event.function.getName().equals("PowderBot")){
            nukerWrapper.enable = true;
            looking = false;
            nukerWrapper.enable();
        }
    }

    @SubscribeEvent
    public void receivePacket(CustomEventHandler.PacketReceivedEvent event) {
        if (FunctionManager.getStatus("PowderBot")) {
            if (event.packet instanceof S2APacketParticles) {
                S2APacketParticles packet = (S2APacketParticles) event.packet;
                if (packet.getParticleType().equals(EnumParticleTypes.CRIT)) {
                    Vec3 particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
                    if (closestChest != null) {
                        double dist = closestChest.distanceTo(particlePos);
                        if (dist < 1) {
                            looking = true;
                            SmoothRotation.smoothLook(Utils.getRotation(particlePos), 5, () -> {
                            });
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (FunctionManager.getStatus("PowderBot")) {
            closestChest = closestChest();
            if (closestChest != null) {
                Utils.BoxWithESP(new BlockPos(closestChest.xCoord, closestChest.yCoord, closestChest.zCoord), new Color(0, 255, 149), false);
            }
            if(pos != null){
                Utils.BoxWithESP(pos, new Color(0, 244, 253), false);
            }
            BlockPos pos1 = Minecraft.getMinecraft().thePlayer.getPosition().add(2,3,2);
            BlockPos pos2 = Minecraft.getMinecraft().thePlayer.getPosition().add(-2,0,-2);
            Utils.OutlinedBoxWithESP(new AxisAlignedBB(pos1.getX(),pos1.getY(),pos1.getZ(),pos2.getX(),pos2.getY(),pos2.getZ()),new Color(0, 244, 253),false,2.5F);
        }
    }
    @SubscribeEvent
    public void reset(ClientChatReceivedEvent event) {
        if(FunctionManager.getStatus("PowderBot")) {
            if (EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getFormattedText()).contains("You have successfully picked the lock on this chest")) {
                ignoreList.add(closestChest);
                closestChest = null;
                looking = false;
            }
        }
    }
    @SubscribeEvent
    public void WorldChangeTrigger(WorldEvent.Load event) {
        if(FunctionManager.getStatus("PowderBot")) {
            ignoreList.clear();
            new Utils().print("检测到世界服务器改变,自动关闭PowderBot");
            FunctionManager.setStatus("PowderBot",false);
        }
    }

    private static Vec3 closestChest() {
        if(Minecraft.getMinecraft().theWorld != null) {
            int r = 3;
            BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
            playerPos.add(0, 1, 0);
            Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
            Vec3i vec3i = new Vec3i(r, r, r);
            ArrayList<Vec3> chests = new ArrayList<Vec3>();
            if (playerPos != null) {
                for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                    IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                    if (blockState.getBlock() == Blocks.chest) {
                        Vec3 chestVec = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                        if (!isIgnored(chestVec)) {
                            chests.add(chestVec);
                        }
                    }
                }
            }
            double smallest = 9999;
            Vec3 closest = null;
            for (Vec3 chest : chests) {
                double dist = chest.distanceTo(playerVec);
                if (dist < smallest) {
                    smallest = dist;
                    closest = chest;
                }
            }
            return closest;
        } else {
            return null;
        }
    }
    private static Vec3 closestStoneUpperThanPlayer() {
        double smallest = 9999;
        Vec3 closest = null;
        for(BlockPos pos : BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(2,3,2),Minecraft.getMinecraft().thePlayer.getPosition().add(-2,0,-2))){
            if(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.stone){
                Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
                Vec3 vec3 = new Vec3(pos);
                double dist = vec3.distanceTo(playerVec);
                if (dist < smallest) {
                    smallest = dist;
                    closest = vec3;
                }
            }
        }
        return closest;
    }
    private static Vec3 closestStoneUpperThanPlayer(int r) {
        if(Minecraft.getMinecraft().theWorld != null) {
            BlockPos playerPos = Minecraft.getMinecraft().thePlayer.getPosition();
            playerPos.add(0, 1, 0);
            Vec3 playerVec = Minecraft.getMinecraft().thePlayer.getPositionVector();
            Vec3i vec3i = new Vec3i(r, r, r);
            ArrayList<Vec3> stone = new ArrayList<Vec3>();
            if (playerPos != null) {
                for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                    IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
                    if (blockState.getBlock() == Blocks.stone) {
                        Vec3 chestVec = new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                        if (chestVec.yCoord > (Minecraft.getMinecraft().thePlayer.getPositionVector().yCoord - 1)) {
                            stone.add(chestVec);
                        }
                    }
                }
            }
            double smallest = 9999;
            Vec3 closest = null;
            for (Vec3 vec3 : stone) {
                double dist = vec3.distanceTo(playerVec);
                if (dist < smallest) {
                    smallest = dist;
                    closest = vec3;
                }
            }
            return closest;
        } else {
            return null;
        }
    }
    private static boolean isIgnored(Vec3 vec){
        boolean isIgnored = false;
        for(Vec3 ignored : ignoreList){
            if(vecEquals(vec,ignored)){
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }
    private static boolean vecEquals(Vec3 vec1,Vec3 vec2){
        return vec1.xCoord == vec2.xCoord && vec1.yCoord == vec2.yCoord && vec1.zCoord == vec2.zCoord;
    }
    private static boolean hasStone(){
        boolean hasStone = false;
        for(BlockPos pos : BlockPos.getAllInBox(Minecraft.getMinecraft().thePlayer.getPosition().add(2,3,2),Minecraft.getMinecraft().thePlayer.getPosition().add(-2,0,-2))){
            if(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock() == Blocks.stone){
                hasStone = true;
                break;
            }
        }
        return hasStone;
    }

}