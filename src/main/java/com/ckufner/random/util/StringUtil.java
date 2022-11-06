package com.ckufner.random.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public interface StringUtil {
    enum StringSplitOptions {
        RemoveEmptyEntries,
        TrimEntries
    }

    static List<String> split(
            final CharSequence charSequence,
            final char delimiter,
            final Character quote,
            final Character escape,
            final EnumSet<StringSplitOptions> splitOptions
    ) {
        final var result = new ArrayList<String>();

        final var _splitOptions = splitOptions == null ? EnumSet.noneOf(StringSplitOptions.class) : EnumSet.copyOf(splitOptions);

        if (charSequence == null || charSequence.length() == 0) return result;
        if (charSequence.length() == 1) return handleSingleCharacter(charSequence.charAt(0), delimiter, _splitOptions);

        final var stringBuilder = new StringBuilder();
        var isQuoted = false;
        var isEscaped = false;

        for (var i = 0; i < charSequence.length(); i++) {
            var curChar = charSequence.charAt(i);

            if (escape != null && curChar == escape && !isQuoted) {
                stringBuilder.append(curChar);

                isEscaped = true;
                continue;
            }

            if (quote != null && curChar == quote && !isEscaped) {
                stringBuilder.append(curChar);

                isQuoted = !isQuoted;
                continue;
            }

            if (curChar == delimiter && !isQuoted && !isEscaped) {
                addPartToResult(stringBuilder, result, _splitOptions);
                continue;
            }

            stringBuilder.append(curChar);

            if (isEscaped) isEscaped = false;
        }

        addPartToResult(stringBuilder, result, _splitOptions);

        return result;
    }

    private static void addPartToResult(
            final StringBuilder stringBuilder,
            final ArrayList<String> result,
            final EnumSet<StringSplitOptions> splitOptions
    ) {
        var part = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());

        if (splitOptions.contains(StringSplitOptions.TrimEntries)) {
            part = part.trim();
        }

        if (splitOptions.contains(StringSplitOptions.RemoveEmptyEntries)) {
            if (part.length() > 0) {
                result.add(part);
            }
        } else {
            result.add(part);
        }
    }

    private static List<String> handleSingleCharacter(
            final char c,
            final char delimiter,
            final EnumSet<StringSplitOptions> splitOptions
    ) {
        final var result = new ArrayList<String>();

        if (c == delimiter) {
            if (splitOptions.contains(StringSplitOptions.RemoveEmptyEntries)) return result;

            result.add("");
            result.add("");

            return result;
        }

        if (c <= 32 && splitOptions.contains(StringSplitOptions.RemoveEmptyEntries)) return result;

        result.add(String.valueOf(c));
        return result;
    }

    static List<String> split(
            final CharSequence charSequence,
            final char delimiter,
            final Character quote,
            final Character escape,
            final StringSplitOptions splitOption
    ) {
        return split(
                charSequence,
                delimiter,
                quote,
                escape,
                splitOption == null ? null : EnumSet.of(splitOption)
        );
    }

    static List<String> split(
            final CharSequence charSequence,
            final char delimiter,
            final EnumSet<StringSplitOptions> splitOptions
    ) {
        return split(
                charSequence,
                delimiter,
                null,
                null,
                splitOptions
        );
    }

    static List<String> split(
            final CharSequence charSequence,
            final char delimiter,
            final StringSplitOptions splitOption
    ) {
        return split(
                charSequence,
                delimiter,
                null,
                null,
                splitOption == null ? null : EnumSet.of(splitOption)
        );
    }

    static List<String> split(
            final CharSequence charSequence,
            final char delimiter
    ) {
        return split(
                charSequence,
                delimiter,
                null,
                null,
                (EnumSet<StringSplitOptions>) null
        );
    }

    static List<String> split(
            final CharSequence charSequence
    ) {
        return split(
                charSequence,
                ',',
                '"',
                '\\',
                EnumSet.of(StringSplitOptions.TrimEntries)
        );
    }
}
