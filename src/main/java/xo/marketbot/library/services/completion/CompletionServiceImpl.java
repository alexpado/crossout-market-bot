package xo.marketbot.library.services.completion;

import org.jetbrains.annotations.NotNull;
import xo.marketbot.library.services.completion.exceptions.CompletionException;
import xo.marketbot.library.services.completion.interfaces.ICompletionService;
import xo.marketbot.library.services.completion.interfaces.IMatchingResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class implements the {@link ICompletionService} interface, providing an advanced completion system with support for dynamic
 * argument.
 *
 * @param <T>
 *         Type for a completion identifier. A completion identifier can be any object, but make sure that you can identify them between
 *         each other, or the whole purpose of this would be defeated.
 *
 * @author alexpado
 */
public class CompletionServiceImpl<T> implements ICompletionService<T> {

    private final Map<T, List<String>>      identifiersSyntax;
    private final Map<String, List<String>> dynamicOptions;
    private final List<String>              flags;
    private final HashMap<String, String>   arguments;

    /**
     * Create a new {@link CompletionServiceImpl}.
     *
     * @param identifiersSyntax
     *         A {@link Map} linking each {@link T} to a {@link List} of {@link String}
     */
    public CompletionServiceImpl(@NotNull Map<T, List<String>> identifiersSyntax) {

        this.identifiersSyntax = identifiersSyntax;
        this.dynamicOptions    = new HashMap<>();
        this.flags             = new ArrayList<>();
        this.arguments         = new HashMap<>();
    }

    /**
     * Create a new {@link CompletionServiceImpl}.
     *
     * @param identifiersSyntax
     *         A {@link Map} linking each {@link T} to a {@link List} of {@link String}
     * @param dynamicOptions
     *         A {@link Map} linking each dynamic argument name to its {@link List} of values.
     */
    public CompletionServiceImpl(@NotNull Map<T, List<String>> identifiersSyntax, @NotNull Map<String, List<String>> dynamicOptions) {

        this.identifiersSyntax = identifiersSyntax;
        this.dynamicOptions    = dynamicOptions;
        this.flags             = new ArrayList<>();
        this.arguments         = new HashMap<>();
    }

    /**
     * Generate a {@link List} of {@link String} based on the provided user's input for handling completion.
     *
     * @param userInput
     *         The actual user's input to complete.
     *
     * @return A {@link List} of {@link String}
     */
    private List<String> prepareUserInput(@NotNull String userInput) {

        List<String> input = Arrays.stream(userInput.trim().split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());


        List<String> userInputScan = new ArrayList<>();


        if (userInput.endsWith(" ")) {
            // The string ends with a space, this one need to be enforced for the completion to work in any use-case.
            input.add("");
        }

        StringScanner scanner = new StringScanner(input);
        scanner.scan(this.flags::add, this.arguments::put, userInputScan::add);

        return userInputScan;
    }

