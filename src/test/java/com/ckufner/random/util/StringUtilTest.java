package com.ckufner.random.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
class StringUtilTest {
    @ParameterizedTest
    @ArgumentsSource(value = ReturnsEmptyListArgumentsProvider.class)
    void returnsEmptyList(String param) {
        List<String> list = StringUtil.split(param);

        assertNotNull(list);
        assertEquals(0, list.size());
    }

    private static class ReturnsEmptyListArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of((String) null),
                    Arguments.of("")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(value = DefaultSplitArgumentsProvider.class)
    void defaultSplit(String param, List<String> expected) {
        List<String> list = StringUtil.split(param);

        assertNotNull(list);
        assertArrayEquals(expected.toArray(new String[0]), list.toArray(new String[0]));
    }

    private static class DefaultSplitArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(",", List.of("", "")),

                    Arguments.of("a", List.of("a")),
                    Arguments.of("a,b", List.of("a", "b")),
                    Arguments.of("a,b,c", List.of("a", "b", "c")),

                    Arguments.of(",b,c", List.of("", "b", "c")),
                    Arguments.of("a,,c", List.of("a", "", "c")),
                    Arguments.of("a,b,", List.of("a", "b", "")),
                    Arguments.of(",,c", List.of("", "", "c")),
                    Arguments.of("a,,", List.of("a", "", "")),
                    Arguments.of(",,", List.of("", "", "")),

                    Arguments.of("\"a,b,c\"", List.of("\"a,b,c\"")),
                    Arguments.of("\"a,b,c", List.of("\"a,b,c")),

                    Arguments.of("\"a,\"b,c\"", List.of("\"a,\"b", "c\"")),

                    Arguments.of("\\\"a,b", List.of("\\\"a", "b")),
                    Arguments.of("\"a,b", List.of("\"a,b")),
                    Arguments.of("a\\,b", List.of("a\\,b")),

                    Arguments.of("a\\\\b", List.of("a\\\\b")),
                    Arguments.of("a\\\\,b", List.of("a\\\\,b")),

                    Arguments.of("\"a, b\" <a@b.asdf>", List.of("\"a, b\" <a@b.asdf>")),
                    Arguments.of("\"a, b\" <a@b.asdf>, \"c, d\" <c.d@qwertz>", List.of("\"a, b\" <a@b.asdf>", "\"c, d\" <c.d@qwertz>"))
            );
        }
    }

    @Test
    void defaultSplitsOnCommaDQouteAndBackslashEscapeAndTrimms() {
        final var expected = List.of("\"a, b\" <a@b.asdf>", "\"c, d\" <c.d@qwertz>");
        final var actual = StringUtil.split("\"a, b\" <a@b.asdf>, \"c, d\" <c.d@qwertz>");

        assertNotNull(actual);
        assertArrayEquals(expected.toArray(new String[0]), actual.toArray(new String[0]));
    }

    @Test
    void removeEmptyEntries() {
        var actual = StringUtil.split(",", ',');
        var expected = List.of("", "");

        assertNotNull(actual);
        assertArrayEquals(expected.toArray(new String[0]), actual.toArray(new String[0]));

        actual = StringUtil.split(",", ',', StringUtil.StringSplitOptions.RemoveEmptyEntries);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }
}