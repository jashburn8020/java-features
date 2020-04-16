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
  - [`PredicateDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/PredicateDemo.java)
  - [`ConsumerDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/ConsumerDemo.java)
  - [`FunctionDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/FunctionDemo.java)
  - [`SupplierDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/SupplierDemo.java)
  - [`UnaryBinaryOperatorDemo.java`](/src/test/java/com/jashburn/javafeatures/java8/lambda/UnaryBinaryOperatorDemo.java)

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

## Sources

- Warburton, Richard. Java 8 Lambdas. 1st ed., O’Reilly Media, Inc., 2014.
- "RichardWarburton/java-8-lambdas-exercises." <https://github.com/RichardWarburton/java-8-lambdas-exercises>.
