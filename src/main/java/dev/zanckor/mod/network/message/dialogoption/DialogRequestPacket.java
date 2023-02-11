package dev.zanckor.mod.network.message.dialogoption;

import dev.zanckor.api.database.LocateHash;
import dev.zanckor.api.dialog.abstractdialog.AbstractDialogOption;
import dev.zanckor.api.dialog.abstractdialog.DialogTemplate;
import dev.zanckor.example.enumregistry.enumdialog.EnumOptionType;
import dev.zanckor.api.quest.register.TemplateRegistry;
import dev.zanckor.mod.util.MCUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

public class DialogRequestPacket {
    EnumOptionType optionType;
    int optionID;

    public DialogRequestPacket(EnumOptionType optionType, int optionID) {
        this.optionType = optionType;
        this.optionID = optionID;
    }

    public DialogRequestPacket(FriendlyByteBuf buffer) {
        optionType = buffer.readEnum(EnumOptionType.class);
        optionID = buffer.readInt();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeEnum(optionType);
        buffer.writeInt(optionID);
    }


    public static void handler(DialogRequestPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            String globalDialogID = LocateHash.currentGlobalDialog.get(player);

            Path path = LocateHash.getDialogLocation(globalDialogID);
            File dialogFile = path.toFile();
            AbstractDialogOption dialogTemplate = TemplateRegistry.getDialogTemplate(msg.optionType);

            try {
                DialogTemplate dialog = MCUtil.getJsonDialog(dialogFile, MCUtil.gson());

                dialogTemplate.handler(ctx.get().getSender(), dialog, msg.optionID);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}