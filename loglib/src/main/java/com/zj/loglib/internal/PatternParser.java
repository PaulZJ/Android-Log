package com.zj.loglib.internal;

public class PatternParser {

    private static final char EXCAPE_CHAR = '%';

    private static final int LITERAL_STATE = 0;
    private static final int CONVERTER_STATE = 1;
    private static final int DOT_STATE = 3;
    private static final int MIN_STATE = 4;
    private static final int MAX_STATE = 5;

    int state;
    protected StringBuffer currentLiteral = new StringBuffer(32);
    protected int patternLength;
    protected int i;
    PatternConverter head;
    PatternConverter tail;
    protected FormatInfo formatInfo = new FormatInfo();
    protected String pattern;

    public PatternParser(String pattern) {
        this.pattern = pattern;
        this.patternLength = pattern.length();
        state = LITERAL_STATE;
    }

    private void addToList(PatternConverter patternConverter) {
        if (head == null) {
            head = tail = patternConverter;
        }else {
            tail.next = patternConverter;
            tail = patternConverter;
        }
    }

    public PatternConverter parse() {
        char c;
        i = 0;
        while (i < patternLength) {
            c = pattern.charAt(i++);
            switch (state) {
                case LITERAL_STATE:
                    if (i == patternLength) {
                        currentLiteral.append(c);
                        continue;
                    }
                    if (c == EXCAPE_CHAR) {
                        switch (pattern.charAt(i)) {
                            case EXCAPE_CHAR:
                                currentLiteral.append(c);
                                i++;
                                break;
                            case 'n':
                                currentLiteral.append(PatternLayout.LINE_SEP);
                                i++;
                                break;
                                default:
                                    if (currentLiteral.length() != 0) {
                                        addToList(new LiteralPatternConverter(currentLiteral.toString()));
                                    }
                                    currentLiteral.setLength(0);
                                    currentLiteral.append(c);
                                    state = CONVERTER_STATE;
                                    formatInfo.reset();
                        }
                    }else {
                        currentLiteral.append(c);
                    }
                    break;
                case CONVERTER_STATE:
                    currentLiteral.append(c);
                    switch (c) {
                        case '-':
                            formatInfo.leftAlign = true;
                            break;
                        case '.':
                            state = DOT_STATE;
                            break;
                            default:
                                if (c >= '0' && c <= '9') {
                                    formatInfo.min = c - '0';
                                    state = MIN_STATE;
                                }else {
                                    finalizeConverter(c);
                                }
                    }
                    break;
                case MIN_STATE:
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formatInfo.min = formatInfo.min * 10 + (c - '0');
                    } else if (c == '.') {
                        state = DOT_STATE;
                    } else {
                        finalizeConverter(c);
                    }
                    break;
                case DOT_STATE:
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formatInfo.max = c - '0';
                        state = MAX_STATE;
                    }else {
                        state = LITERAL_STATE;
                    }
                    break;
                case MAX_STATE:
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formatInfo.max = formatInfo.max * 10 + (c - '0');
                    }else {
                        finalizeConverter(c);
                        state = LITERAL_STATE;
                    }
                    break;
            }
        }

        if (currentLiteral.length() != 0) {
            addToList(new LiteralPatternConverter(currentLiteral.toString()));
        }

        return head;
    }

    protected void finalizeConverter(char c) {
        PatternConverter pc = null;

        switch (c) {
            case 'm':
                pc = new BasicPatternConverter(formatInfo, BasicPatternConverter.MESSAGE_CONVERTER);
                currentLiteral.setLength(0);
                break;
                default:
                    pc = new LiteralPatternConverter(currentLiteral.toString());
                    currentLiteral.setLength(0);
        }

        addConverter(pc);
    }

    protected void addConverter(PatternConverter pc) {
        currentLiteral.setLength(0);
        addToList(pc);
        state = LITERAL_STATE;
        formatInfo.reset();
    }
}
