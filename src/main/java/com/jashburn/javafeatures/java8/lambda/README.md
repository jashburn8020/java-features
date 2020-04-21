# Java 8 (Lambda)

(Based mainly on 'Java 8 Lambdas' by Richard Warburton)

## Lambda Expressions

- A method without a name that is used to pass around behaviour as if it were data

### Different Ways of Writing Lambda Expressions

- No arguments:

```java
Runnable noArguments = () -> System.out.println("Hello World");
```

- One argument:

```java
ActionListener oneArgument = event -> System.out.println("button clicked");
```

- Full block of code
  - follows the usual rules that you would expect from a method

```java
Runnable multiStatement = () -> {
    System.out.print("Hello");
    System.out.println(" World");
};
```

- Represent methods that take more than one argument:

```java
BinaryOperator<Long> add = (x, y) -> x + y;
```

- Explicit types for lambda expression parameters:

```java
BinaryOperator<Long> addExplicit = (Long x, Long y) -> x + y;
```

### Using Values

- Using a variable from the surrounding method in a lambda expression
  - possible to refer to non-`final` variable, but has to be _effectively_ `final`
  - can assign to the variable only once

### Functional Interfaces

- A functional interface is an interface with a single abstract method that is used as the type of a lambda expression
- Important functional interfaces:

| Interface name      | Arguments | Returns   | Example                              |
| ------------------- | --------- | --------- | ------------------------------------ |
| `Predicate<T>`      | `T`       | `boolean` | Has this album been released yet?    |
| `Consumer<T>`       | `T`       | `void`    | Printing out a value                 |
| `Function<T,R>`     | `T`       | `R`       | Get the name from an `Artist` object |
| `Supplier<T>`       | None      | `T`       | A factory method                     |
| `UnaryOperator<T>`  | `T`       | `T`       | Logical not (`!`)                    |
| `BinaryOperator<T>` | `(T, T)`  | `T`       | Multiplying two numbers (`*`)        |

