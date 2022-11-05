package com.greencat;

import com.greencat.common.Chat.AntimonyChannel;
import com.greencat.common.Chat.CheckConnect;
import com.greencat.common.Chat.ReadFromServer;
import com.greencat.common.EventLoader;
import com.greencat.common.FunctionManager.FunctionManager;
import com.greencat.common.MainMenu.GuiMainMenuModify;
import com.greencat.common.command.ChatCommand;
import com.greencat.common.command.CommandManager;
import com.greencat.common.command.DevCommand;
import com.greencat.common.config.ConfigLoader;
import com.greencat.common.config.getConfigByFunctionName;
import com.greencat.common.event.CustomEventHandler;
import com.greencat.common.function.*;
import com.greencat.common.function.rank.CustomRank;
import com.greencat.common.function.rank.RankList;
import com.greencat.common.function.title.TitleManager;
import com.greencat.common.key.KeyLoader;
import com.greencat.common.register.AntimonyRegister;
import com.greencat.core.HUDManager;
import com.greencat.core.Pathfinding;
import com.greencat.settings.*;
import com.greencat.type.AntimonyFunction;
import com.greencat.type.SelectObject;
import com.greencat.type.SelectTable;
import com.greencat.utils.Blur;
import com.greencat.utils.Chroma;
import com.greencat.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;


