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

  public <X> X mapLeftOrRight(Function<T, X> leftFunction, Function<S, X> rightFunction) {
    return mapLeft(leftFunction)
      .or(() -> mapRight(rightFunction))
      .orElseThrow();
  }

  public <X, Y> Either<X, Y> map(Function<T, X> leftFunction, Function<S, Y> rightFunction) {
    return mapLeft(leftFunction)
      .<Either<X, Y>>map(Either::ofLeft)
      .or(() -> mapRight(rightFunction)
          .map(Either::ofRight))
      .orElseThrow();
  }

  public <X, Y> Either<X, Y> flatMap(Function<T, Either<X, Y>> leftFunction, Function<S, Either<X, Y>> rightFunction) {
    return mapLeft(leftFunction)
      .or(() -> mapRight(rightFunction))
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

  private Optional<T> getLeft() {
    return Optional.ofNullable(left);
  }

  private Optional<S> getRight() {
    return Optional.ofNullable(right);
  }

  private <Y> Optional<Y> mapLeft(Function<T, Y> mapFunction) {
    return getLeft()
      .map(mapFunction);
  }

  private <X> Optional<X> mapRight(Function<S, X> mapFunction) {
    return getRight()
      .map(mapFunction);
  }
}
