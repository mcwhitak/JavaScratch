package com.whitaker.bioptional;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class BiOptional<T, S> {

  private final Optional<T> left;
  private final Optional<S> right;

  private BiOptional(Optional<T> left, Optional<S> right) {
    this.left = left;
    this.right = right;
  }

  public static <X, Y> BiOptional<X, Y> from(Optional<X> left, Optional<Y> right) {
    return new BiOptional<>(left, right);
  }

  public static <X, Y> BiOptional<X, Y> ofNullable(X left, Y right) {
    return new BiOptional<>(Optional.ofNullable(left), Optional.ofNullable(right));
  }

  public static <X, Y> BiOptional<X, Y> empty() {
    return new BiOptional(Optional.empty(), Optional.empty());
  }

  public boolean isPresent() {
    return left.isPresent() && right.isPresent();
  }

  public void ifPresent(BiConsumer<T, S> consumer) {
    if (isPresent()) {
      consumer.accept(left.get(), right.get());
    }
  }

  public void ifPresentOrElse(BiConsumer<T, S> consumer, Runnable emptyAction) {
    if (isPresent()) {
      consumer.accept(left.get(), right.get());
    } else {
      emptyAction.run();
    }
  }

  public <X> Optional<X> map(BiFunction<T, S, X> function) {
    if (isPresent()) {
      return Optional.ofNullable(function.apply(left.get(), right.get()));
    }

    return Optional.empty();
  }

  public <X> Optional<X> flatMap(BiFunction<T, S, Optional<X>> function) {
    if (isPresent()) {
      return function.apply(left.get(), right.get());
    }

    return Optional.empty();
  }

  public BiOptional<T, S> filter(BiPredicate<T, S> filter) {
    if (isPresent() && filter.test(left.get(), right.get())) {
      return this;
    }

    return BiOptional.empty();
  }
}