- See:
  - [`lambda/PredicateDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/PredicateDemo.java)
  - [`lambda/ConsumerDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/ConsumerDemo.java)
  - [`lambda/FunctionDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/FunctionDemo.java)
  - [`lambda/SupplierDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/SupplierDemo.java)
  - [`lambda/UnaryBinaryOperatorDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/UnaryBinaryOperatorDemo.java)

### Type Inference

- `javac` looks for information close to your lambda expression and uses this information to figure out what the correct type should be
- Type inference with a `Predicate` (below)
  - a functional interface that checks whether something is true or false
  - the only argument of the lambda expression implementing `Predicate` is inferred as an `Integer`
  - `javac` can also check whether the return value is a boolean - the return type of the `Predicate` method

```java
public interface Predicate<T> {
    boolean test(T t);
}
```

```java
Predicate<Integer> atLeast5 = x -> x > 5;
```

- Type inference with a `BinaryOperator`
  - interface takes two arguments and returns a value, all of which are the same type
  - type is inferred as `Long`

```java
BinaryOperator<Long> addLongs = (x, y) -> x + y;
```

## Streams

- Allow us to write collections-processing code at a higher level of abstraction
- A tool for building up complex operations on collections using a functional approach

### `for` Loop and External Iteration

```java
int count = 0;
for (Artist artist : allArtists) {
    if (artist.isFrom("London")) {
        count++;
    }
}
```

- `for` loop is syntactic sugar that wraps up and hides the iteration

```java
int count = 0;
Iterator<Artist> iterator = allArtists.iterator();
while (iterator.hasNext()) {
    Artist artist = iterator.next();
    if (artist.isFrom("London")) {
        count++;
    }
}
```

- Problems with these approaches:
  - a lot of boilerplate code
  - hard to abstract away the different behavioural operations
  - hard to write a parallel version
  - doesn't fluently convey the intent of the programmer
    - must read though the body of the loop to understand it
    - a burden with a large code base full of them, especially with nested loops
    - conflates what you are doing with how you are doing it

### Internal Iteration with `Stream`

```java
long count = allArtists.stream()
    .filter(artist -> artist.isFrom("London"))
    .count();

```

- 2 simpler operations:
  - finding all the artists from London
    - `filter` the `Stream` - keep only objects that pass a test
    - functional programming - we aren't changing the contents of the `Collection`; we’re just declaring what the contents of the `Stream` will be
  - counting a list of artists
    - `count` how many objects are in a given `Stream`
- The `Stream` object returned isn't a new collection
  - it's a recipe for creating a new collection
  - call to `filter` builds up a `Stream` recipe, but there's nothing to force this recipe to be used
- **Lazy** method
  - methods such as `filter` that build up the `Stream` recipe but don't force a new value to be generated at the end
  - returns a `Stream`
  - not executed until an _eager_ method (terminal step) is called
- **Eager** method
  - methods such as `count` that generate a final value out of the `Stream` sequence
  - returns another value or is `void`
- The preferred way of using these methods is to form a sequence of lazy operations chained together, and then to have a single eager operation at the end that generates your result
  - we can string together lots of different operations over our collection and iterate over the collection only once
- **Bun** methods
  - the opening call to `stream` and the closing call to a `count` or other terminal method
  - they aren't the actual filling of our stream burger, but they help us see where the operations begin and end
- See: [`java.util.stream`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/package-summary.html)

### Concepts

- **Intermediate and terminal operations**
  - a stream _pipeline_ consists of
    - a source (such as a `Collection`, an array, a generator function, or an I/O channel);
    - followed by zero or more _intermediate_ operations such as `Stream.filter` or `Stream.map`;
    - and a _terminal_ operation such as `Stream.forEach` or `Stream.reduce`
  - intermediate operations
    - are always _lazy_
    - execution does not actually perform anything, but instead creates a new stream
    - divided into stateless and stateful operations
    - _stateless_ operations such as `filter` and `map`
      - retain no state from previously seen element when processing a new element
      - each element can be processed independently of operations on other elements
      - pipelines containing exclusively stateless operations can be processed in a single pass - sequential or parallel - with minimal data buffering
    - _stateful_ operations, such as `distinct` and `sorted`
      - may incorporate state from previously seen elements when processing new elements
      - may need to process the entire input before producing a result
      - under parallel computation, some pipelines containing stateful operations may require multiple passes on the data or may need to buffer significant data
  - traversal of the pipeline source does not begin until the terminal operation of the pipeline is executed
  - terminal operations
    - are _eager_ in most cases, completing their traversal of the data source and processing of the pipeline before returning
      - except `iterator()` and `spliterator()`
        - provided as an "escape hatch" to enable arbitrary client-controlled pipeline traversal
- **Non-interference**
  - streams enable execution of possibly-parallel aggregate operations over data sources, including even non-thread-safe collections such as `ArrayList`
  - possible only if we can prevent _interference_ (modifying the data source) with the data source during the execution of a stream pipeline
    - begins when the terminal operation is invoked (except for the escape-hatch operations)
  - exception - streams whose sources are concurrent collections
  - interference can cause exceptions, incorrect answers, or non-conformant behaviour
- **Reduction operation** (also called a _fold_)
  - takes a sequence of input elements and combines them into a single summary result by repeated application of a combining operation, such as finding the sum or maximum of a set of numbers, or accumulating elements into a list
  - general reduction operations: `reduce()`, `collect()`
  - specialised reduction forms: `sum()`, `max()`, `count()`
  - a properly constructed reduce operation is inherently parallelisable, so long as the function(s) used to process the elements are associative and stateless
- **Mutable reduction operation**
  - accumulates input elements into a mutable result container, such as a `Collection` or `StringBuilder`, as it processes the elements in the stream
  - e.g., concatenating a stream of strings can be achieved with ordinary reduction: `strings.reduce("", String::concat)`
    - poor performance due to the large amount of string copying
    - better performance by accumulating the results into a `StringBuilder`
- **Associativity**
  - condition that a group of quantities connected by operators gives the same result whatever their grouping
    - i.e. in whichever order the operations are performed, as long as the order of the quantities remains the same
    - e.g. `( a × b ) × c = a × ( b × c )`
  - importance to parallel evaluation, e.g., with `a op b op c op d == (a op b) op (c op d)`
    - we can evaluate `(a op b)` in parallel with `(c op d)`, and then invoke `op` on the results
  - examples of associative operations: numeric addition, min, and max, and string concatenation

### Common Stream Operations

- For each of the examples below, see [`streams/CommonStreamOperations.java`](/src/test/java/com/jashburn/javafeatures/java8/streams/CommonStreamOperations.java)

#### `collect(Collectors.toList())`

- an eager operation that generates a list from the values in a `Stream`
- `collect()`
  - a terminal operation
  - performs a mutable reduction operation on the elements of this stream using a `Collector`
- `Collectors.toList()`: returns a `Collector` that accumulates the input elements into a new `List`
- see `collectToList()`

#### `map`

- an intermediate operation
- applies a function (that converts a value of one type into another) to a stream of values, producing another stream of the new values
- see `mapStringToInteger()`

#### `filter`

- an intermediate operation
- retains some elements of the `Stream`, while throwing others out
  - elements that match the given (non-interfering, stateless) predicate
- the presence of an `if` statement in the middle of a `for` loop is an indicator that you can use a `filter`
- see `filterStringStartsWithDigit()`

#### `flatMap`

- an intermediate operation
- replaces a value with a `Stream` and concatenates all the streams together
- its associated functional interface is the same as `map`'s - the `Function` - but its return type is restricted to streams and not any value
- see `flatMapConcatListsToList()`

#### `max` and `min`

- terminal operations
- special cases of reduction
- return maximum and minimum element of the stream according to the provided `Comparator`
  - the `Comparator` is non-interfering and stateless
  - the return value is an `Optional`, which can be empty if the stream is empty
- see `findShortestAndLongestTrack()`

#### `reduce`

- a terminal operation
- to generate a single result from a collection of values
- `count`, `min`, and `max` are forms of reduction
- example `reduce` operation: adding up streams of numbers
  - `int count = Stream.of(1, 2, 3).reduce(0, (acc, element) -> acc + element);`
  - start with a count of 0 (the count of an empty `Stream`)
    - fold together each element with an accumulator (`acc`)
      - add the element to the accumulator at every step
  - _identity_ value: `0`
    - an identity for the accumulator function
    - for all `t`, `accumulator.apply(identity, t)` is equal to `t`
  - _accumulator_ function:
    - must be an associative function
    - holds the current sum (partial result of the reduction)
    - passed in the current element in the `Stream`
- equivalent imperative version:

```java
int acc = 0;
for (Integer element : asList(1, 2, 3)) {
    acc = acc + element;
}
```

- See:
  - `reduceAccumulator()`
  - `reduceParallelAccumulatorCombiner()`

### Putting Operations Together

- Problem: for a given album, to find the nationality of every band playing on that album
  - the artists who play each track can be solo artists or they can be in a band
  - pretend that a band is really an artist whose name begins with 'The'
  - see [`streams/PuttingOperationsTogether.java`](/src/test/java/com/jashburn/javafeatures/java8/streams/PuttingOperationsTogether.java)

### Higher-Order Functions

- A function that either takes another function as an argument or returns a function as its result
- Examples:
  - `map`: its `mapper` argument is a function
  - `comparing`:
    - takes another function in order to extract an index value
    - returns a new `Comparator`
      - has only a single abstract method, so it's a functional interface

## Libraries

### Primitives

- Boxed types of primitives are objects
  - have memory overhead
  - in the worst case, an `Integer[]` may take up nearly six times more memory than an `int[]` of the same size
  - also computational overhead when converting from a primitive type to a boxed type (boxing), and vice versa (unboxing)
- The streams library differentiates between the primitive and boxed versions of some library functions
  - `int`, `long` and `double` have primitive specialisation implementation
  - naming convention
    - prefix with `To` and the primitive type
      - return type is a primitive
      - e.g., `ToLongFunction<T>`: `applyAsLong(T)` returns `long`
    - name prefix is just the type name
      - argument type is a primitive
      - e.g., `LongFunction<R>`: `apply(long)` returns `R`
    - suffixed with `To` and the primitive type
      - higher-order function uses a primitive type
      - e.g., `Stream<T>`: `mapToLong(ToLongFunction<? super T>)` returns `LongStream`
  - specialised version of `Stream` that prefix the type name
    - e.g., `LongStream`
    - `map` implementation is also specialised
      - e.g., `map​(LongUnaryOperator)` returns `LongStream`
        - `LongUnaryOperator`: `applyAsLong(long)` returns `long`
  - get back from a primitive stream to a boxed stream
    - e.g., `DoubleStream`: `mapToObj​(DoubleFunction<? extends U>)` returns `Stream<U>`
    - e.g., `DoubleStream`: `boxed()` returns `Stream<Double>`
- Use primitive specialised functions wherever possible
  - performance benefits
  - additional functionality
  - better convey intent of numerical operations
- See [`libraries/PrimitiveSpecialisation.java`](/src/test/java/com/jashburn/javafeatures/java8/libraries/PrimitiveSpecialisation.java)

### Overload Resolution

- The parameter types of a lambda are inferred from the target type, and the inference follows these rules:
  - if there is a single possible target type, the lambda expression infers the type from the corresponding argument on the functional interface
  - if there are several possible target types, the most specific type is inferred
  - if there are several possible target types and there is no most specific type, you must manually provide (cast) a type

### `@FunctionalInterface`

- Applied to any interface that is intended to be used as a functional interface
  - there to bundle up blocks of code as data
- There are some interfaces that have only a single method but aren't normally meant to be implemented by lambda expressions
  - might assume that the object has internal state and be interfaces with a single method only coincidentally
  - e.g., `java.lang.Comparable`
    - imposes a total ordering on the objects of each class that implements it
      - as opposed to `java.util.Comparator`, a comparison function, which imposes a total ordering on some collection of objects
    - you don't normally think about functions themselves as being comparable objects because they lack fields and state
      - if there are no fields and no state, what is there to sensibly compare?
  - e.g., `java.io.Closeable`
    - a source or destination of data that can be closed
    - must hold an open resource, such as a file handle that needs to be closed at some point in time
    - the interface cannot be a pure function because closing a resource is really another example of mutating state

### Default Methods

- Default methods can be used on any interface, functional or not
  - `stream` method on `Collection`
  - `forEach` method on `Iterable`
- Interfaces don't have instance fields, so default methods can modify their child classes only by calling methods on them
- Have slightly different inheritance rules to regular methods
  - a virtual method
    - opposite of a static method
    - whenever it comes up against competition from a class (concrete) method, the logic for determining which override to pick always chooses the class
  - see [`libraries/InheritanceRules.java`](/src/test/java/com/jashburn/javafeatures/java8/libraries/InheritanceRules.java)
- It is possible to implement two interfaces, both provide `default` methods with the same signature
  - results in compile error
  - requires the concrete class to override the `default` method
  - use enhanced `super` syntax (`InterfaceName.super.method()`) to pick preferred implementation
  - see [`libraries/MultipleInheritance.java`](/src/test/java/com/jashburn/javafeatures/java8/libraries/MultipleInheritance.java)
- Three rules for `default` methods inheritance behaviour:
  1. class wins over interface
  2. subtype wins over supertype
  3. if previous two rules don't give us an answer, implement the method or declare it `abstract`

### Optional

- Designed to provide a better alternative to `null`
- `null` is often used to represent the absence of a value - the use case `Optional` is replacing
- Goals of `Optional`:
  - encourages the coder to check whether a variable is `null` in order to avoid `NullPointerException`
  - documents values that are expected to be absent in a class's API
- Usage:
  - `empty()`: returns an empty `Optional` instance
    - `isPresent()` returns `false`
  - `of(value)`: returns an `Optional` describing the given non-`null` value
    - `isPresent()` returns `true`
  - `get()`: if a value is present, returns the value, otherwise throws `NoSuchElementException`
  - `ofNullable(value)`: returns an `Optional` describing the given value, if non-`null`, otherwise returns an empty `Optional`
  - `orElse(other)`: if a value is present, returns the value, otherwise returns `other`
  - `orElseGet(supplier)`: if a value is present, returns the value, otherwise returns the result produced by the supplying function
- See: [`libraries/OptionalDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/libraries/OptionalDemo.java)

