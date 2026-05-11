package eu.pb4.placeholders.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

record SimpleServerPlaceholderContext(
        MinecraftServer server,
        CommandSourceStack commandSourceStack,
        ServerLevel serverLevel,
        ServerPlayer serverPlayer,
        Entity entity,
        GameProfile gameProfile,
        PlaceholderContext.ViewObject view
) implements ServerPlaceholderContext {
    @Override
    public HolderLookup.Provider holderLookup() {
        if (this.serverLevel != null) {
            return this.serverLevel.registryAccess();
        }
        return this.server != null ? this.server.registryAccess() : null;
    }

    @Override
    public Level level() {
        return this.serverLevel;
    }

    @Override
    public Player player() {
        return this.serverPlayer;
    }

    @Override
    public BlockPos blockPosition() {
        return this.entity != null ? this.entity.blockPosition() : null;
    }

    @Override
    public Vec3 position() {
        return this.entity != null ? this.entity.position() : null;
    }
}
