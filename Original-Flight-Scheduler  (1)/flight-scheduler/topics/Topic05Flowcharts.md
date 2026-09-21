# 05. Flowcharts

A flowchart describes an algorithm before coding. Oval = start/end; rectangle = action; diamond = decision; parallelogram = input/output. This topic is a design document, not a Java program.

## Complete scheduler

```mermaid
flowchart TD
    A([Start]) --> B[Create flight array and three sample objects]
    B --> C[/Display menu and read choice/]
    C --> D{Whole number from 0 to 6?}
    D -- No --> E[/Show validation message/]
    E --> C
    D -- Yes --> F{Choice is 0?}
    F -- Yes --> G([End])
    F -- No --> H[Call method for chosen action]
    H --> C
```

## Search using an array

```mermaid
flowchart TD
    A([Start search]) --> B[/Read flight number/]
    B --> C[Set index to 0]
    C --> D{Index less than flightCount?}
    D -- No --> E[/Display flight not found/]
    D -- Yes --> F{Flight number matches?}
    F -- Yes --> G[/Display matching flight/]
    F -- No --> H[Increase index by 1]
    H --> D
    E --> I([End search])
    G --> I
```

Text version: Start → read number → inspect each occupied array position → display the matching flight, or display “not found” after the final occupied position → end.