    /**
     * Retrieve the {@link List} of {@link String} containing the syntax parameters of the provided completion identifier {@link T}.
     *
     * @param identifier
     *         The completion identifier {@link T} from which the {@link List} should be retrieved.
     *
     * @return A {@link List} of {@link String}
     */
    @Override
    public final @NotNull List<String> getIdentifierSyntax(@NotNull T identifier) {

        if (this.identifiersSyntax.containsKey(identifier)) {
            return this.identifiersSyntax.get(identifier);
        }
        throw new IllegalArgumentException("This identifier isn't registered in this completion service.");
    }

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
    @Override
    public final @NotNull String getIdentifierSyntaxAt(@NotNull T identifier, int index) {

        List<String> syntax = this.getIdentifierSyntax(identifier);
        if (syntax.size() <= index) {
            return syntax.get(syntax.size() - 1);
        }
        return this.getIdentifierSyntax(identifier).get(index);
    }

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
    @Override
    public final @NotNull List<T> filterIdentifier(@NotNull List<String> input, @NotNull FilterMode filterMode) {

        return this.identifiersSyntax.keySet().stream().filter(identifier -> {

            List<String> syntax = this.getIdentifierSyntax(identifier);
            int          sizeA  = syntax.size();
            int          sizeB  = input.size();

            if (syntax.get(syntax.size() - 1).endsWith("...")) {
                return true;
            } else if (filterMode == FilterMode.STRICT) {
                return sizeA == sizeB;
            } else {
                return sizeA >= sizeB;
            }
        }).collect(Collectors.toList());
    }

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
    @Override
    public final @NotNull List<T> filterPotentialIdentifiers(@NotNull List<String> input) {

        return this.filterIdentifier(input, FilterMode.STARTING_WITH);
    }

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
    @Override
    public final @NotNull List<T> filterMatchingIdentifiers(@NotNull List<String> input) {

        return this.filterIdentifier(input, FilterMode.STRICT);
    }

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
    @Override
    public final @NotNull List<String> resolveSyntax(@NotNull String syntax) {

        if (syntax.startsWith("{") && syntax.endsWith("}")) {
            String name = syntax.substring(1, syntax.length() - 1);

            if (!this.dynamicOptions.containsKey(name)) {
                throw new CompletionException("The dynamic argument `" + name + "` isn't registered.");
            }
            return this.dynamicOptions.get(name);
        } else if (syntax.startsWith("[") && syntax.endsWith("]") || syntax.endsWith("...")) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(syntax);
        }
    }

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
    @Override
    public final boolean isOptionMatching(@NotNull String syntax, @NotNull String input, @NotNull FilterMode filterMode) {

        List<String> options = this.resolveSyntax(syntax);
        if (options.isEmpty()) {
            // Named pass-through syntax
            return true;
        }
        return options.stream().anyMatch(option -> filterMode.isMatching(option, input));
    }

    /**
     * Retrieve a {@link List} of {@link String} completing the user's input.
     *
     * @param userInput
     *         The user's input to complete.
     *
     * @return A {@link List} of {@link String}
     */
    @Override
    public final @NotNull List<String> complete(@NotNull String userInput) {

        List<T>      identifiers;
        List<String> input = this.prepareUserInput(userInput);

        identifiers = this.filterPotentialIdentifiers(input);
        identifiers = this.filterByUserInput(identifiers, input, FilterMode.STARTING_WITH);

        List<String> results = this.collectCompletion(identifiers, input);
        // Anti-duplication thing
        results = new ArrayList<>(new HashSet<>(results));
        // Sorting the list
        results.sort(String::compareTo);

        return results;
    }

    /**
     * Retrieve an {@link Optional} instance of {@link IMatchingResult}. The {@link Optional} won't be empty if one, and only one completion
     * identifier matches the user's input.
     *
     * @param userInput
     *         The user's input to match.
     *
     * @return An {@link Optional} {@link IMatchingResult}
     */
    @Override
    public final Optional<IMatchingResult<T>> getMatchingIdentifier(@NotNull String userInput) {

        List<T>      identifiers;
        List<String> input = this.prepareUserInput(userInput);

        identifiers = this.filterMatchingIdentifiers(input);
        identifiers = this.filterByUserInput(identifiers, input, FilterMode.STRICT);

        if (identifiers.size() != 1) {
            return Optional.empty();
        }

        T identifier = identifiers.get(0);

        // Let's build the parameter map !
        Map<String, String> matchingParameter = new HashMap<>();
        List<String>        identifierSyntax  = this.getIdentifierSyntax(identifier);

        for (int i = 0 ; i < identifierSyntax.size() ; i++) {
            String syntax = identifierSyntax.get(i);
            if ((syntax.startsWith("{") && syntax.endsWith("}")) || syntax.startsWith("[") && syntax.endsWith("]")) {
                String name = syntax.substring(1, syntax.length() - 1);
                matchingParameter.put(name, input.get(i));
            } else if (syntax.endsWith("...")) {
                String name = syntax.substring(0, syntax.length() - 3);
                matchingParameter.put(name, String.join(" ", input.subList(i, input.size())));
            }
        }

        // And return the result !
        return Optional.of(new IMatchingResult<>() {

            @NotNull
            @Override
            public T getIdentifier() {

                return identifier;
            }

            @Override
            public String getParameter(String name) {

                return matchingParameter.get(name);
            }

            @Override
            public Map<String, String> getParameters() {

                return matchingParameter;
            }

            @Override
            public List<String> getFlags() {

                return CompletionServiceImpl.this.flags;
            }

            /**
             * TODO: JAVADOC
             *
             * @return
             */
            @Override
            public HashMap<String, String> getArguments() {

                return CompletionServiceImpl.this.arguments;
            }
        });
    }

    private List<T> filterByUserInput(List<T> identifiers, List<String> input, FilterMode filterMode) {

        List<T> ts = identifiers;

        for (int i = 0 ; i < input.size() ; i++) {
            String inputOption = input.get(i);
            // Come on Java, this is so dumb.
            final int finalI = i;

            ts = ts.stream().filter(identifier -> this.isOptionMatching(this.getIdentifierSyntaxAt(identifier, finalI), inputOption,
                    // We need to enforce strict mode if its not the last argument.
                    finalI == input.size() - 1 ? filterMode : FilterMode.STRICT)).collect(Collectors.toList());
        }
        return ts;
    }

    private List<String> collectCompletion(Collection<T> identifiers, List<String> input) {

        return identifiers.stream()
                          .map(identifier -> this.getIdentifierSyntaxAt(identifier, input.size() - 1))
                          .flatMap(syntax -> this.resolveSyntax(syntax).stream())
                          .filter(option -> FilterMode.STARTING_WITH.isMatching(option, input.get(input.size() - 1)))
                          .collect(Collectors.toList());
    }

}
