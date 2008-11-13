package flapjack.parser;


public class DefaultParseResultFactory implements ParseResultFactory {
    public ParseResult build() {
        return new DefaultParseResult();
    }
}
