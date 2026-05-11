package eu.pb4.placeholders.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class PlaceholderResult {
    private final Component component;
    private final boolean valid;

    private PlaceholderResult(Component component, String reason) {
        if (component != null) {
            this.component = component;
            this.valid = true;
        } else {
            String message = reason != null ? reason : "Invalid placeholder!";
            this.component = Component.literal("[" + message + "]")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true));
            this.valid = false;
        }
    }

    public Component component() {
        return this.component;
    }

    public boolean isValid() {
        return this.valid;
    }

    public static PlaceholderResult invalid(String reason) {
        return new PlaceholderResult(null, reason);
    }

    public static PlaceholderResult invalid() {
        return new PlaceholderResult(null, null);
    }

    public static PlaceholderResult value(Component component) {
        return new PlaceholderResult(component, null);
    }

    public static PlaceholderResult value(String value) {
        return new PlaceholderResult(Component.literal(value), null);
    }
}
