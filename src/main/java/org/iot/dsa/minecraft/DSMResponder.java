package org.iot.dsa.minecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;
import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.Permission;
import org.dsa.iot.dslink.node.actions.Action;
import org.dsa.iot.dslink.node.actions.ActionResult;
import org.dsa.iot.dslink.node.value.Value;
import org.vertx.java.core.Handler;

public class DSMResponder extends DSLinkHandler {

    private static DSMResponder instance;

    private static Node blocksBrokenNode;
    private static Node playersNode;
    private static Node gamemodeNode;
    private static Node gamemodeIdNode;

    public static DSMResponder instance() {
        if (instance == null) {
            instance = new DSMResponder();
        }
        return instance;
    }

    @Override
    public void onResponderConnected(DSLink link) {
        super.onResponderConnected(link);

        NodeManager manager = link.getNodeManager();
        Node superRoot = manager.getSuperRoot();

        NodeBuilder blocksBroken = superRoot.createChild("blocksBroken");
        blocksBroken.setConfig("type", new Value("int"));
        blocksBrokenNode = blocksBroken.build();

        NodeBuilder players = superRoot.createChild("players");
        playersNode = players.build();

        NodeBuilder gamemode = superRoot.createChild("gamemode");
        gamemode.setConfig("type", new Value("string"));
        gamemodeNode = gamemode.build();

        NodeBuilder gamemodeId = superRoot.createChild("gamemodeId");
        gamemodeId.setConfig("writable", new Value("write"));
        gamemodeIdNode = gamemodeId.build();

        gamemodeId.getListener().setValueHandler(new Handler<Value>() {
            @Override
            public void handle(Value event) {
                if (MinecraftServer.getServer().getGameType().getID() != event.getNumber().intValue()) {
                    MinecraftServer.getServer().setGameType(WorldSettings.GameType.getByID(event.getNumber().intValue()));
                }
            }
        });

        updateBlocksBroken();
        updateGamemode();
    }

    public void updateBlocksBroken() {
        blocksBrokenNode.setValue(new Value(MinecraftDSLink.BLOCKS_BROKEN));
    }

    public void updateGamemode() {
        gamemodeNode.setValue(new Value(MinecraftServer.getServer().getGameType().getName()));
        gamemodeIdNode.setValue(new Value(MinecraftServer.getServer().getGameType().getID()));
    }

    public void addPlayer(final EntityPlayer player) {
        NodeBuilder playernb = playersNode.createChild(player.getDisplayNameString());
        Node playerNode = playernb.build();
        NodeBuilder kicknb = playerNode.createChild("kick");
        kicknb.setAction(new Action(Permission.NONE, new Handler<ActionResult>() {
            @Override
            public void handle(ActionResult event) {
                ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Kicked from server");
            }
        }));
        Node kickNode = kicknb.build();
        playerNode.addChild(kickNode);
        playersNode.addChild(playerNode);
    }

    public void removePlayer(EntityPlayer player) {
        playersNode.removeChild(player.getDisplayNameString());
    }

}
