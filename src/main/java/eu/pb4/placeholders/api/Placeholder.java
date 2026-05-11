package eu.pb4.placeholders.api;

import net.minecraft.resources.ResourceLocation;

public record Placeholder<Ctx, T>(ResourceLocation identifier, ArgumentParser<T> argumentParser, Handler<Ctx, T> handler) {
    public PlaceholderResult onPlaceholderRequest(Ctx context, String argument) {
        return this.handler.onPlaceholderRequest(context, this.argumentParser.parseArgument(argument));
    }

    public Placeholder<Ctx, T> withId(ResourceLocation identifier) {
        return new Placeholder<>(identifier, this.argumentParser, this.handler);
    }

    @FunctionalInterface
    public interface Handler<Ctx, ArgType> {
        PlaceholderResult onPlaceholderRequest(Ctx context, ArgType argument);
    }
}
