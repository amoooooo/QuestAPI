package dev.zanckor.example.common.handler.dialogoption;

import com.google.gson.Gson;
import dev.zanckor.api.database.LocateHash;
import dev.zanckor.api.filemanager.dialog.abstractdialog.AbstractDialogOption;
import dev.zanckor.api.filemanager.dialog.ServerDialog;
import dev.zanckor.example.common.enumregistry.enumdialog.EnumOptionType;
import dev.zanckor.api.filemanager.quest.UserQuest;
import dev.zanckor.api.filemanager.quest.ServerQuest;
import dev.zanckor.api.filemanager.quest.abstracquest.AbstractQuestRequirement;
import dev.zanckor.example.common.enumregistry.enumquest.EnumQuestRequirement;
import dev.zanckor.api.filemanager.quest.register.TemplateRegistry;
import dev.zanckor.mod.common.network.ClientHandler;
import dev.zanckor.mod.common.util.GsonManager;
import dev.zanckor.mod.common.util.Timer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.zanckor.example.common.enumregistry.enumquest.EnumQuestType.PROTECT_ENTITY;
import static dev.zanckor.mod.QuestApiMain.*;

public class AddQuestHandler extends AbstractDialogOption {

    /**
     * When player clicks on an option which type is "ADD_QUEST" will try to give it if player has the requirements.
     * In case that player obtains the quest, a new file will be written on his folder data.
     *
     * @param player            The player
     * @param dialog            DialogTemplate class with all dialog data
     * @param option_id         DialogOption ID, Returns the object inside the List< DialogOption >. This is not a parameter inside the .json file
     * @throws IOException      Exception fired when server cannot read json file
     */

    @Override
    public void handler(Player player, ServerDialog dialog, int option_id, Entity npc) throws IOException {
        int currentDialog = LocateHash.currentDialog.get(player);
        ServerDialog.DialogOption option = dialog.getDialog().get(currentDialog).getOptions().get(option_id);
        String quest = option.getQuest_id() + ".json";


        Path userFolder = Paths.get(playerData.toString(), player.getUUID().toString());

        if (option.getType().equals(EnumOptionType.ADD_QUEST.toString())) {
            if (Files.exists(Paths.get(getCompletedQuest(userFolder).toString(), quest)) || Files.exists(Paths.get(getActiveQuest(userFolder).toString(), quest)) || Files.exists(Paths.get(getUncompletedQuest(userFolder).toString(), quest))) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.closeDialog());

                return;
            }


            for (File file : serverQuests.toFile().listFiles()) {
                Path path = Paths.get(getActiveQuest(userFolder).toString(), "\\", file.getName());

                if (file.getName().equals(quest)) {
                    ServerQuest serverQuest = (ServerQuest) GsonManager.getJson(file, ServerQuest.class);
                    AbstractQuestRequirement requirement = TemplateRegistry.getQuestRequirement(EnumQuestRequirement.valueOf(serverQuest.getRequirements_type()));

                    if (!requirement.handler(player, serverQuest)) {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.closeDialog());
                        return;
                    }


                    FileWriter writer = new FileWriter(path.toFile());
                    UserQuest playerQuest = UserQuest.createQuest(serverQuest, path);
                    GsonManager.gson().toJson(playerQuest, writer);
                    writer.close();

                    if (playerQuest.hasTimeLimit()) {
                        Timer.updateCooldown(player.getUUID(), option.getQuest_id(), playerQuest.getTimeLimitInSeconds());
                    }

                    if (playerQuest.getQuest_type().equals(PROTECT_ENTITY.toString())) {
                        protectEntityQuest(playerQuest, player.level, player, serverQuest, path, GsonManager.gson(), option.getQuest_id());
                    }

                    LocateHash.registerQuestByID(option.getQuest_id(), path);
                    break;
                }
            }
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.closeDialog());
    }


    private static void protectEntityQuest(UserQuest playerQuest, Level level, Player player, ServerQuest serverQuest, Path path, Gson gson, String questID) throws IOException {
        EntityType entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(playerQuest.getQuest_target().get(0)));
        UUID playerUUID = player.getUUID();

        Entity entity = entityType.create(level);
        entity.setPos(player.getPosition(0));

        level.addFreshEntity(entity);

        FileWriter protectEntityWriter = new FileWriter(path.toFile());
        UserQuest protectEntityPlayerQuest = UserQuest.createQuest(serverQuest, path);
        List<String> list = new ArrayList<>();

        list.add(entity.getUUID().toString());

        protectEntityPlayerQuest.setQuest_target(list);
        gson.toJson(protectEntityPlayerQuest, protectEntityWriter);
        protectEntityWriter.close();

        Timer.updateCooldown(playerUUID, questID, playerQuest.getTimeLimitInSeconds());
    }
}