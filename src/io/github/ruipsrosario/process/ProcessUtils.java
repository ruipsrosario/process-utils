package io.github.ruipsrosario.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for {@link Process}-related methods.
 */
public final class ProcessUtils {
    private ProcessUtils() {
        // Empty constructor - Utility classes should not be instanced
    }

    /**
     * Creates a {@link ProcessBuilder} with the commands decoded from the supplied map.
     *
     * @param command The main command to build.
     * @param arguments A map of additional commands to append to the main command.
     *
     * @return The created process builder.
     */
    public static ProcessBuilder buildProcess(String command, Map<String, String> arguments) {
        List<String> commands = new LinkedList<>();
        commands.add(command);

        for (Map.Entry<String, String> entry: arguments.entrySet()) {
            commands.add(entry.getKey());
            if (entry.getValue() != null) {
                commands.add(entry.getValue());
            }
        }

        return new ProcessBuilder(commands);
    }

    /**
     * Locates an executable in the current system.
     * <br><br>
     * This method uses OS-specific commands to retrieve the path to the executable with the name supplied. On Windows
     * this means using the {@code where} command, on other systems the {@code which} command is used.
     * <br><br>
     * This is equivalent to calling {@link #findExecutablePaths(String, int)} with {@link Integer#MAX_VALUE} as an
     * argument.
     *
     * @param executableName The name of the executable to locate.
     *
     * @return A list with all the paths found for executables with the name supplied.
     *
     * @throws NullPointerException If no executable name is supplied.
     * @throws IllegalArgumentException If the supplied executable name is empty.
     *
     * @see <a href="http://stackoverflow.com/a/38073998">Source StackOverflow answer</a>
     * @see #findExecutablePath(String)
     * @see #findExecutablePaths(String, int)
     * @see #existsExecutableName(String)
     */
    public static List<Path> findExecutablePaths(String executableName) {
        return findExecutablePaths(executableName, Integer.MAX_VALUE);
    }

    /**
     * Locates an executable in the current system.
     * <br><br>
     * This method uses OS-specific commands to retrieve the path to the executable with the name supplied. On Windows
     * this means using the {@code where} command, on other systems the {@code which} command is used.
     *
     * @param executableName The name of the executable to locate.
     * @param maxResults The maximum number of results to retrieve.
     *
     * @return A list with all the paths found for executables with the name supplied, capped to the maximum number of
     *         results specified.
     *
     * @throws NullPointerException If no executable name is supplied.
     * @throws IllegalArgumentException If the supplied executable name is empty or if the maximum results is negative
     *                                  or zero.
     *
     * @see <a href="http://stackoverflow.com/a/38073998">Source StackOverflow answer</a>
     * @see #findExecutablePath(String)
     * @see #findExecutablePaths(String)
     * @see #existsExecutableName(String)
     */
    public static List<Path> findExecutablePaths(String executableName, int maxResults) {
        if (executableName == null)
            throw new NullPointerException();
        if (executableName.length() == 0 || maxResults <= 0)
            throw new IllegalArgumentException();

        List<Path> executablePaths = new LinkedList<>();
        try {
            Process process = new ProcessBuilder(isWindowsOS() ? "where" : "which", executableName).start();
            if (process.waitFor() == 0) {

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String executablePath;
                    while (executablePaths.size() < maxResults && (executablePath = reader.readLine()) != null) {
                        executablePaths.add(Paths.get(executablePath));
                    }
                }
            }
        } catch (IOException | InterruptedException ignore) {
            // Empty catch block - Silently ignore errors and return current results
        }

        return executablePaths;
    }

    /**
     * Locates an executable in the current system.
     * <br><br>
     * This method uses OS-specific commands to retrieve the path to the executable with the name supplied. On Windows
     * this means using the {@code where} command, on other systems the {@code which} command is used.
     * <br><br>
     * This is equivalent to retrieving the first result of {@link #findExecutablePaths(String)}, if any.
     *
     * @param executableName The name of the executable to locate.
     * @return The path for the executable if any, {@code null} otherwise.
     *
     * @throws NullPointerException If no executable name is supplied.
     * @throws IllegalArgumentException If the supplied executable name is empty.
     *
     * @see <a href="http://stackoverflow.com/a/38073998">Source StackOverflow answer</a>
     * @see #existsExecutableName(String)
     * @see #findExecutablePaths(String)
     * @see #findExecutablePaths(String, int)
     */
    public static Path findExecutablePath(String executableName) {
        List<Path> executablePaths = findExecutablePaths(executableName, 1);
        return executablePaths.isEmpty() ? null : executablePaths.get(0);
    }

    /**
     * Checks whether or not an executable with the name supplied exists on the current system.
     * <br><br>
     * This method uses OS-specific commands to retrieve the path to the executable with the name supplied. On Windows
     * this means using the {@code where} command, on other systems the {@code which} command is used.
     * <br><br>
     * This is equivalent to checking if {@link #findExecutablePaths(String)} returns any result.
     *
     * @param executableName The name of the executable to locate.
     *
     * @return {@code true} if at least one executable with the supplied name exists, {@code false} otherwise.
     *
     * @throws NullPointerException If no executable name is supplied.
     * @throws IllegalArgumentException If the supplied executable name is empty.
     *
     * @see <a href="http://stackoverflow.com/a/38073998">Source StackOverflow answer</a>
     * @see #findExecutablePath(String)
     * @see #findExecutablePaths(String)
     * @see #findExecutablePaths(String, int)
     */
    public static boolean existsExecutableName(String executableName) {
        return !findExecutablePaths(executableName, 1).isEmpty();
    }

    private static boolean isWindowsOS() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