## Advanced Collections and Collectors

### Method References

- Standard form: `ClassName::methodName`
  - lambda: `artist -> artist.getName()`
  - method references: `Artist::getName`
- Not actually calling the method
  - providing the equivalent of a lambda expression that can be called in order to call the method
- Call constructors:
  - lambda: `(name, nationality) -> new Artist(name, nationality)`
  - method references: `Artist::new`
- Create arrays: `String[]::new`

### Element Ordering

- When you create a `Stream` from a collection with a defined order (e.g., `List`), the `Stream` has a defined _encounter order_
- Operations on data may create an encounter order where there wasn't one to begin with, e.g., using `sorted()`
- Encounter order is propagated across intermediate operations if it exists
- See [`advancedcollections/ElementOrdering.java`](/src/test/java/com/jashburn/javafeatures/java8/advancedcollections/ElementOrdering.java)

### Enter the Collector

- `Collector`: a general-purpose construct for producing complex values from streams

#### Into Other Collections

- Some collectors just build up other collections: `Collectors.toList()`, `toSet()`
- To `collect` your values into a `Collection` of a specific type: `Collectors.toCollection(Supplier)`
  - e.g., `stream.collect(toCollection(TreeSet::new))`

#### To Values

- Collect into a single value using a collector
  - according to some ordering
    - e.g., `Collectors.maxBy(Comparator)`, `minBy(Comparator)`
  - implement some numerical operation
    - e.g., `Collectors.averagingInt(ToIntFunction)`, `summingInt(ToIntFunction)`, `summarizingInt(ToIntFunction)`
  - See: [`advancedcollections/CollectToValue.java`](/src/test/java/com/jashburn/javafeatures/java8/advancedcollections/CollectToValue.java)

