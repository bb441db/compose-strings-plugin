package github.bb441db.compose.strings.parser;

import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.FormatFlagsConversionMismatchException;
import java.util.IllegalFormatFlagsException;
import java.util.IllegalFormatPrecisionException;
import java.util.IllegalFormatWidthException;
import java.util.List;
import java.util.MissingFormatWidthException;
import java.util.UnknownFormatConversionException;
import java.util.UnknownFormatFlagsException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Formatter {

    private static final Pattern fsPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");

    static List<Formatter.FormatString> parse(String s) {
        ArrayList<FormatString> al = new ArrayList();
        Matcher m = fsPattern.matcher(s);
        int i = 0;

        for(int len = s.length(); i < len; i = m.end()) {
            if (!m.find(i)) {
                checkText(s, i, len);
                al.add(new FixedString(s, i, s.length()));
                break;
            }

            if (m.start() != i) {
                checkText(s, i, m.start());
                al.add(new FixedString(s, i, m.start()));
            }

            al.add(new FormatSpecifier(s, m));
        }

        return al;
    }

    private static void checkText(String s, int start, int end) {
        for(int i = start; i < end; ++i) {
            if (s.charAt(i) == '%') {
                char c = i == end - 1 ? 37 : s.charAt(i + 1);
                throw new UnknownFormatConversionException(String.valueOf(c));
            }
        }

    }

    static class DateTime {
        private DateTime() {
        }

        static boolean isValid(char c) {
            switch(c) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'H':
                case 'I':
                case 'L':
                case 'M':
                case 'N':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'h':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'p':
                case 'r':
                case 's':
                case 'y':
                case 'z':
                    return true;
                case 'E':
                case 'G':
                case 'J':
                case 'K':
                case 'O':
                case 'P':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case '[':
                case '\\':
                case ']':
                case '^':
                case '_':
                case '`':
                case 'f':
                case 'g':
                case 'i':
                case 'n':
                case 'o':
                case 'q':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                default:
                    return false;
            }
        }
    }

    static class Conversion {
        static final char DECIMAL_INTEGER = 'd';
        static final char OCTAL_INTEGER = 'o';
        static final char HEXADECIMAL_INTEGER = 'x';
        static final char HEXADECIMAL_INTEGER_UPPER = 'X';
        static final char SCIENTIFIC = 'e';
        static final char SCIENTIFIC_UPPER = 'E';
        static final char GENERAL = 'g';
        static final char GENERAL_UPPER = 'G';
        static final char DECIMAL_FLOAT = 'f';
        static final char HEXADECIMAL_FLOAT = 'a';
        static final char HEXADECIMAL_FLOAT_UPPER = 'A';
        static final char CHARACTER = 'c';
        static final char CHARACTER_UPPER = 'C';
        static final char DATE_TIME = 't';
        static final char DATE_TIME_UPPER = 'T';
        static final char BOOLEAN = 'b';
        static final char BOOLEAN_UPPER = 'B';
        static final char STRING = 's';
        static final char STRING_UPPER = 'S';
        static final char HASHCODE = 'h';
        static final char HASHCODE_UPPER = 'H';
        static final char LINE_SEPARATOR = 'n';
        static final char PERCENT_SIGN = '%';

        private Conversion() {
        }

        static boolean isValid(char c) {
            return isGeneral(c) || isInteger(c) || isFloat(c) || isText(c) || c == 't' || isCharacter(c);
        }

        static boolean isGeneral(char c) {
            switch(c) {
                case 'B':
                case 'H':
                case 'S':
                case 'b':
                case 'h':
                case 's':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isCharacter(char c) {
            switch(c) {
                case 'C':
                case 'c':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isInteger(char c) {
            switch(c) {
                case 'X':
                case 'd':
                case 'o':
                case 'x':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isFloat(char c) {
            switch(c) {
                case 'A':
                case 'E':
                case 'G':
                case 'a':
                case 'e':
                case 'f':
                case 'g':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isText(char c) {
            switch(c) {
                case '%':
                case 'n':
                    return true;
                default:
                    return false;
            }
        }
    }

    static class Flags {
        private int flags;
        static final Formatter.Flags NONE = new Formatter.Flags(0);
        static final Formatter.Flags LEFT_JUSTIFY = new Formatter.Flags(1);
        static final Formatter.Flags UPPERCASE = new Formatter.Flags(2);
        static final Formatter.Flags ALTERNATE = new Formatter.Flags(4);
        static final Formatter.Flags PLUS = new Formatter.Flags(8);
        static final Formatter.Flags LEADING_SPACE = new Formatter.Flags(16);
        static final Formatter.Flags ZERO_PAD = new Formatter.Flags(32);
        static final Formatter.Flags GROUP = new Formatter.Flags(64);
        static final Formatter.Flags PARENTHESES = new Formatter.Flags(128);
        static final Formatter.Flags PREVIOUS = new Formatter.Flags(256);

        private Flags(int f) {
            this.flags = f;
        }

        public int valueOf() {
            return this.flags;
        }

        public boolean contains(Formatter.Flags f) {
            return (this.flags & f.valueOf()) == f.valueOf();
        }

        public Formatter.Flags dup() {
            return new Formatter.Flags(this.flags);
        }

        private Formatter.Flags add(Formatter.Flags f) {
            this.flags |= f.valueOf();
            return this;
        }

        public Formatter.Flags remove(Formatter.Flags f) {
            this.flags &= ~f.valueOf();
            return this;
        }

        public static Formatter.Flags parse(String s, int start, int end) {
            Formatter.Flags f = new Formatter.Flags(0);

            for(int i = start; i < end; ++i) {
                char c = s.charAt(i);
                Formatter.Flags v = parse(c);
                if (f.contains(v)) {
                    throw new DuplicateFormatFlagsException(v.toString());
                }

                f.add(v);
            }

            return f;
        }

        private static Formatter.Flags parse(char c) {
            switch(c) {
                case ' ':
                    return LEADING_SPACE;
                case '!':
                case '"':
                case '$':
                case '%':
                case '&':
                case '\'':
                case ')':
                case '*':
                case '.':
                case '/':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case ':':
                case ';':
                default:
                    throw new UnknownFormatFlagsException(String.valueOf(c));
                case '#':
                    return ALTERNATE;
                case '(':
                    return PARENTHESES;
                case '+':
                    return PLUS;
                case ',':
                    return GROUP;
                case '-':
                    return LEFT_JUSTIFY;
                case '0':
                    return ZERO_PAD;
                case '<':
                    return PREVIOUS;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.contains(LEFT_JUSTIFY)) {
                sb.append('-');
            }

            if (this.contains(UPPERCASE)) {
                sb.append('^');
            }

            if (this.contains(ALTERNATE)) {
                sb.append('#');
            }

            if (this.contains(PLUS)) {
                sb.append('+');
            }

            if (this.contains(LEADING_SPACE)) {
                sb.append(' ');
            }

            if (this.contains(ZERO_PAD)) {
                sb.append('0');
            }

            if (this.contains(GROUP)) {
                sb.append(',');
            }

            if (this.contains(PARENTHESES)) {
                sb.append('(');
            }

            if (this.contains(PREVIOUS)) {
                sb.append('<');
            }

            return sb.toString();
        }
    }

    static class FormatSpecifier implements Formatter.FormatString {
        private int index = -1;
        private Formatter.Flags f;
        public int width;
        public int precision;
        public boolean dt;
        public char c;

        private int index(String s, int start, int end) {
            if (start >= 0) {
                try {
                    this.index = Integer.parseInt(s, start, end - 1, 10);
                } catch (NumberFormatException var5) {
                    assert false;
                }
            } else {
                this.index = 0;
            }

            return this.index;
        }

        public int index() {
            return this.index;
        }

        private Formatter.Flags flags(String s, int start, int end) {
            this.f = Formatter.Flags.parse(s, start, end);
            if (this.f.contains(Formatter.Flags.PREVIOUS)) {
                this.index = -1;
            }

            return this.f;
        }

        private int width(String s, int start, int end) {
            this.width = -1;
            if (start >= 0) {
                try {
                    this.width = Integer.parseInt(s, start, end, 10);
                    if (this.width < 0) {
                        throw new IllegalFormatWidthException(this.width);
                    }
                } catch (NumberFormatException var5) {
                    assert false;
                }
            }

            return this.width;
        }

        private int precision(String s, int start, int end) {
            this.precision = -1;
            if (start >= 0) {
                try {
                    this.precision = Integer.parseInt(s, start + 1, end, 10);
                    if (this.precision < 0) {
                        throw new IllegalFormatPrecisionException(this.precision);
                    }
                } catch (NumberFormatException var5) {
                    assert false;
                }
            }

            return this.precision;
        }

        private char conversion(char conv) {
            this.c = conv;
            if (!this.dt) {
                if (!Formatter.Conversion.isValid(this.c)) {
                    throw new UnknownFormatConversionException(String.valueOf(this.c));
                }

                if (Character.isUpperCase(this.c)) {
                    this.f.add(Formatter.Flags.UPPERCASE);
                    this.c = Character.toLowerCase(this.c);
                }

                if (Formatter.Conversion.isText(this.c)) {
                    this.index = -2;
                }
            }

            return this.c;
        }

        FormatSpecifier(String s, Matcher m) {
            this.f = Formatter.Flags.NONE;
            this.dt = false;
            this.index(s, m.start(1), m.end(1));
            this.flags(s, m.start(2), m.end(2));
            this.width(s, m.start(3), m.end(3));
            this.precision(s, m.start(4), m.end(4));
            int tTStart = m.start(5);
            if (tTStart >= 0) {
                this.dt = true;
                if (s.charAt(tTStart) == 'T') {
                    this.f.add(Formatter.Flags.UPPERCASE);
                }
            }

            this.conversion(s.charAt(m.start(6)));
            if (this.dt) {
                this.checkDateTime();
            } else if (Formatter.Conversion.isGeneral(this.c)) {
                this.checkGeneral();
            } else if (Formatter.Conversion.isCharacter(this.c)) {
                this.checkCharacter();
            } else if (Formatter.Conversion.isInteger(this.c)) {
                this.checkInteger();
            } else if (Formatter.Conversion.isFloat(this.c)) {
                this.checkFloat();
            } else {
                if (!Formatter.Conversion.isText(this.c)) {
                    throw new UnknownFormatConversionException(String.valueOf(this.c));
                }

                this.checkText();
            }

        }

        public String toString() {
            StringBuilder sb = new StringBuilder("%");
            Formatter.Flags dupf = this.f.dup().remove(Formatter.Flags.UPPERCASE);
            sb.append(dupf.toString());
            if (this.index > 0) {
                sb.append(this.index).append('$');
            }

            if (this.width != -1) {
                sb.append(this.width);
            }

            if (this.precision != -1) {
                sb.append('.').append(this.precision);
            }

            if (this.dt) {
                sb.append((char)(this.f.contains(Formatter.Flags.UPPERCASE) ? 'T' : 't'));
            }

            sb.append(this.f.contains(Formatter.Flags.UPPERCASE) ? Character.toUpperCase(this.c) : this.c);
            return sb.toString();
        }

        private void checkGeneral() {
            if ((this.c == 'b' || this.c == 'h') && this.f.contains(Formatter.Flags.ALTERNATE)) {
                this.failMismatch(Formatter.Flags.ALTERNATE, this.c);
            }

            if (this.width == -1 && this.f.contains(Formatter.Flags.LEFT_JUSTIFY)) {
                throw new MissingFormatWidthException(this.toString());
            } else {
                this.checkBadFlags(Formatter.Flags.PLUS, Formatter.Flags.LEADING_SPACE, Formatter.Flags.ZERO_PAD, Formatter.Flags.GROUP, Formatter.Flags.PARENTHESES);
            }
        }

        private void checkDateTime() {
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            } else if (!Formatter.DateTime.isValid(this.c)) {
                throw new UnknownFormatConversionException("t" + this.c);
            } else {
                this.checkBadFlags(Formatter.Flags.ALTERNATE, Formatter.Flags.PLUS, Formatter.Flags.LEADING_SPACE, Formatter.Flags.ZERO_PAD, Formatter.Flags.GROUP, Formatter.Flags.PARENTHESES);
                if (this.width == -1 && this.f.contains(Formatter.Flags.LEFT_JUSTIFY)) {
                    throw new MissingFormatWidthException(this.toString());
                }
            }
        }

        private void checkCharacter() {
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            } else {
                this.checkBadFlags(Formatter.Flags.ALTERNATE, Formatter.Flags.PLUS, Formatter.Flags.LEADING_SPACE, Formatter.Flags.ZERO_PAD, Formatter.Flags.GROUP, Formatter.Flags.PARENTHESES);
                if (this.width == -1 && this.f.contains(Formatter.Flags.LEFT_JUSTIFY)) {
                    throw new MissingFormatWidthException(this.toString());
                }
            }
        }

        private void checkInteger() {
            this.checkNumeric();
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            } else {
                if (this.c == 'd') {
                    this.checkBadFlags(Formatter.Flags.ALTERNATE);
                } else if (this.c == 'o') {
                    this.checkBadFlags(Formatter.Flags.GROUP);
                } else {
                    this.checkBadFlags(Formatter.Flags.GROUP);
                }

            }
        }

        private void checkBadFlags(Formatter.Flags... badFlags) {
            Formatter.Flags[] var2 = badFlags;
            int var3 = badFlags.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Formatter.Flags badFlag = var2[var4];
                if (this.f.contains(badFlag)) {
                    this.failMismatch(badFlag, this.c);
                }
            }

        }

        private void checkFloat() {
            this.checkNumeric();
            if (this.c != 'f') {
                if (this.c == 'a') {
                    this.checkBadFlags(Formatter.Flags.PARENTHESES, Formatter.Flags.GROUP);
                } else if (this.c == 'e') {
                    this.checkBadFlags(Formatter.Flags.GROUP);
                } else if (this.c == 'g') {
                    this.checkBadFlags(Formatter.Flags.ALTERNATE);
                }
            }

        }

        private void checkNumeric() {
            if (this.width != -1 && this.width < 0) {
                throw new IllegalFormatWidthException(this.width);
            } else if (this.precision != -1 && this.precision < 0) {
                throw new IllegalFormatPrecisionException(this.precision);
            } else if (this.width == -1 && (this.f.contains(Formatter.Flags.LEFT_JUSTIFY) || this.f.contains(Formatter.Flags.ZERO_PAD))) {
                throw new MissingFormatWidthException(this.toString());
            } else if (this.f.contains(Formatter.Flags.PLUS) && this.f.contains(Formatter.Flags.LEADING_SPACE) || this.f.contains(Formatter.Flags.LEFT_JUSTIFY) && this.f.contains(Formatter.Flags.ZERO_PAD)) {
                throw new IllegalFormatFlagsException(this.f.toString());
            }
        }

        private void checkText() {
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            } else {
                switch(this.c) {
                    case '%':
                        if (this.f.valueOf() != Formatter.Flags.LEFT_JUSTIFY.valueOf() && this.f.valueOf() != Formatter.Flags.NONE.valueOf()) {
                            throw new IllegalFormatFlagsException(this.f.toString());
                        }

                        if (this.width == -1 && this.f.contains(Formatter.Flags.LEFT_JUSTIFY)) {
                            throw new MissingFormatWidthException(this.toString());
                        }
                        break;
                    case 'n':
                        if (this.width != -1) {
                            throw new IllegalFormatWidthException(this.width);
                        }

                        if (this.f.valueOf() != Formatter.Flags.NONE.valueOf()) {
                            throw new IllegalFormatFlagsException(this.f.toString());
                        }
                        break;
                    default:
                        assert false;
                }

            }
        }

        private void failMismatch(Formatter.Flags f, char c) {
            String fs = f.toString();
            throw new FormatFlagsConversionMismatchException(fs, c);
        }

    }

    static class FixedString implements Formatter.FormatString {
        private String s;
        private int start;
        private int end;

        FixedString(String s, int start, int end) {
            this.s = s;
            this.start = start;
            this.end = end;
        }

        public int index() {
            return -2;
        }

        public String toString() {
            return this.s.substring(this.start, this.end);
        }
    }

    interface FormatString {
        int index();

        String toString();
    }
}
