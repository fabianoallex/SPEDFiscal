package sped.core;

@FunctionalInterface
public interface BuildListner<T> {
    void onBuild(T build);
}
