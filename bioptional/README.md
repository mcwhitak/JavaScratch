# BiOptional
BiOptional composes two optional elements into a single monad, exposing operations that will be executed if and only if both elements are present.

### Problem
One of the most common ways in which programmers must work outside of the `Optional` construct in java is when evaluting the combined existence of two objects. Current solutions include nested flatmaps and reverting to traditional conditional semantics for evaluting and executing.

```
-- GIVEN --
Optional<String> a;
Optional<String> b;

-- FUNCTION --
// If and only if both a and b are present, return  "${a} ${b}" else return "UNKNOWN"

-- Flatmap Form --
return a.flatMap(aVal -> b.map(bVal -> aVal + " " + bVal))
  .orElse("UNKNOWN");
  
-- Conditional Form --
if (a.isPresent() && b.isPresent()) {
  String aVal = a.get();
  String bVal = b.get();

  return aVal + " " + bVal;
}

return "UNKNOWN";
```

Both existing solutions are not ideal. While the flatMap form stays within the monad and is safe even during a refactor, the nested nature does not lend itself well to readability and combined with style guides and formatting rules can lead to an unruly mess.

The conditional form manually checks conditions which the `Optional` class would normally be able to handle. The inability to compose `Optional` instances makes sense to some degree (how to do it effectively without erasing the value like in `CompletableFuture`) but the negative implications of a refactor bomb due to having to live outside of the monad outweigh the benefits IMO.

### Solution
`BiOptional` aims to fix this issue by making `Optional` composable to a single level. It is not meant to be as flexible or as useful as `Optional` but rather to simply serve as a bridge for this specific use case. `BiOptional` itself is not composable and is meant only to be used as an intermediary when evaluting two composable `Optional` instances. As the composable problem for java types is seemingly unsolvable `BiOptional` does not attempt to solve that in it's generic form.

```
-- GIVEN --
Optional<String> a;
Optional<String> b;

-- FUNCTION --
// If and only if both a and b are present, return  "${a} ${b}" else return "UNKNOWN"

-- BiOptional Form --
return BiOptional.from(a, b)
  .map((aVal, bVal) -> aVal + " " + bVal);
  .orElse("UNKNOWN");
```

While the `BiOptional` entrypoint is not ideal, it could be eventually improved by adding an `Optional::and` function to the java lang which would serve as the main entrypoint

```
return a.and(b)
  .map((aVal, bVal) -> aVal + " " + bVal);
  .orElse("UNKNOWN");
```

