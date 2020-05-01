# Java 8 (Lambda)

(Based mainly on 'Java 8 Lambdas' by Richard Warburton)

- [Java 8 (Lambda)](#java-8-lambda)
  - [Lambda Expressions](#lambda-expressions)
    - [Different Ways of Writing Lambda Expressions](#different-ways-of-writing-lambda-expressions)
    - [Using Values](#using-values)
    - [Functional Interfaces](#functional-interfaces)
    - [Type Inference](#type-inference)
  - [Streams](#streams)
    - [`for` Loop and External Iteration](#for-loop-and-external-iteration)
    - [Internal Iteration with `Stream`](#internal-iteration-with-stream)
    - [Concepts](#concepts)
    - [Common Stream Operations](#common-stream-operations)
      - [`collect(Collectors.toList())`](#collectcollectorstolist)
      - [`map`](#map)
      - [`filter`](#filter)
      - [`flatMap`](#flatmap)
      - [`max` and `min`](#max-and-min)
      - [`reduce`](#reduce)
    - [Putting Operations Together](#putting-operations-together)
    - [Higher-Order Functions](#higher-order-functions)
  - [Libraries](#libraries)
    - [Primitives](#primitives)
    - [Overload Resolution](#overload-resolution)
    - [`@FunctionalInterface`](#functionalinterface)
    - [Default Methods](#default-methods)
    - [Optional](#optional)
  - [Advanced Collections and Collectors](#advanced-collections-and-collectors)
    - [Method References](#method-references)
    - [Element Ordering](#element-ordering)
    - [Enter the Collector](#enter-the-collector)
      - [Into Other Collections](#into-other-collections)
      - [To Values](#to-values)
      - [Partitioning and Grouping the Data](#partitioning-and-grouping-the-data)
      - [Strings](#strings)
      - [Composing Collectors](#composing-collectors)
    - [Collection Niceties](#collection-niceties)
  - [Data Parallelism](#data-parallelism)
    - [Parallelism Versus Concurrency](#parallelism-versus-concurrency)
    - [Parallel Stream Operations](#parallel-stream-operations)
    - [Simulation](#simulation)
    - [Caveats](#caveats)
    - [Performance](#performance)
    - [Parallel Array Operations](#parallel-array-operations)
  - [Testing, Debugging, and Refactoring](#testing-debugging-and-refactoring)
    - [Lambda Refactoring Candidates](#lambda-refactoring-candidates)
      - [Querying, Operating, Pushing a Value Back into the Object](#querying-operating-pushing-a-value-back-into-the-object)
      - [The Lonely Override](#the-lonely-override)
      - [Behavioural Write Everything Twice](#behavioural-write-everything-twice)
    - [Unit Testing Lambda Expressions](#unit-testing-lambda-expressions)
    - [Using Lambda Expressions in Test Doubles](#using-lambda-expressions-in-test-doubles)
    - [Logging and Printing](#logging-and-printing)
    - [Midstream Breakpoints](#midstream-breakpoints)
  - [Design and Architectural Principles](#design-and-architectural-principles)
    - [Lambda-Enabled Design Patterns](#lambda-enabled-design-patterns)
      - [Command Pattern](#command-pattern)
      - [Strategy Pattern](#strategy-pattern)
      - [Observer Pattern](#observer-pattern)
      - [Template Method Pattern](#template-method-pattern)
    - [Lambda-Enabled SOLID Principles](#lambda-enabled-solid-principles)
      - [The Single Responsibility Principle](#the-single-responsibility-principle)
      - [The Open/Closed Principle](#the-openclosed-principle)
      - [The Dependency Inversion Principle](#the-dependency-inversion-principle)
  - [Lambda-Enabled Concurrency](#lambda-enabled-concurrency)
    - [Why Use Nonblocking I/O](#why-use-nonblocking-io)
    - [Message Passing Architectures](#message-passing-architectures)
    - [The Pyramid of Doom](#the-pyramid-of-doom)
    - [Futures](#futures)
    - [Completable Futures](#completable-futures)
    - [When and Where](#when-and-where)
  - [Sources](#sources)

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
  - _accumulator_ function
    - must be an _associative_ function
      - order in which the function is applied doesn’t matter as long the values of the sequence aren't changed
      - e.g., `+` and `*` are associative:
        - `(4 + 2) + 1 = 4 + (2 + 1) = 7`
        - `(4 * 2) * 1 = 4 * (2 * 1) = 8`
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

## Data Parallelism

- The big shift from external to internal iteration
  - made it easier to write simple and clean code
  - don't have to manually control the iteration
  - we express the what and, by changing a single method call, we can get a library to figure out the how

### Parallelism Versus Concurrency

- Concurrency arises when two tasks are making progress at overlapping time periods
- Parallelism arises when two tasks are happening at literally the same time, such as on a multicore CPU
- The goal of parallelism is to reduce the runtime of a specific task by breaking it down into smaller components and performing them in parallel
- _Data parallelism_
  - achieve parallelism by splitting up the data to be operated on and assigning a single processing unit to each chunk of data
  - works really well when you want to perform the same operation on a lot of data
  - the problem needs be decomposed in a way that will work on subsections of the data, and then the answers from each subsection can be composed at the end
  - contrasted with _task parallelism_, in which each individual thread of execution can be doing a totally different task

### Parallel Stream Operations

- To make a `Stream` parallel:
  - call `Stream`'s `parallel` method
  - call `Collection`'s `parallelStream` method
- Parallel speed-up can depend on:
  - size of the input stream
    - may be slower with small dataset, and faster with large dataset
  - how you wrote your code
  - how many cores are available

### Simulation

- The kinds of problems that parallel stream libraries excel at are those that involve simple operations processing a lot of data, such as simulations
- _Monte Carlo_ simulations
  - work by running the same simulation many times over with different random seeds on every run
  - results of each run are recorded and aggregated in order to build up a comprehensive simulation
- See: [`dataparallelism/Simulations.java`](/src/test/java/com/jashburn/javafeatures/java8/dataparallelism/Simulations.java)

### Caveats

- You can run existing code in parallel with little modification, but only if you've written idiomatic code
- Idiomatic code:
  - `reduce`
    - _identity_ value for the accumulating function
    - combining function must be _associative_
    - see [`reduce`](#reduce)
  - avoid trying to hold locks
    - the streams framework deals with any necessary synchronisation itself

### Performance

- Factors that influence parallel streams performance
  - _data size_
    - overhead to decomposing the problem to be executed in parallel and merging the results
    - parallelisation worthwhile only when there's enough data that execution of a streams pipeline takes a while
  - _source data structure_
    - each pipeline of operations operates on some initial data source, usually a collection
    - ease splitting out subsections for different data sources
  - _packing_
    - primitives are faster to operate on than boxed values
  - _number of cores_
    - not worth going parallel with single core
    - number of cores that are available to use at runtime
      - other processes executing simultaneously
      - thread affinity (forcing threads to execute on certain cores or CPUs)
  - _cost per element_
    - more time spent operating on each element in the stream, the better performance from going parallel
- Helpful to understand how problems are decomposed and merged

```java
private int addIntegers(List<Integer> values) {
    return values.parallelStream()
        .mapToInt(i -> i)
        .sum();
}
```

- Parallel streams back onto the fork/join framework
  - fork stage recursively splits up a problem
  - each chunk is operated upon in parallel
  - join stage merges the results back together

```text
           ┌─────────────────────────────────────────┐
           │             Elements 1 .. N             │
           ├───────────────────┬─────────────────────┤
Fork       │ Elements 1 .. N/2 │  Elements N/2 .. N  │
           ├────────┬──────────┼───────────┬─────────┤
           │ 1..N/4 │ N/4..N/2 │ N/2..3N/4 │ 3N/4..N │
           └────────┴──────────┴───────────┴─────────┘
            mapToInt   mapToInt   mapToInt   mapToInt
Leaf work      ↓           ↓          ↓          ↓
                 ↘       ↙              ↘      ↙
Join                sum                    sum
                         ↘              ↙
                               sum
```

- On a four-core machine:
  1. data source is decomposed into four chunks of elements
  2. perform leaf computation work in parallel on each thread
     - involves mapping each `Integer` to an `int` and also summing a quarter of the values in each thread
     - ideally, we want to spend as much of our time as possible in leaf computation work
  3. merge the results
     - `sum` operation
     - might involve any kind of `reduce`, `collect`, or terminal operation
- Nature of the initial source is extremely important
  - the ease with which we can repeatedly split a data structure in half corresponds to how fast it can be operated upon
  - main groups of data sources by performance characteristics:
    - _good_
      - `ArrayList`, an array, or `IntStream.range`
      - support random access - can be split up arbitrarily with ease
    - _okay_
      - `HashSet` and `TreeSet`
      - can't easily decompose with perfect amounts of balance, but most of the time it's possible to do so
    - _bad_
      - may take _`O(N)`_ time to decompose
      - `LinkedList`
      - `Streams.iterate` and `BufferedReader.lines` have unknown length at the beginning
- 2 types of stream operations:
  - _stateless_
    - need to maintain no concept of state over the whole operation
    - examples: `map`, `filter`, and `flatMap`
  - _stateful_
    - have the overhead and constraint of maintaining state
    - examples: `sorted`, `distinct`, and `limit`
- If you can get away with using stateless operations, then you will get better parallel performance

### Parallel Array Operations

- Parallel array operations that utilise lambda expressions outside of the streams framework
  - data parallel operations
  - located on the utility class `Arrays`
  - `parallelSort`: sorts elements in parallel
- `parallelSetAll`
  - updates the values in an array using a lambda expression

```java
double[] values = new double[size];
Arrays.parallelSetAll(values, i -> i);
```

- `parallelPrefix`
  - calculates running totals of the values of an array given an arbitrary function
  - useful for performing accumulation-type calculations over time series of data
  - mutates an array, replacing each element with the sum of that element and its predecessors
    - 'sum' can be any `BinaryOperator`
      - side-effect-free, associative function
  - see: [`dataparallelism/ParallelArray.java`](/src/test/java/com/jashburn/javafeatures/java8/dataparallelism/ParallelArray.java)

## Testing, Debugging, and Refactoring

### Lambda Refactoring Candidates

#### Querying, Operating, Pushing a Value Back into the Object

- If you find that your code is repeatedly querying and operating on an object, only to push a value back into that object at the end
  - that code belongs in the class of the object that you're modifying

```java
if (logger.isDebugEnabled()) {
    logger.debug("Look at this: " + expensiveOperation());
}
```

- Refactored to:

```java
logger.debug(() -> "Look at this: " + expensiveOperation());
```

- Better object-oriented programming (OOP)
  - a key OOP concept is to encapsulate local state
  - `isDebugEnabled` exposes its state
  - with the lambda-based approach, the code outside of the logger doesn't need to check the level at all

#### The Lonely Override

- You subclass solely to override a single method
- Example: `ThreadLocal`
  - allows us to create a factory that generates at most one value per thread
  - to look up an artist from the database but want to do it once per thread:

```java
ThreadLocal<Album> thisAlbum = new ThreadLocal<Album> () {
    @Override protected Album initialValue() {
        return database.lookupCurrentAlbum();
    }
};
```

- Refactored to:

```java
ThreadLocal<Album> thisAlbum = ThreadLocal.withInitial(() -> database.lookupCurrentAlbum());
```

- Existing instance of `Supplier<Album>` can be reused and composed
- Don't need to deal with subclassing boilerplate

#### Behavioural Write Everything Twice

- Write Everything Twice (WET) is the opposite of Don't Repeat Yourself (DRY)
- Try adding lambdas where you want to perform a similar overall pattern but have a different behaviour from one variant to another
- See [`testingdebuggingrefactoring/Order.java`](/src/test/java/com/jashburn/javafeatures/java8/testingdebuggingrefactoring/Order.java)

### Unit Testing Lambda Expressions

- View the lambda expression as a block of code within its surrounding method
  - test the behaviour of the surrounding method, not the lambda expression itself
- To test a complex lambda expression directly
  - extract the lambda expression into a method
    - refer to the method using method reference
  - test the extracted method directly to cover edge cases
- See: [`testingdebuggingrefactoring/TestingLambda.java`](/src/test/java/com/jashburn/javafeatures/java8/testingdebuggingrefactoring/TestingLambda.java)

### Using Lambda Expressions in Test Doubles

- One of the simplest ways to use lambda expressions in test code is to implement lightweight stubs
  - really easy and natural to implement if the collaborator to be stubbed is already a functional interface
- If you expect to pass a lambda expression into your code, then it's usually the right thing to have your test also pass in a lambda expression
- See `testPassingInALambda()` in [`testingdebuggingrefactoring/Order.java`](/src/test/java/com/jashburn/javafeatures/java8/testingdebuggingrefactoring/Order.java)

### Logging and Printing

- Let's say you’re performing a series of operations on a collection and you're trying to debug the code
  - you want to see what the result of an individual operation is
  - printing out the collection value after each step is pretty hard with the Streams framework, as intermediate steps are lazily evaluated
- `peek(Consumer) : Stream`
  - look at each value in turn and also lets you continue to operate on the same underlying stream
  - see: [`testingdebuggingrefactoring/LoggingAndPrinting.java`](/src/test/java/com/jashburn/javafeatures/java8/testingdebuggingrefactoring/LoggingAndPrinting.java)

### Midstream Breakpoints

- To allow us to debug a stream element by element, as we might debug a loop step by step, a breakpoint can be set on the body of the `peek` method
- `peek` can just have an empty body that you set a breakpoint in
- Some debuggers won't let you set a breakpoint in an empty body, in which case just map a value to itself in order to be able to set the breakpoint

## Design and Architectural Principles

### Lambda-Enabled Design Patterns

#### Command Pattern

- A command object is an object that encapsulates all the information required to call another method later
- The command pattern is a way of using this object in order to write generic code that sequences and executes methods based on runtime decisions
- 4 classes that take part in the command pattern:
  1. **Receiver**: performs the actual work
  2. **Command**: encapsulates all the information required to call the receiver
  3. **Invoker**: controls the sequencing and execution of one or more commands
  4. **Client**: creates concrete command instances
- Example
  - GUI `Editor` (receiver) component that has actions upon it that we'll be calling, such as `open` or `save` (commands)
  - we want to implement macro functionality (invoker)
    - a series of operations that can be recorded and then run later as a single operation
  - see: [`designarchitecture/commandpattern`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/commandpattern)

#### Strategy Pattern

- The strategy pattern is a way of changing the algorithmic behaviour of software based upon a runtime decision
- The main idea is to be able to define a common problem that is solved by different algorithms
  - encapsulate all the algorithms behind the same programming interface
- Classes that take part in the strategy pattern:
  1. **Context**: maintains reference to one of the concrete strategies and communicates with this object via the strategy interface
  2. **Strategy**: an interface common to all strategies; contains a method the context uses to execute a strategy
  3. **Concrete Strategy**: implements variations of an algorithm the context uses
  4. **Client**: creates a specific strategy and passes it to the context
- Example
  - users have the choice of compressing files using either the zip algorithm or gzip algorithm (concrete strategies)
  - `CompressionStrategy`: API for strategies (strategy)
  - a generic `Compressor` class that can compress using either algorithm (context)
  - see: [`designarchitecture/strategypattern`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/strategypattern)

#### Observer Pattern

- In the observer pattern, an object, called the **subject**, maintains a list of other objects, which are its **observer**s
- When the state of the subject changes, its observers are notified
- Example:
  - NASA and aliens (observers) want to observe (be notified of) landings on the moon (subject)
  - see: [`designarchitecture/observerpattern`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/observerpattern)
- Going down the lambda route depends a lot on the complexity of the observer code

#### Template Method Pattern

- The template method pattern is designed for situations where a common algorithm with a set of differing specifics
  - you want implementations to have a common pattern in order to ensure that they're following the same algorithm
- Overall algorithm design is represented by an **abstract class**
  - has a series of abstract methods that represent customised steps in the algorithm
  - any common code can be kept in this class
- Each variant of the algorithm is implemented by a **concrete class**
  - overrides the abstract methods and provides the relevant implementation
- Example
  - a bank gives out loans to members of the public, companies, and employees
  - similar loan application process - check the identity, credit history, and income history
  - obtain information from different sources
    - e.g., check the identity of a person by looking at an existing bill, but companies have an official registrar
  - employee loan application is just like a personal loan application but with no income history checking
  - see: [`designarchitecture/templatemethodpattern`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/templatemethodpattern)

### Lambda-Enabled SOLID Principles

#### The Single Responsibility Principle

- _Every class or method in your program should have only a single reason to change_
- When the requirements of your software change, the responsibilities of the classes and methods that implement these requirements also change
- If you have a class that has more than one responsibility
  - when a responsibility changes, the resulting code changes can affect the other responsibilities that the class possesses
- A class should not just have a single responsibility; it should also encapsulate it
- Lambda expressions make it a lot easier to implement the single responsibility principle at the method level
- See: [`designarchitecture/singleresponsibilityprinciple/CountPrimes.java`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/singleresponsibilityprinciple/CountPrimes.java)

#### The Open/Closed Principle

- _Software entities should be open for extension, but closed for modification_
- The open/closed principle is an effort to ensure that existing classes can be extended without their internal implementation being modified
  - to avoid changes rippling through the code base in a way that is likely to introduce new bugs
  - rely on an abstraction, and plug in new functionality that fits into this abstraction
- Example: Program that measures information about system performance and graphs the results of these measurements
  - `MetricDataGraph`: the class that has the responsibility for displaying metrics
  - see: [`designarchitecture/openclosedprinciple/MetricDataGraph.java`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/openclosedprinciple/MetricDataGraph.java)
- Higher-order function: a function that takes a function as parameter, or returns a function after its execution
  - open for extension despite being closed for modification
  - example: `ThreadLocal` class
    - see: [`designarchitecture/openclosedprinciple/ThreadLocalOpenClosed.java`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/openclosedprinciple/ThreadLocalOpenClosed.java)
- Immutable objects implement the open/closed principle
  - because their internal state can't be modified, it's safe to add new methods to them
  - the new methods can't alter the internal state of the object, so they are closed for modification
    - but they are adding behaviour, so they are open to extension
  - they are inherently thread-safe
    - there is no internal state to mutate, so they can be shared between different threads

#### The Dependency Inversion Principle

- _Abstractions should not depend on details; details should depend on abstractions_
- Many of the higher-order functions, such as `map`, enable dependency inversion
  - allows us to reuse code for the general concept of transforming a stream of values between different specific transformations
  - doesn't depend upon the details of any of these specific transformations, but upon an abstraction - the functional interface `Function`
- A more complex example of dependency inversion is resource management
  - example: extract headings from a hypothetical markup language
    - each heading is designated by being suffixed with a colon (`:`)
    - the method is going to extract the headings from a file by
      - reading the file
      - looking at each of the lines in turn
      - filtering out the headings
      - closing the file
    - wrap any `Exception` related to the file I/O into a domain exception called a `HeadingLookupException`
    - see: [`designarchitecture/dependencyinversionprinciple/HeadingsExtractor.java`](/src/test/java/com/jashburn/javafeatures/java8/designarchitecture/dependencyinversionprinciple/HeadingsExtractor.java)

## Lambda-Enabled Concurrency

### Why Use Nonblocking I/O

- Nonblocking I/O
  - sometimes called asynchronous I/O
  - can be used to process many concurrent network connections without having an individual thread service each connection
  - the methods to read and write data to your clients return immediately
  - the actual I/O processing happens in a separate thread, and you are free to perform some useful work in the meantime
- The Java standard library presents a nonblocking I/O API in the form of NIO (New I/O)
  - the original version of NIO uses the concept of a `Selector`
    - lets a thread manage multiple channels of communication, such as the network socket that's used to write to your client
  - this approach never proved particularly popular with Java developers and resulted in code that was fairly hard to understand and debug
  - with the introduction of lambda expressions, it becomes idiomatic to design and develop APIs that don't have these deficiencies

### Message Passing Architectures

- No-shared-state design
  - all communication between components is done by sending messages over a bus
  - don't need to protect any shared state
  - don't need any kind of locks or use of the `synchronized` keyword in our code base
  - concurrency is much simpler
- To ensure that we aren't sharing any state between components
  - impose a few constraints on the types of messages being sent
  - immutable, e.g., strings
  - the receiving handler can't modify the state of the `String`, it can't interfere with the behaviour of the sender
  - if messages are mutable
    - copy mutable message the moment you send it
- Message passing and reactive programming
  - for concurrency situations in which we want to have many more units of I/O work, such as connected clients, than we have threads running in parallel
  - use lambda expressions to represent the behaviour, and build APIs that manage the concurrency for you

### The Pyramid of Doom

- We can use callbacks and events to produce nonblocking concurrent code
- If you write code with lots of callbacks, it becomes very hard to read, even with lambda expressions
  - nested callbacks can turn into a _pyramid of doom_

### Futures

- Another option when trying to build up complex sequences of concurrent operations is to use a `Future`
  - an IOU for a value
- Instead of a method returning a value, it returns the `Future`
  - doesn't have the value when it's first created
  - extract the value of the Future by calling its `get` method
    - blocks until the value is ready
- Unfortunately, `Future`s end up with composability issues, just like callbacks
- Example:

```java
@Override
public Album lookupByName(String albumName) {
    Future<Credentials> trackLogin = loginTo("track"); // (i)
    Future<Credentials> artistLogin = loginTo("artist");

    try {
        Future<List<Track>> tracks = lookupTracks(albumName, trackLogin.get()); // (ii)
        Future<List<Artist>> artists = lookupArtists(albumName, artistLogin.get());

        return new Album(albumName, tracks.get(), artists.get()); // (iii)
    } catch (InterruptedException | ExecutionException e) {
        throw new AlbumLookupException(e.getCause());
    }
}
```

- Example (code explanation):
  1. start by logging into the track and artist services
  2. make calls to look up the tracks and artists given the login credentials, and call `get` on both of these login credentials in order to get them out of the `Future`s
  3. build up our new `Album` to return, again calling `get` in order to block on the existing `Future`s
- Example (problem):
  - if you want to pass the result of one `Future` into the beginning of another piece of work, you end up blocking the thread of execution
    - performance limitation because instead of work being executed in parallel, it is (accidentally) run in serial
    - we can't start either of the calls to the lookup services until we've logged into both of them
  - we could drag the blocking `get` calls into the execution body of `lookupTracks` and `lookupArtists`
    - would solve the problem, but would also result in uglier code, and an inability to reuse credentials between multiple calls
- What we really want is a way of acting on the result of a `Future`, without having to make a blocking `get` call
  - combine a `Future` with a callback

### Completable Futures

- `CompletableFuture` combines the IOU idea of a `Future` with using callbacks to handle event-driven work
  - can compose different instances in a way that doesn't result in the pyramid of doom
    - various other languages call them a _deferred object_ or a _promise_
    - in the Google Guava Library and the Spring Framework these are referred to as `ListenableFuture`s
- Example:
  - `loginTo`, `lookupArtists`, and `lookupTracks` all return a `CompletableFuture` instead of a `Future`
  - key "trick" to the `CompletableFuture` API is to register lambda expressions and chain higher-order functions
  - see: [`lambdaconcurrency/CompletableFuturesDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambdaconcurrency/CompletableFuturesDemo.java)
- Use cases:
  - if you want to end your chain with a block of code that returns nothing, such as a `Consumer` or `Runnable`, then take a look at `thenAccept` and `thenRun`
  - transforming the value of the `CompletableFuture`, a bit like using the `map` method on `Stream`, can be achieved using `thenApply`
  - if you want to convert situations in which your `CompletableFuture` has completed with an exception, the `exceptionally` method allows you to recover by registering a function to make an alternative value
  - if you need to do a `map` that takes account of both the exceptional case and regular use cases, use `handle`
  - when trying to figure out what is happening with your `CompletableFuture`, you can use the `isDone` and `isCompletedExceptionally` methods

### When and Where

- There are two scenarios in particular in which you might want to think in terms of reacting to events rather than blocking
  1. when your business domain is phrased in terms of events
     - example: Twitter
       - a service for subscribing to streams of text messages
       - users send messages between one another
       - by making your application event-driven, you are accurately modelling the business domain
     - example: an application that tries to plot the price of shares
       - each new price update can be modelled as an event
  2. where your application needs to perform many I/O operations simultaneously
     - performing blocking I/O requires too many threads to be spawned simultaneously
     - results in too many locks in contention and too much context switching

## Sources

- Warburton, Richard. Java 8 Lambdas. 1st ed., O’Reilly Media, Inc., 2014.
- "RichardWarburton/java-8-lambdas-exercises." <https://github.com/RichardWarburton/java-8-lambdas-exercises>.
- "java.util.stream (Java SE 11 & JDK 11)." <https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/package-summary.html>.
