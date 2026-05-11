package eu.pb4.placeholders.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface ServerPlaceholderContext extends PlaceholderContext {
    static ServerPlaceholderContext of(MinecraftServer server) {
        return new SimpleServerPlaceholderContext(server, server.createCommandSourceStack(), null, null, null, null, ViewObject.DEFAULT);
    }

    static ServerPlaceholderContext of(ServerLevel level) {
        return new SimpleServerPlaceholderContext(level.getServer(), level.getServer().createCommandSourceStack(), level, null, null, null, ViewObject.DEFAULT);
    }

    static ServerPlaceholderContext of(ServerPlayer player) {
        return new SimpleServerPlaceholderContext(player.getServer(), player.createCommandSourceStack(), player.serverLevel(), player, player, player.getGameProfile(), ViewObject.DEFAULT);
    }

    static ServerPlaceholderContext of(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        GameProfile profile = player != null ? player.getGameProfile() : null;
        return new SimpleServerPlaceholderContext(source.getServer(), source, source.getLevel(), player, source.getEntity(), profile, ViewObject.DEFAULT);
    }

    static ServerPlaceholderContext of(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return of(player);
        }

        ServerLevel level = (ServerLevel) entity.level();
        return new SimpleServerPlaceholderContext(level.getServer(), entity.createCommandSourceStack(), level, null, entity, null, ViewObject.DEFAULT);
    }

    default boolean hasServerPlayer() {
        return this.serverPlayer() != null;
    }

    MinecraftServer server();

    CommandSourceStack commandSourceStack();

    ServerLevel serverLevel();

    ServerPlayer serverPlayer();
}
