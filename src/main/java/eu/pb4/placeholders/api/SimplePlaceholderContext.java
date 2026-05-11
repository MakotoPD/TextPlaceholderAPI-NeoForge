package eu.pb4.placeholders.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

record SimplePlaceholderContext(
        Level level,
        Player player,
        Entity entity,
        GameProfile gameProfile,
        PlaceholderContext.ViewObject view,
        BlockPos blockPosition,
        Vec3 position
) implements PlaceholderContext {
    @Override
    public HolderLookup.Provider holderLookup() {
        return this.level != null ? this.level.registryAccess() : null;
    }
}
