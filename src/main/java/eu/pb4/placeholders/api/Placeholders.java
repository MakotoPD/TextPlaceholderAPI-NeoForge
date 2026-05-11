package eu.pb4.placeholders.api;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class Placeholders {
    private static final Map<ResourceLocation, Placeholder<PlaceholderContext, ?>> COMMON_PLACEHOLDERS = new HashMap<>();
    private static final Map<ResourceLocation, Placeholder<ServerPlaceholderContext, ?>> SERVER_PLACEHOLDERS = new HashMap<>();

    private Placeholders() {
    }

    public static PlaceholderResult parseServerPlaceholder(ResourceLocation identifier, String argument, ServerPlaceholderContext context) {
        Placeholder<ServerPlaceholderContext, ?> placeholder = getServerPlaceholder(identifier);
        return placeholder != null ? placeholder.onPlaceholderRequest(context, argument) : PlaceholderResult.invalid("Placeholder doesn't exist!");
    }

    public static PlaceholderResult parseCommonPlaceholder(ResourceLocation identifier, String argument, PlaceholderContext context) {
        Placeholder<PlaceholderContext, ?> placeholder = getCommonPlaceholder(identifier);
        return placeholder != null ? placeholder.onPlaceholderRequest(context, argument) : PlaceholderResult.invalid("Placeholder doesn't exist!");
    }

    public static Placeholder<PlaceholderContext, ?> getCommonPlaceholder(ResourceLocation identifier) {
        return COMMON_PLACEHOLDERS.get(identifier);
    }

    public static Placeholder<ServerPlaceholderContext, ?> getServerPlaceholder(ResourceLocation identifier) {
        return SERVER_PLACEHOLDERS.get(identifier);
    }

    public static <T> void registerServer(ResourceLocation identifier, Placeholder.Handler<ServerPlaceholderContext, String> handler) {
        registerServer(identifier, ArgumentParser.STRING, handler);
    }

    public static <T> void registerServer(ResourceLocation identifier, ArgumentParser<T> argumentParser, Placeholder.Handler<ServerPlaceholderContext, T> handler) {
        registerServer(new Placeholder<>(identifier, argumentParser, handler));
    }

    public static void registerServer(Placeholder<ServerPlaceholderContext, ?> placeholder) {
        SERVER_PLACEHOLDERS.put(placeholder.identifier(), placeholder);
    }

    public static <T> void registerCommon(ResourceLocation identifier, Placeholder.Handler<PlaceholderContext, String> handler) {
        registerCommon(identifier, ArgumentParser.STRING, handler);
    }

    public static <T> void registerCommon(ResourceLocation identifier, ArgumentParser<T> argumentParser, Placeholder.Handler<PlaceholderContext, T> handler) {
        registerCommon(new Placeholder<>(identifier, argumentParser, handler));
    }

    public static void registerCommon(Placeholder<PlaceholderContext, ?> placeholder) {
        COMMON_PLACEHOLDERS.put(placeholder.identifier(), placeholder);
    }

    public static ImmutableMap<ResourceLocation, Placeholder<PlaceholderContext, ?>> getCommonPlaceholders() {
        return ImmutableMap.copyOf(COMMON_PLACEHOLDERS);
    }

    public static ImmutableMap<ResourceLocation, Placeholder<ServerPlaceholderContext, ?>> getServerPlaceholders() {
        return ImmutableMap.copyOf(SERVER_PLACEHOLDERS);
    }

    public static Component parseText(String text, ServerPlaceholderContext context) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        MutableComponent output = Component.empty();
        int cursor = 0;
        while (cursor < text.length()) {
            int start = text.indexOf('%', cursor);
            if (start < 0) {
                output.append(text.substring(cursor));
                break;
            }

            if (start > cursor) {
                output.append(text.substring(cursor, start));
            }

            if (start + 1 < text.length() && text.charAt(start + 1) == '%') {
                output.append("%");
                cursor = start + 2;
                continue;
            }

            int end = text.indexOf('%', start + 1);
            if (end < 0) {
                output.append(text.substring(start));
                break;
            }

            String token = text.substring(start + 1, end);
            ParsedToken parsed = ParsedToken.parse(token);
            if (parsed != null) {
                output.append(parseServerPlaceholder(parsed.identifier(), parsed.argument(), context).component());
            } else {
                output.append(text.substring(start, end + 1));
            }
            cursor = end + 1;
        }
        return output;
    }

    private record ParsedToken(ResourceLocation identifier, String argument) {
        private static ParsedToken parse(String token) {
            if (token == null || token.isBlank()) {
                return null;
            }

            String id = token;
            String argument = "";
            int slash = token.indexOf('/');
            if (slash >= 0) {
                id = token.substring(0, slash);
                argument = token.substring(slash + 1);
            }

            ResourceLocation identifier = ResourceLocation.tryParse(id);
            return identifier != null ? new ParsedToken(identifier, argument) : null;
        }
    }
}
