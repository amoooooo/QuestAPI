package com.zanckor.mod;

import com.mojang.logging.LogUtils;
import com.zanckor.example.ModExample;
import com.zanckor.mod.network.QuestNetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Mod(QuestApiMain.MOD_ID)
public class QuestApiMain {
    public static final String MOD_ID = "questapi";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Path serverDirectory, questApi, playerData, serverQuests;

    public QuestApiMain() {
        QuestNetworkHandler.register();
        ModExample.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }


    public static Path getUserFolder(UUID playerUUID) {
        Path userFolder = Paths.get(playerData.toString(), playerUUID.toString());

        return userFolder;
    }

    public static Path getActiveQuest(Path userFolder) {
        Path activeQuest = Paths.get(userFolder.toString(), "active-quests");

        return activeQuest;
    }

    public static Path getCompletedQuest(Path userFolder) {
        Path completedQuest = Paths.get(userFolder.toString(), "completed-quests");

        return completedQuest;
    }

    public static Path getUncompletedQuest(Path userFolder) {
        Path uncompletedQuest = Paths.get(userFolder.toString(), "uncompleted-quests");

        return uncompletedQuest;
    }
}