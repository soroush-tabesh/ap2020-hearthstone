package ir.soroushtabesh.hearthstone.network;

public interface LazyResult<T> {
    void call(T result);
}
