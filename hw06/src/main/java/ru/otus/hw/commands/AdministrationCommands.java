package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@RequiredArgsConstructor
@ShellComponent

public class AdministrationCommands {

    @ShellMethod(value = "Start H2 console", key = "start h2")
    public String startH2Console() throws SQLException {
        Console.main();
        return "Console h2 base has been started";
    }
}