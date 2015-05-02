package org.iot.dsa.minecraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MinecraftDSLink.MODID, version = MinecraftDSLink.VERSION,
        serverSideOnly = true, acceptableRemoteVersions = "*")
public class MinecraftDSLink {
    public static final String MODID = "MinecraftDSLink";
    public static final String VERSION = "1.0.0";
    private static final DSMEventHandler eventHandler = new DSMEventHandler();
    public static int BLOCKS_BROKEN = 0;
    public static int PLAYERS = 0;
    public static int ENTITIES_KILLED = 0;
    private static Configuration configuration;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();
        configuration.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        String brokerUrl = configuration.getString("brokerUrl", "broker",
                "http://dglux.directcode.org:8025/conn", "Broker URL");
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
        new Thread(new StartupThread(brokerUrl)).start();
    }
}
