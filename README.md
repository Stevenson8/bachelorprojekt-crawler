# bachelorprojekt-crawler

## To start the program

To start the Crawler:
- Import the project as a gradle project in your IDE
- In the `Configuration` class, please choose the configurations you want to apply
- Start the program using `Main` as your entry point and `“--add-opens java.base/java.nio=ALL-UNNAMED` added to your VM options

To start the Database:
- Start the MySQL Server locally
- In the `DatabaseHelper` class adapt the variables `DB_CONNECTION_URL`, `USER` and `PASS` accordingly