import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Mod(modid = Antimony.MODID, name = Antimony.NAME, version = Antimony.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Antimony {
    public static final String MODID = "antimony";
    public static final String NAME = "Antimony-Client";
    public static final String VERSION = "3.0.3";
    private static final String Sb = "Sb";

    public static float strafe;
    public static float forward;
    public static float friction;

    public static boolean AutoFishYawState = false;
    public static int ImageScaling = 1;
    public static boolean shouldRenderBossBar = true;
    public static int Color = 16542622;
    public static File AntimonyDirectory = new File(System.getProperty("user.dir") + "\\Antimony\\");
    public static String PresentGUI = "root";
    public static String PresentFunction = "";
    public static HashMap<String, String> GroundDecorateList = new HashMap<String, String>();
    Utils utils = new Utils();
    public static Boolean LabymodInstallCheck;

    @Instance(Antimony.MODID)
    public static Antimony instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        // TODO

        new com.greencat.common.config.ConfigLoader(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) throws AWTException {
        // TODO
        new EventLoader();
        new KeyLoader();
        if (!AntimonyDirectory.exists()) {
            AntimonyDirectory.mkdir();
        }
        ClientCommandHandler.instance.registerCommand(new CommandManager());
        ClientCommandHandler.instance.registerCommand(new DevCommand());
        ClientCommandHandler.instance.registerCommand(new ChatCommand());
        LabymodInstallCheck = utils.ModLoadCheck("labymod");

        if (Minecraft.getMinecraft().gameSettings.gammaSetting > 1) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = 0;
        }

        //init GroundDecorate
        try {
            String content = "";
            URL url = new URL("https://itzgreencat.github.io/AntimonyDecorate/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String input;
            while ((input = reader.readLine()) != null) {
                content += input;
            }
            reader.close();
            GroundDecorateList.clear();
            for (String str : content.split(";")) {
                GroundDecorateList.put(str.split(",")[0], str.split(",")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //init CustomItemName
        try {
            String content = "";
            URL url = new URL("https://itzgreencat.github.io/AntimonyCustomItemName/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String input;
            while ((input = reader.readLine()) != null) {
                content += input;
            }
            reader.close();
            CustomItemName.CustomName.clear();
            for (String str : content.split(";")) {
                CustomItemName.CustomName.put(str.split(",")[0], str.split(",")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomEventHandler.EVENT_BUS.register(new Utils());


        //Misc
        new Chroma();
        new GuiMainMenuModify();
        new CustomEventHandler.ClientTickEndEvent();
        new HUDManager();
        new Pathfinding();

        //Dev
        //Function
        new AutoFish();
        new AutoKillWorm();
        new WormLavaESP();
        new SilverfishESP();
        new GolemESP();
        new GuardianESP();
        new GemstoneHidePane();
        new FullBright();
        new StarredMobESP();
        new DungeonKeyESP();
        new CustomPetName();
        new CustomItemSound();
        new LanternESP();
        new SkeletonAim();
        new TitleManager();
        new AntiAFKJump();
        new Sprint();
        new Eagle();
        new Velocity();
        new ItemTranslate();
        new GhostBlock();
        new InstantSwitch();
        new AutoClicker();
        new TerminalESP();
        new HideDungeonMobNameTag();
        new PlayerFinder();
        new SecretBot();
        new LividESP();
        new AutoCannon();
        new DroppedItemESP();
        new MouseISwitch();
        new Rat();
        new Killaura();
        new AutoUse();
        new HollowAutoPurchase();
        new WTap();
        new FreeCamera();
        new TargetESP();
        new Cartoon();
        new Interface();
        new ShortBowAura();
        new HideFallingBlock();
        new AutoWolfSlayer();


        Blur.register();

        new com.greencat.common.decorate.Events();

        new AntimonyChannel();
        new ReadFromServer();
        new CheckConnect();

        new RankList();
        new CustomRank();

        AntimonyRegister register = new AntimonyRegister();
        register.RegisterFunction(new AntimonyFunction("AutoClicker"));
        register.RegisterFunction(new AntimonyFunction("Killaura"));
        register.RegisterFunction(new AntimonyFunction("Reach"));
        register.RegisterFunction(new AntimonyFunction("WTap"));
        register.RegisterFunction(new AntimonyFunction("FreeCamera"));
        register.RegisterFunction(new AntimonyFunction("TargetESP"));
        register.RegisterFunction(new AntimonyFunction("WormLavaESP"));
        register.RegisterFunction(new AntimonyFunction("LanternESP"));
        register.RegisterFunction(new AntimonyFunction("SilverfishESP"));
        register.RegisterFunction(new AntimonyFunction("GolemESP"));
        register.RegisterFunction(new AntimonyFunction("GuardianESP"));
        register.RegisterFunction(new AntimonyFunction("AutoFish"));
        register.RegisterFunction(new AntimonyFunction("AutoKillWorm"));
        register.RegisterFunction(new AntimonyFunction("GemstoneHidePane"));
        register.RegisterFunction(new AntimonyFunction("FullBright"));
        register.RegisterFunction(new AntimonyFunction("StarredMobESP"));
        register.RegisterFunction(new AntimonyFunction("DungeonKeyESP"));
        register.RegisterFunction(new AntimonyFunction("CustomPetNameTag"));
        register.RegisterFunction(new AntimonyFunction("CustomItemSound"));
        register.RegisterFunction(new AntimonyFunction("SkeletonAim"));
        register.RegisterFunction(new AntimonyFunction("AntiAFKJump"));
        register.RegisterFunction(new AntimonyFunction("Sprint"));
        register.RegisterFunction(new AntimonyFunction("Eagle"));
        register.RegisterFunction(new AntimonyFunction("Velocity"));
        register.RegisterFunction(new AntimonyFunction("AutoCannon"));
        register.RegisterFunction(new AntimonyFunction("ItemTranslate"));
        register.RegisterFunction(new AntimonyFunction("HUD"));
        register.RegisterFunction(new AntimonyFunction("GhostBlock"));
        register.RegisterFunction(new AntimonyFunction("InstantSwitch"));
        register.RegisterFunction(new AntimonyFunction("NoHurtCam"));
        register.RegisterFunction(new AntimonyFunction("NoSlow"));
        register.RegisterFunction(new AntimonyFunction("TerminalESP"));
        register.RegisterFunction(new AntimonyFunction("HideDungeonMobNameTag"));
        register.RegisterFunction(new AntimonyFunction("LividESP"));
        register.RegisterFunction(new AntimonyFunction("PlayerFinder"));
        register.RegisterFunction(new AntimonyFunction("SecretBot"));
        register.RegisterFunction(new AntimonyFunction("DroppedItemESP"));
        register.RegisterFunction(new AntimonyFunction("MouseISwitch"));
        register.RegisterFunction(new AntimonyFunction("AutoUse"));
        register.RegisterFunction(new AntimonyFunction("Cartoon"));
        register.RegisterFunction(new AntimonyFunction("HollowAutoPurchase"));
        register.RegisterFunction(new AntimonyFunction("AntimonyChannel"));
        register.RegisterFunction(new AntimonyFunction("Rat"));
        register.RegisterFunction(new AntimonyFunction("Interface"));
        register.RegisterFunction(new AntimonyFunction("ShortBowAura"));
        register.RegisterFunction(new AntimonyFunction("HideFallingBlock"));
        register.RegisterFunction(new AntimonyFunction("Pathfinding"));
        register.RegisterFunction(new AntimonyFunction("AutoWolfSlayer"));


        register.RegisterTable(new SelectTable("root"));
        register.RegisterTable(new SelectTable("Combat"));
        register.RegisterTable(new SelectTable("Render"));
        register.RegisterTable(new SelectTable("Dungeon"));
        register.RegisterTable(new SelectTable("Macro"));
        register.RegisterTable(new SelectTable("CrystalHollow"));
        register.RegisterTable(new SelectTable("Movement"));
        register.RegisterTable(new SelectTable("Misc"));
        register.RegisterTable(new SelectTable("Fun"));


        register.RegisterSelectObject(new SelectObject("table", "Combat", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Render", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Dungeon", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Macro", "root"));
        register.RegisterSelectObject(new SelectObject("table", "CrystalHollow", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Movement", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Misc", "root"));
        register.RegisterSelectObject(new SelectObject("table", "Fun", "root"));

        register.RegisterSelectObject(new SelectObject("function", "AutoClicker", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "AutoCannon", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "TargetESP", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "NoSlow", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Killaura", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "ShortBowAura", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "Reach", "Combat"));
        register.RegisterSelectObject(new SelectObject("function", "WTap", "Combat"));

        register.RegisterSelectObject(new SelectObject("function", "SilverfishESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GuardianESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "GolemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "WormLavaESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "LanternESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "PlayerFinder", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FullBright", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "DroppedItemESP", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "NoHurtCam", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "HideFallingBlock", "Render"));
        register.RegisterSelectObject(new SelectObject("function", "FreeCamera", "Render"));

        register.RegisterSelectObject(new SelectObject("function", "StarredMobESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "DungeonKeyESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "TerminalESP", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "HideDungeonMobNameTag", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "SecretBot", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "GhostBlock", "Dungeon"));
        register.RegisterSelectObject(new SelectObject("function", "LividESP", "Dungeon"));

        register.RegisterSelectObject(new SelectObject("function", "AutoFish", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoKillWorm", "Macro"));
        register.RegisterSelectObject(new SelectObject("function", "AutoWolfSlayer", "Macro"));

        register.RegisterSelectObject(new SelectObject("function", "GemstoneHidePane", "CrystalHollow"));
        register.RegisterSelectObject(new SelectObject("function", "HollowAutoPurchase", "CrystalHollow"));

        register.RegisterSelectObject(new SelectObject("function", "AntiAFKJump", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Sprint", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Eagle", "Movement"));
        register.RegisterSelectObject(new SelectObject("function", "Velocity", "Movement"));


        register.RegisterSelectObject(new SelectObject("function", "SkeletonAim", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "AntimonyChannel", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "InstantSwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "MouseISwitch", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "AutoUse", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "Interface", "Misc"));
        register.RegisterSelectObject(new SelectObject("function", "HUD", "Misc"));

        register.RegisterSelectObject(new SelectObject("function", "CustomPetNameTag", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "CustomItemSound", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Cartoon", "Fun"));
        register.RegisterSelectObject(new SelectObject("function", "Rat", "Fun"));


        register.RegisterSelectObject(new SelectObject("function", "ItemTranslate", "root"));

        AntimonyRegister.ReList();

        ConfigLoader.applyFunctionState();

        FunctionManager.setStatus("CustomPetNameTag", false);
        FunctionManager.setStatus("Pathfinding", false);
        FunctionManager.setStatus("Interface", true);

        FunctionManager.bindFunction("Killaura");
        FunctionManager.addConfiguration(new SettingBoolean("攻击玩家", "isAttackPlayer", true));
        FunctionManager.addConfiguration(new SettingBoolean("目标实体透视", "targetESP", true));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大纵向旋转角度", "maxPitch", 120.0D,90.0D,-90.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大横向旋转角度", "maxYaw", 120.0D,180.0D,-180.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("最大旋转距离", "maxRotationRange", 6.0D,12.0D,2.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("在此项值内视场角生物为可攻击生物", "Fov", 270.0D,360.0D,90.0D));
        FunctionManager.addConfiguration(new SettingBoolean("攻击NPC", "isAttackNPC", false));
        FunctionManager.addConfiguration(new SettingBoolean("攻击同队伍玩家", "isAttackTeamMember", false));

        FunctionManager.bindFunction("InstantSwitch");
        FunctionManager.addConfiguration(new SettingString("物品名称", "itemName", "of the End"));
        FunctionManager.addConfiguration(new SettingBoolean("左键模式", "leftClick", false));

        FunctionManager.bindFunction("AutoUse");
        FunctionManager.addConfiguration(new SettingLimitInt("间隔时间","cooldown",10,Integer.MAX_VALUE,0));
        FunctionManager.addConfiguration(new SettingString("物品名称(部分匹配,无需写全名)","itemName","of the End"));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(Y)", "timerY", 130));

        FunctionManager.bindFunction("Reach");
        FunctionManager.addConfiguration(new SettingLimitDouble("距离","distance",3.0D,4.0D,3.0D));

        FunctionManager.bindFunction("AutoFish");
        FunctionManager.addConfiguration(new SettingBoolean("SlugFish模式", "slug", false));
        FunctionManager.addConfiguration(new SettingBoolean("状态提示", "message", true));
        FunctionManager.addConfiguration(new SettingBoolean("显示抛竿计时器", "timer", true));
        FunctionManager.addConfiguration(new SettingBoolean("强制潜行", "sneak", false));
        FunctionManager.addConfiguration(new SettingInt("抛竿计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("抛竿计时器位置(Y)", "timerY", 100));

        FunctionManager.bindFunction("WTap");
        FunctionManager.addConfiguration(new SettingBoolean("对弓的支持", "bowMode", true));

        HashMap<String, Integer> HUDTypeMap = new HashMap<String, Integer>();
        HUDTypeMap.put("Classic",0);
        HUDTypeMap.put("White",1);
        HUDTypeMap.put("Transparent",2);
        FunctionManager.bindFunction("HUD");
        FunctionManager.addConfiguration(new SettingInt("左上方(SelectGUI)距屏幕顶部距离","HUDHeight",0));
        FunctionManager.addConfiguration(new SettingInt("右上方(FunctionList)距屏幕顶部距离","FunctionListHeight",0));
        FunctionManager.addConfiguration(new SettingTypeSelector("HUD样式","style",2,HUDTypeMap));

        FunctionManager.bindFunction("CustomPetNameTag");
        FunctionManager.addConfiguration(new SettingString("规则为\"原始字符=要替换的字符\",如果添加多项规则请使用\",\"分割,如果想清空自定义名称规则请填写null", "petName", "null"));
        FunctionManager.addConfiguration(new SettingInt("宠物等级,如果想取消自定义等级请填写0","petLevel",0));

        FunctionManager.bindFunction("MouseISwitch");
        FunctionManager.addConfiguration(new SettingString("物品名称", "itemName", "Shadow Fu"));
        FunctionManager.addConfiguration(new SettingBoolean("左键触发", "leftTrigger", true));

        FunctionManager.bindFunction("AutoKillWorm");
        FunctionManager.addConfiguration(new SettingLimitInt("间隔时间","cooldown",300,Integer.MAX_VALUE,0));
        FunctionManager.addConfiguration(new SettingBoolean("瞄准Worm", "aim", false));
        FunctionManager.addConfiguration(new SettingString("右键物品名称", "itemName", "staff of the vol"));
        FunctionManager.addConfiguration(new SettingInt("右键次数", "rcCount", 1));
        FunctionManager.addConfiguration(new SettingInt("右键延迟(游戏刻)", "rcCooldown", 10));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(X)", "timerX", 200));
        FunctionManager.addConfiguration(new SettingInt("计时器位置(Y)", "timerY", 115));

        FunctionManager.bindFunction("AntimonyChannel");
        FunctionManager.addConfiguration(new SettingBoolean("重连提示", "notice", true));

        FunctionManager.bindFunction("NoSlow");
        FunctionManager.addConfiguration(new SettingLimitDouble("剑格挡减速效果","sword",0.5D,1.0D,0.2D));
        FunctionManager.addConfiguration(new SettingLimitDouble("拉弓减速效果","bow",1.0D,1.0D,0.2D));
        FunctionManager.addConfiguration(new SettingLimitDouble("进食减速效果","eat",1.0D,1.0D,0.2D));

        HashMap<String, Integer> BackgroundStyleMap = new HashMap<String, Integer>();
        BackgroundStyleMap.put("Vanilla",0);
        BackgroundStyleMap.put("Blur",1);
        FunctionManager.bindFunction("Interface");
        FunctionManager.addConfiguration(new SettingBoolean("FPS显示", "fps", false));
        FunctionManager.addConfiguration(new SettingInt("FPS位置(X)", "fpsX", 200));
        FunctionManager.addConfiguration(new SettingInt("FPS位置(Y)", "fpsY", 130));
        FunctionManager.addConfiguration(new SettingBoolean("坐标显示", "location", false));
        FunctionManager.addConfiguration(new SettingInt("坐标位置(X)", "locationX", 200));
        FunctionManager.addConfiguration(new SettingInt("坐标位置(Y)", "locationY", 145));
        FunctionManager.addConfiguration(new SettingBoolean("服务器天数显示", "day", false));
        FunctionManager.addConfiguration(new SettingInt("天数位置(X)", "dayX", 200));
        FunctionManager.addConfiguration(new SettingInt("天数位置(Y)", "dayY", 190));
        FunctionManager.addConfiguration(new SettingTypeSelector("GUI背景样式","bgstyle",1,BackgroundStyleMap));

        FunctionManager.bindFunction("ShortBowAura");
        FunctionManager.addConfiguration(new SettingLimitDouble("延迟", "delay", 3.0D,10.0D,1.0D));
        FunctionManager.addConfiguration(new SettingLimitDouble("距离", "range", 15.0D,100.0D,5.0D));
        FunctionManager.addConfiguration(new SettingBoolean("右键模式", "right", false));
        FunctionManager.addConfiguration(new SettingBoolean("攻击同队成员", "attackTeam", false));

        FunctionManager.bindFunction("AutoWolfSlayer");
        HashMap<String, Integer> SlayMode = new HashMap<String, Integer>();
        SlayMode.put("Killaura",0);
        SlayMode.put("ShortBowAura",1);
        FunctionManager.addConfiguration(new SettingTypeSelector("击杀Wolf模式","mode",0,SlayMode));
        FunctionManager.addConfiguration(new SettingString("近战物品名称", "swordName", "livid"));
        FunctionManager.addConfiguration(new SettingString("短弓名称", "bowName", "juju"));

        NewUserFunction();

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    public void NewUserFunction() {
        if (ConfigLoader.getBoolean("newUser", true)) {
            FunctionManager.setStatus("HUD", true);
            FunctionManager.setStatus("AntimonyChannel", true);

            ConfigLoader.setBoolean("newUser", false, true);
        }
    }
    public static BlockPos getEntityPos(Entity entity){
        return new BlockPos(entity.posX,entity.posY,entity.posZ);
    }
}
