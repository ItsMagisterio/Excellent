package dev.luvbeeq.baritone.api.utils;

import dev.luvbeeq.baritone.api.pathing.calc.IPath;

import java.util.Objects;
import java.util.Optional;

public class PathCalculationResult {

    private final IPath path;
    private final Type type;

    public PathCalculationResult(Type type) {
        this(type, null);
    }

    public PathCalculationResult(Type type, IPath path) {
        Objects.requireNonNull(type);
        this.path = path;
        this.type = type;
    }

    public final Optional<IPath> getPath() {
        return Optional.ofNullable(this.path);
    }

    public final Type getType() {
        return this.type;
    }

    public enum Type {
        SUCCESS_TO_GOAL,
        SUCCESS_SEGMENT,
        FAILURE,
        CANCELLATION,
        EXCEPTION,
    }
}
