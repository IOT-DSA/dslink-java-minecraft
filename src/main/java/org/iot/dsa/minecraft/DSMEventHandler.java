package org.iot.dsa.minecraft;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class DSMEventHandler {

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        MinecraftDSLink.BLOCKS_BROKEN++;
        DSMResponder.instance().updateBlocksBroken();
    }

    @SubscribeEvent
    public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftDSLink.PLAYERS++;
        DSMResponder.instance().updatePlayerCount();
        DSMResponder.instance().updatePlayerList();
    }

    @SubscribeEvent
    public void onLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        MinecraftDSLink.PLAYERS--;
        DSMResponder.instance().updatePlayerCount();
        DSMResponder.instance().updatePlayerList();
    }

    @SubscribeEvent
    public void onEntityKilled(LivingDeathEvent event) {
        MinecraftDSLink.ENTITIES_KILLED++;
        DSMResponder.instance().updateEntitiesKilled();
    }

}
