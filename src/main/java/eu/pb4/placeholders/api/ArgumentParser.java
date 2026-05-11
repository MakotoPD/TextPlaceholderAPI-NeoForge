package eu.pb4.placeholders.api;

@FunctionalInterface
public interface ArgumentParser<T> {
    ArgumentParser<String> STRING = argument -> argument == null ? "" : argument;

    T parseArgument(String argument);
}
