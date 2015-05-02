package org.iot.dsa.minecraft;

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
        DSMResponder.instance().addPlayer(event.player);
    }

    @SubscribeEvent
    public void onLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        DSMResponder.instance().removePlayer(event.player);
    }

}
