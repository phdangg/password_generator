# Password Generator Guide
## Introduction
This guide provides instructions on how to compile and run the PasswordGenerator.
java program. This program generates secure passwords based on user input, including email, master password, character set, and desired length. If a custom character set is not provided, a default character set will be used.

**Note**: This project reimplements the concepts presented in the paper "Assistance in Daily Password Generation Tasks," which can be accessed at this [link](https://dl.acm.org/doi/10.1145/3267305.3274127).

## Prerequisites
Ensure you have the following installed on your system:
- Java Development Kit (JDK)
## Compilation Instructions
1. Open your terminal or command prompt.
2. Navigate to the directory where PasswordGenerator.java is located.
3. Compile the Java file using the following command:
    ```
    javac PasswordGenerator.java
    ```
## Running the Program
1. After successful compilation, run the program with:
    ```
    java PasswordGenerator
    ```
2. Follow the prompts provided by the program:
    - Email: Enter your email address.
    - Master Password: Enter your master password.
    - Character Set (optional): Enter a custom character set or type d to use the default character set.
    - Length: Enter the desired length of the generated password.
## Example
```sql
$ java PasswordGenerator
Enter your email: example@example.com
Enter your master password: ********
Enter character set (optional, type 'd' to get default character set): d
Enter the desired length of the password: 16

Generated password: aB1cD2eF3gH4iJ5k
```
## Note
- The default character set includes uppercase and lowercase letters, digits, and special characters.
- The program ensures that the generated password meets typical security requirements, such as including a mix of character types.
