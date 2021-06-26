package xo.marketbot.library.services.completion.interfaces;

import org.jetbrains.annotations.NotNull;
import xo.marketbot.library.services.completion.FilterMode;

import java.util.List;
import java.util.Optional;

/**
 * Interface representing a completion service.
 *
 * @param <T>
 *         Type for a completion identifier. A completion identifier can be any object, but make sure that you can identify them between
 *         each other, or the whole purpose of this would be defeated.
 *
 * @author alexpado
 */
public interface ICompletionService<T> {

    /**
     * Retrieve the {@link List} of {@link String} containing the syntax parameters of the provided completion identifier {@link T}.
     *
     * @param identifier
     *         The completion identifier {@link T} from which the {@link List} should be retrieved.
     *
     * @return A {@link List} of {@link String}
     */
    @NotNull List<String> getIdentifierSyntax(@NotNull T identifier);

    /**
     * Retrieve the syntax at the provided index for the provided completion identifier {@link T}.
     *
     * @param identifier
     *         The completion identifier {@link T} from which the syntax parameter will be retrieved.
     * @param index
     *         The index that will be used to retrieve the syntax parameter from the identifier syntax {@link List}.
     *
     * @return A syntax parameter.
     */
    @NotNull String getIdentifierSyntaxAt(@NotNull T identifier, int index);

    /**
     * Filter the {@link List} of completion identifier matching the provided {@link List} of {@link String} corresponding to the user's
     * input ruled by the provided {@link FilterMode} using {@link FilterMode#isMatching(String, String)}
     *
     * @param input
     *         The {@link List} of {@link String} corresponding to the user's input.
     * @param filterMode
     *         The {@link FilterMode} to use to rule the matching with the user's input.
     *
     * @return A {@link List} of completion identifier {@link T}.
     */
    @NotNull List<T> filterIdentifier(@NotNull List<String> input, @NotNull FilterMode filterMode);

    /**
     * Filter the {@link List} of completion identifier {@link T} matching the provided {@link List} of {@link String} corresponding to the
     * user's input. If possible, this method should only call {@link #filterIdentifier(List, FilterMode)} with the filter mode being set to
     * {@link FilterMode#STARTING_WITH}.
     *
     * @param input
     *         The {@link List} of {@link String} corresponding to the user's input.
     *
     * @return A {@link List} of completion identifier {@link T}.
     */
    @NotNull List<T> filterPotentialIdentifiers(@NotNull List<String> input);

    /**
     * Filter the {@link List} of completion identifier {@link T} matching the provided {@link List} of {@link String} corresponding to the
     * user's input. If possible, this method should only call {@link #filterIdentifier(List, FilterMode)} with the filter mode being set to
     * {@link FilterMode#STRICT}.
     *
     * @param input
     *         The {@link List} of {@link String} corresponding to the user's input.
     *
     * @return A {@link List} of completion identifier {@link T}.
     */
    @NotNull List<T> filterMatchingIdentifiers(@NotNull List<String> input);

    /**
     * Retrieve a {@link List} of {@link String} corresponding to the provided syntax. In most cases, this will just be a {@link List} with
     * only one item: the syntax itself.
     * <p>
     * This can be use to create dynamic completion where one argument in the completion need to be a {@link List} of {@link String} that
     * can mutate over time (eg. A user list). The generation of this {@link List} needs to be done by the implementing class, as this
     * interface does only provide simple completion methods.
     * <p>
     * If dynamic completion is used, you may use this in the {@link #isOptionMatching(String, String, FilterMode)} method.
     *
     * @param syntax
     *         The syntax {@link String} to resolve.
     *
     * @return A {@link String} of {@link String}
     */
    @NotNull List<String> resolveSyntax(@NotNull String syntax);

    /**
     * Check if the provided syntax and input match using the matching rule of the provided {@link FilterMode}. This method should use the
     * {@link #resolveSyntax(String)} method on the first argument to be able to implement easily any syntax mutation needed.
     *
     * @param syntax
     *         The syntax to match
     * @param input
     *         The user's input option to match
     * @param filterMode
     *         The {@link FilterMode} to use to rule the matching between the syntax and the input.
     *
     * @return True if they match, false instead.
     */
    boolean isOptionMatching(@NotNull String syntax, @NotNull String input, @NotNull FilterMode filterMode);

    /**
     * Retrieve a {@link List} of {@link String} completing the user's input.
     *
     * @param userInput
     *         The user's input to complete.
     *
     * @return A {@link List} of {@link String}
     */
    @NotNull List<String> complete(@NotNull String userInput);

    /**
     * Retrieve an {@link Optional} instance of {@link IMatchingResult}. The {@link Optional} won't be empty if one, and only one completion
     * identifier matches the user's input.
     *
     * @param userInput
     *         The user's input to match.
     *
     * @return An {@link Optional} {@link IMatchingResult}
     */
    Optional<IMatchingResult<T>> getMatchingIdentifier(@NotNull String userInput);

}
