package com.whitaker.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Either<T, S> {

  private final T left;
  private final S right;

  private Either(T left, S right) {
    this.left = left;
    this.right = right;
  }

  public static <X, Y> Either<X, Y> ofLeft(X left) {
    return new Either(left, null);
  }

  public static <X, Y> Either<X, Y> ofRight(Y right) {
    return new Either(null, right);
  }

  public Optional<T> getLeft() {
    return Optional.ofNullable(left);
  }

  public Optional<S> getRight() {
    return Optional.ofNullable(right);
  }

  public <Y> Optional<Y> mapLeft(Function<T, Y> mapFunction) {
    return getLeft()
      .map(mapFunction);
  }

  public <X> Optional<X> mapRight(Function<S, X> mapFunction) {
    return getRight()
      .map(mapFunction);
  }

  public <X> X mapLeftOrRight(Function<T, X> leftFunction, Function<S, X> rightFunction) {
    return getLeft()
      .map(leftFunction)
      .or(() -> getRight().map(rightFunction))
      .orElseThrow();
  }

  public void ifLeft(Consumer<T> leftConsumer) {
    getLeft().ifPresent(leftConsumer);
  }

  public void ifRight(Consumer<S> rightConsumer) {
    getRight().ifPresent(rightConsumer);
  }

  public void ifLeftOrRight(Consumer<T> leftConsumer, Consumer<S> rightConsumer) {
    ifLeft(leftConsumer);
    ifRight(rightConsumer);
  }
}
