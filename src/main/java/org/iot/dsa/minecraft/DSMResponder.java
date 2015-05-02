package org.iot.dsa.minecraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings;
import org.dsa.iot.dslink.DSLink;
import org.dsa.iot.dslink.DSLinkHandler;
import org.dsa.iot.dslink.node.Node;
import org.dsa.iot.dslink.node.NodeBuilder;
import org.dsa.iot.dslink.node.NodeManager;
import org.dsa.iot.dslink.node.value.Value;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;

public class DSMResponder extends DSLinkHandler {

    private static DSMResponder instance;

    private static Node blocksBrokenNode;
    private static Node playerCountNode;
    private static Node playerListNode;
    private static Node gamemodeNode;
    private static Node gamemodeIdNode;
    private static Node entitiesKilledNode;

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

        NodeBuilder playerCount = superRoot.createChild("playerCount");
        playerCount.setConfig("type", new Value("int"));
        playerCountNode = playerCount.build();

        NodeBuilder playerList = superRoot.createChild("playerList");
        playerList.setConfig("type", new Value("array"));
        playerListNode = playerList.build();

        NodeBuilder gamemode = superRoot.createChild("gamemode");
        gamemode.setConfig("type", new Value("string"));
        gamemodeNode = gamemode.build();

        NodeBuilder gamemodeId = superRoot.createChild("gamemodeId");
        gamemode.setConfig("writable", new Value("write"));
        gamemodeIdNode = gamemodeId.build();

        /*NodeBuilder entitiesKilled = superRoot.createChild("entitiesKilled");
        entitiesKilled.setConfig("type", new Value("int"));
        entitiesKilledNode = entitiesKilled.build();*/

        gamemodeId.getListener().setValueHandler(new Handler<Value>() {
            @Override
            public void handle(Value event) {
                if (MinecraftServer.getServer().getGameType().getID() != event.getNumber().intValue()) {
                    MinecraftServer.getServer().setGameType(WorldSettings.GameType.getByID(event.getNumber().intValue()));
                }
            }
        });

        updateBlocksBroken();
        updatePlayerCount();
        updatePlayerList();
        updateGamemode();
        //updateEntitiesKilled();
    }

    public void updateBlocksBroken() {
        blocksBrokenNode.setValue(new Value(MinecraftDSLink.BLOCKS_BROKEN));
    }

    public void updatePlayerCount() {
        playerCountNode.setValue(new Value(MinecraftDSLink.PLAYERS));
    }

    public void updatePlayerList() {
        String[] playerList = new String[MinecraftServer.getServer().getGameProfiles().length];
        for (int i = 0; i < MinecraftServer.getServer().getGameProfiles().length; i++) {
            playerList[i] = MinecraftServer.getServer().getGameProfiles()[i].getName();
        }
        playerListNode.setValue(new Value(new JsonArray(playerList)));
    }

    public void updateGamemode() {
        gamemodeNode.setValue(new Value(MinecraftServer.getServer().getGameType().getName()));
        gamemodeIdNode.setValue(new Value(MinecraftServer.getServer().getGameType().getID()));
    }

    public void updateEntitiesKilled() {
        //entitiesKilledNode.setValue(new Value(MinecraftDSLink.ENTITIES_KILLED));
    }
}
