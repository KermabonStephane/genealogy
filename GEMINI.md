# Gemini Development Guidelines

This document outlines the best practices and conventions to be followed during software development in this project. The goal is to maintain a clean, modern, and maintainable codebase.

## 1. Clean Architecture

We will follow the principles of Clean Architecture to create a system that is independent of frameworks, UI, and database. This promotes separation of concerns and makes the system easier to test and maintain.

-   **Entities**: These are the core business objects of the application. They should not be affected by any change in the other layers.
-   **Use Cases**: These are the application-specific business rules. They orchestrate the flow of data to and from the entities.
-   **Interface Adapters**: This layer converts data from the format most convenient for the use cases and entities, to the format most convenient for some external agency such as the Database or the Web.
-   **Frameworks and Drivers**: This layer is generally composed of frameworks and tools such as the Database, the Web Framework, etc.

**The Dependency Rule**: Source code dependencies can only point inwards. Nothing in an inner circle can know anything at all about something in an outer circle.

## 2. Clean Code

### Naming Conventions

-   **Descriptive Names**: Choose names that reveal intent.
-   **Avoid Disinformation**: Don't use names that are misleading.
-   **Use Pronounceable Names**: If you can't pronounce it, you can't discuss it without sounding like an idiot.
-   **Use Searchable Names**: Single-letter names and numeric constants have a particular problem in that they are not easy to locate across a body of text.
-   **Class Names**: Should be nouns or noun phrases (e.g., `Customer`, `Account`, `AddressParser`).
-   **Method Names**: Should be verbs or verb phrases (e.g., `postPayment`, `deletePage`, `save`).

### Functions

-   **Small**: Functions should be small. How small? They should hardly ever be 20 lines long.
-   **Do One Thing**: Functions should do one thing. They should do it well. They should do it only.
-   **Fewer Arguments**: The ideal number of arguments for a function is zero (niladic). Next comes one (monadic), followed closely by two (dyadic). Three arguments (triadic) should be avoided where possible. More than three (polyadic) requires very special justification.
-   **No Side Effects**: Your function promises to do one thing. It should not do other hidden things.

### Comments

-   **Comments are a Failure**: Comments are, at best, a necessary evil. If our code is so obscure that it requires a comment, we should consider rewriting it.
-- **Don't Comment Bad Code - Rewrite It**: It's a classic. It's true.
-   **Explain *Why*, Not *What***: If a comment is necessary, it should explain *why* the code is doing something, not *what* it's doing. The code itself should explain what it's doing.

## 3. Modern Code Style

-   **Embrace Modern Language Features**: Use the latest stable features of the programming language. For example, use `async/await` in Python/JavaScript/TypeScript for asynchronous operations.
-   **Immutability**: Prefer immutable data structures. This helps to prevent side effects and makes the code easier to reason about.
-   **Functional Concepts**: Where appropriate, use functional programming concepts like pure functions, higher-order functions, and map/filter/reduce.
-   **Dependency Injection**: Use dependency injection to decouple components and make them easier to test.

## 4. Testing

-   **Write Tests**: All new features and bug fixes should be accompanied by tests.
-   **Unit Tests**: Should be fast, focused, and test a single unit of code in isolation.
-   **Integration Tests**: Should test the integration between different parts of the system.
-   **Test-Driven Development (TDD)**: Is encouraged. Write a failing test before you write the production code.

## 5. Dependencies

-   **Minimize Dependencies**: Only add a new dependency if it's absolutely necessary.
-   **Keep Dependencies Up-to-Date**: Regularly update dependencies to their latest stable versions to incorporate security patches and bug fixes.

## 6. Version Control (Git)

-   **Meaningful Commit Messages**: Write clear and concise commit messages that explain the *what* and *why* of the change.
-   **Atomic Commits**: Each commit should represent a single logical change.
-   **Branches**: Use feature branches for new development. Create pull requests for code review before merging into the main branch.
-   **Rebase, Don't Merge (on feature branches)**: When updating a feature branch with the latest changes from the main branch, prefer rebasing over merging to maintain a clean and linear history.
