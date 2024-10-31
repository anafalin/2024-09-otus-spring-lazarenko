package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.service.AuthService;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application Testing Commands")
@RequiredArgsConstructor
public class ApplicationShellCommands {
    private final TestRunnerService testRunnerService;

    private final AuthService authService;

    private final LocalizedIOService localizedIOService;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption String firstName, @ShellOption String lastName) {
        authService.login(firstName, lastName);
        return String.format(localizedIOService.getMessage("Shell.welcome.message", firstName, lastName));
    }

    @ShellMethod(value = "Run testing", key = {"rt", "run test"})
    @ShellMethodAvailability(value = "isUserAuthorized")
    private void runTest() {
        testRunnerService.run();
    }

    private Availability isUserAuthorized() {
        return authService.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable(localizedIOService.getMessage("Shell.unavailable.error.message"));
    }
}