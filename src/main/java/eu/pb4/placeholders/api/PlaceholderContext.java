package eu.pb4.placeholders.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface PlaceholderContext {
    static PlaceholderContext of() {
        return new SimplePlaceholderContext(null, null, null, null, ViewObject.DEFAULT, null, null);
    }

    static PlaceholderContext of(Level level) {
        return new SimplePlaceholderContext(level, null, null, null, ViewObject.DEFAULT, null, null);
    }

    static PlaceholderContext of(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return ServerPlaceholderContext.of(player);
        }

        Player player = entity instanceof Player playerEntity ? playerEntity : null;
        GameProfile profile = player != null ? player.getGameProfile() : null;
        return new SimplePlaceholderContext(entity.level(), player, entity, profile, ViewObject.DEFAULT, entity.blockPosition(), entity.position());
    }

    default boolean hasLevel() {
        return this.level() != null;
    }

    default boolean hasPlayer() {
        return this.player() != null;
    }

    default boolean hasGameProfile() {
        return this.gameProfile() != null;
    }

    default boolean hasEntity() {
        return this.entity() != null;
    }

    HolderLookup.Provider holderLookup();

    Level level();

    Player player();

    Entity entity();

    GameProfile gameProfile();

    ViewObject view();

    BlockPos blockPosition();

    Vec3 position();

    interface ViewObject {
        ViewObject DEFAULT = of(ResourceLocation.fromNamespaceAndPath("placeholder_api", "default"));

        static ViewObject of(ResourceLocation identifier) {
            return () -> identifier;
        }

        ResourceLocation identifier();
    }
}