#### Partitioning and Grouping the Data

- `Collectors.partitioningBy` takes a stream and partitions its contents into two groups
  - uses a `Predicate` to determine whether an element should be part of the `true` group or the `false` group
  - `collect` using this `Collector` returns a `Map` from `Boolean` to a `List` of values
- `Collectors.groupingBy` takes a stream and groups its contents into groups according to a classification function
  - uses a `Function` to determine the grouping into which data is added
  - `collect` using this `Collector` returns a `Map` from a grouping type to a `List` of values
- See [`advancedcollections/CollectToPartition.java`](/src/test/java/com/jashburn/javafeatures/java8/advancedcollections/CollectToPartition.java)

#### Strings

- `Collectors.joining` for building up strings from streams in encounter order
  - delimiter (which goes between elements)
  - a prefix for the result
  - a suffix for the result
- See: [`advancedcollections/JoiningStrings.java`](/src/test/java/com/jashburn/javafeatures/java8/advancedcollections/JoiningStrings.java)

#### Composing Collectors

- Compose collectors using downstream collectors, e.g., with `Collectors.groupingBy(Function, Collector)`
- Downstream collectors can be:
  - `Collectors.counting()`
  - `Collectors.mapping(Function, Collector)`
    - which in turn accepts a further downstream collector such as `Collectors.toList()`
- See: [`advancedcollections/ComposingCollectors.java`](/src/test/java/com/jashburn/javafeatures/java8/advancedcollections/ComposingCollectors.java)

### Collection Niceties

- `Map.computeIfAbsent(K, Function)`
  - if the specified key (`K`) is not already associated with a value (or is mapped to null), compute its value using the given mapping function, and enter it into this map unless the computed value is null
- `Map.merge(K, V, BiFunction)`
  - if the specified key is not already associated with a value or is associated with null, associate it with the given non-null value
  - otherwise, replace the associated value with the results of the given remapping function, or remove if the result is null
  - this method may be of use when combining multiple mapped values for a key
- See [`advancedcollections/CollectionNiceties.java`](/src/test/java/com/jashburn/javafeatures/java8/advancedcollections/CollectionNiceties.java)

## Sources

- Warburton, Richard. Java 8 Lambdas. 1st ed., O’Reilly Media, Inc., 2014.
- "RichardWarburton/java-8-lambdas-exercises." <https://github.com/RichardWarburton/java-8-lambdas-exercises>.
- "java.util.stream (Java SE 11 & JDK 11)." <https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/package-summary.html>.
