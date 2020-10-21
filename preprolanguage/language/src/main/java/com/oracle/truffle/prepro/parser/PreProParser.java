// Generated from language/src/main/java/com/oracle/truffle/prepro/parser/PrePro.g4 by ANTLR 4.7.2
package com.oracle.truffle.prepro.parser;

// DO NOT MODIFY - generated from PrePro.g4 using "mx create-sl-parser"

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.prepro.PreProLanguage;
import com.oracle.truffle.prepro.nodes.PreProExpressionNode;
import com.oracle.truffle.prepro.nodes.PreProRootNode;
import com.oracle.truffle.prepro.nodes.PreProStatementNode;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PreProParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, WS=26, COMMENT=27, LINE_COMMENT=28, TYPE=29, EXISTS=30, IDENTIFIER=31, 
		STRING_LITERAL=32, NUMERIC_LITERAL=33;
	public static final int
		RULE_prepro = 0, RULE_mainFunction = 1, RULE_function = 2, RULE_functionArguments = 3, 
		RULE_statement = 4, RULE_assignment = 5, RULE_arithmetic = 6, RULE_term = 7, 
		RULE_factor = 8, RULE_functionCallStatement = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"prepro", "mainFunction", "function", "functionArguments", "statement", 
			"assignment", "arithmetic", "term", "factor", "functionCallStatement"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'function'", "'main'", "'('", "')'", "'{'", "'}'", "'returns'", 
			"'return'", "';'", "','", "'debugger'", "'='", "'+'", "'-'", "'*'", "'/'", 
			"'X'", "'**'", "'=='", "'<='", "'>='", "'&&'", "'||'", "'<'", "'>'", 
			null, null, null, null, "'exists'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "WS", "COMMENT", "LINE_COMMENT", "TYPE", "EXISTS", "IDENTIFIER", 
			"STRING_LITERAL", "NUMERIC_LITERAL"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PrePro.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	private PreProNodeFactory factory;
	private Source source;

	private static final class BailoutErrorListener extends BaseErrorListener {
	    private final Source source;
	    BailoutErrorListener(Source source) {
	        this.source = source;
	    }
	    @Override
	    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
	        throwParseError(source, line, charPositionInLine, (Token) offendingSymbol, msg);
	    }
	}

	public void SemErr(Token token, String message) {
	    assert token != null;
	    throwParseError(source, token.getLine(), token.getCharPositionInLine(), token, message);
	}

	private static void throwParseError(Source source, int line, int charPositionInLine, Token token, String message) {
	    int col = charPositionInLine + 1;
	    String location = "-- line " + line + " col " + col + ": ";
	    int length = token == null ? 1 : Math.max(token.getStopIndex() - token.getStartIndex(), 0);
	    throw new PreProParseError(source, line, col, length, String.format("Error(s) parsing script:%n" + location + message));
	}

	public static Map<String, RootCallTarget> parsePrePro(PreProLanguage language, Source source) {
	    PreProLexer lexer = new PreProLexer(CharStreams.fromString(source.getCharacters().toString()));
	    PreProParser parser = new PreProParser(new CommonTokenStream(lexer));
	    lexer.removeErrorListeners();
	    parser.removeErrorListeners();
	    BailoutErrorListener listener = new BailoutErrorListener(source);
	    lexer.addErrorListener(listener);
	    parser.addErrorListener(listener);
	    parser.factory = new PreProNodeFactory(language, source);
	    parser.source = source;
	    parser.prepro();
	    return parser.factory.getAllFunctions();
	}

	public PreProParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class PreproContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PreProParser.EOF, 0); }
		public List<MainFunctionContext> mainFunction() {
			return getRuleContexts(MainFunctionContext.class);
		}
		public MainFunctionContext mainFunction(int i) {
			return getRuleContext(MainFunctionContext.class,i);
		}
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public PreproContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prepro; }
	}

	public final PreproContext prepro() throws RecognitionException {
		PreproContext _localctx = new PreproContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prepro);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(20);
				mainFunction();
				}
				break;
			case 2:
				{
				setState(21);
				function();
				}
				break;
			}
			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				setState(26);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
				case 1:
					{
					setState(24);
					mainFunction();
					}
					break;
				case 2:
					{
					setState(25);
					function();
					}
					break;
				}
				}
				setState(30);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(31);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MainFunctionContext extends ParserRuleContext {
		public Token main;
		public Token s;
		public StatementContext statement;
		public Token e;
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public MainFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainFunction; }
	}

	public final MainFunctionContext mainFunction() throws RecognitionException {
		MainFunctionContext _localctx = new MainFunctionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_mainFunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			match(T__0);
			setState(34);
			((MainFunctionContext)_localctx).main = match(T__1);
			setState(35);
			((MainFunctionContext)_localctx).s = match(T__2);
			 factory.startFunction(((MainFunctionContext)_localctx).main, ((MainFunctionContext)_localctx).s); 
			setState(37);
			match(T__3);
			 factory.startBlock();
			                                                  List<PreProStatementNode> body = new ArrayList<>(); 
			setState(39);
			match(T__4);
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << TYPE) | (1L << EXISTS) | (1L << IDENTIFIER) | (1L << STRING_LITERAL) | (1L << NUMERIC_LITERAL))) != 0)) {
				{
				{
				setState(40);
				((MainFunctionContext)_localctx).statement = statement();
				 body.add(((MainFunctionContext)_localctx).statement.result); 
				}
				}
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(48);
			((MainFunctionContext)_localctx).e = match(T__5);
			factory.finishFunction(
			                                                  factory.finishBlock(body, ((MainFunctionContext)_localctx).s.getStartIndex(), ((MainFunctionContext)_localctx).e.getStopIndex() - ((MainFunctionContext)_localctx).s.getStartIndex() + 1)
			                                                );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public Token IDENTIFIER;
		public Token s;
		public Token TYPE;
		public StatementContext statement;
		public Token r;
		public ArithmeticContext arithmetic;
		public Token e;
		public TerminalNode IDENTIFIER() { return getToken(PreProParser.IDENTIFIER, 0); }
		public FunctionArgumentsContext functionArguments() {
			return getRuleContext(FunctionArgumentsContext.class,0);
		}
		public TerminalNode TYPE() { return getToken(PreProParser.TYPE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public ArithmeticContext arithmetic() {
			return getRuleContext(ArithmeticContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(T__0);
			setState(52);
			((FunctionContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			setState(53);
			((FunctionContext)_localctx).s = match(T__2);
			 factory.startFunction(((FunctionContext)_localctx).IDENTIFIER, ((FunctionContext)_localctx).s); 
			setState(55);
			functionArguments();
			setState(56);
			match(T__3);
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(57);
				match(T__6);
				setState(58);
				((FunctionContext)_localctx).TYPE = match(TYPE);
				factory.addReturnType(((FunctionContext)_localctx).TYPE);
				}
			}

			 factory.startBlock();
			                                                  List<PreProStatementNode> body = new ArrayList<>(); 
			setState(63);
			match(T__4);
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__10) | (1L << TYPE) | (1L << EXISTS) | (1L << IDENTIFIER) | (1L << STRING_LITERAL) | (1L << NUMERIC_LITERAL))) != 0)) {
				{
				{
				setState(64);
				((FunctionContext)_localctx).statement = statement();
				 body.add(((FunctionContext)_localctx).statement.result); 
				}
				}
				setState(71);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(72);
				((FunctionContext)_localctx).r = match(T__7);
				setState(73);
				((FunctionContext)_localctx).arithmetic = arithmetic();
				 body.add(factory.createReturn(((FunctionContext)_localctx).r, ((FunctionContext)_localctx).arithmetic.result));
				setState(75);
				match(T__8);
				}
			}

			setState(79);
			((FunctionContext)_localctx).e = match(T__5);
			factory.finishFunction(
			                                                  factory.finishBlock(body, ((FunctionContext)_localctx).s.getStartIndex(), ((FunctionContext)_localctx).e.getStopIndex() - ((FunctionContext)_localctx).s.getStartIndex() + 1)
			                                                );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionArgumentsContext extends ParserRuleContext {
		public Token TYPE;
		public Token IDENTIFIER;
		public List<TerminalNode> TYPE() { return getTokens(PreProParser.TYPE); }
		public TerminalNode TYPE(int i) {
			return getToken(PreProParser.TYPE, i);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(PreProParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(PreProParser.IDENTIFIER, i);
		}
		public FunctionArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionArguments; }
	}

	public final FunctionArgumentsContext functionArguments() throws RecognitionException {
		FunctionArgumentsContext _localctx = new FunctionArgumentsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TYPE) {
				{
				setState(82);
				((FunctionArgumentsContext)_localctx).TYPE = match(TYPE);
				setState(83);
				((FunctionArgumentsContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				 factory.addFormalParameter(((FunctionArgumentsContext)_localctx).TYPE, factory.createStringLiteral(((FunctionArgumentsContext)_localctx).IDENTIFIER, false)); 
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__9) {
					{
					{
					setState(85);
					match(T__9);
					setState(86);
					((FunctionArgumentsContext)_localctx).TYPE = match(TYPE);
					setState(87);
					((FunctionArgumentsContext)_localctx).IDENTIFIER = match(IDENTIFIER);
					 factory.addFormalParameter(((FunctionArgumentsContext)_localctx).TYPE, factory.createStringLiteral(((FunctionArgumentsContext)_localctx).IDENTIFIER, false)); 
					}
					}
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public PreProStatementNode result;
		public AssignmentContext assignment;
		public ArithmeticContext arithmetic;
		public Token d;
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public ArithmeticContext arithmetic() {
			return getRuleContext(ArithmeticContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				setState(96);
				((StatementContext)_localctx).assignment = assignment();
				setState(97);
				match(T__8);
				 ((StatementContext)_localctx).result =  ((StatementContext)_localctx).assignment.result; 
				}
				break;
			case 2:
				{
				setState(100);
				((StatementContext)_localctx).arithmetic = arithmetic();
				setState(101);
				match(T__8);
				 ((StatementContext)_localctx).result =  ((StatementContext)_localctx).arithmetic.result; 
				}
				break;
			case 3:
				{
				setState(104);
				((StatementContext)_localctx).d = match(T__10);
				 ((StatementContext)_localctx).result =  factory.createDebugger(((StatementContext)_localctx).d); 
				setState(106);
				match(T__8);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public PreProStatementNode result;
		public Token TYPE;
		public Token IDENTIFIER;
		public ArithmeticContext arithmetic;
		public TerminalNode IDENTIFIER() { return getToken(PreProParser.IDENTIFIER, 0); }
		public ArithmeticContext arithmetic() {
			return getRuleContext(ArithmeticContext.class,0);
		}
		public TerminalNode TYPE() { return getToken(PreProParser.TYPE, 0); }
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assignment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TYPE) {
				{
				setState(109);
				((AssignmentContext)_localctx).TYPE = match(TYPE);
				}
			}

			setState(112);
			((AssignmentContext)_localctx).IDENTIFIER = match(IDENTIFIER);
			 PreProExpressionNode assignmentName = factory.createStringLiteral(((AssignmentContext)_localctx).IDENTIFIER, false); 
			setState(114);
			match(T__11);
			{
			setState(115);
			((AssignmentContext)_localctx).arithmetic = arithmetic();
			 ((AssignmentContext)_localctx).result =  factory.createAssignment(((AssignmentContext)_localctx).TYPE, assignmentName, ((AssignmentContext)_localctx).arithmetic.result); 
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArithmeticContext extends ParserRuleContext {
		public PreProExpressionNode result;
		public TermContext term;
		public Token op;
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public ArithmeticContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arithmetic; }
	}

	public final ArithmeticContext arithmetic() throws RecognitionException {
		ArithmeticContext _localctx = new ArithmeticContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arithmetic);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			((ArithmeticContext)_localctx).term = term();
			 ((ArithmeticContext)_localctx).result =  ((ArithmeticContext)_localctx).term.result; 
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12 || _la==T__13) {
				{
				{
				setState(120);
				((ArithmeticContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
					((ArithmeticContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(121);
				((ArithmeticContext)_localctx).term = term();
				 ((ArithmeticContext)_localctx).result =  factory.createBinary(((ArithmeticContext)_localctx).op, _localctx.result, ((ArithmeticContext)_localctx).term.result); 
				}
				}
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public PreProExpressionNode result;
		public FactorContext factor;
		public Token op;
		public List<FactorContext> factor() {
			return getRuleContexts(FactorContext.class);
		}
		public FactorContext factor(int i) {
			return getRuleContext(FactorContext.class,i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			((TermContext)_localctx).factor = factor();
			 ((TermContext)_localctx).result =  ((TermContext)_localctx).factor.result; 
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24))) != 0)) {
				{
				{
				setState(131);
				((TermContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24))) != 0)) ) {
					((TermContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(132);
				((TermContext)_localctx).factor = factor();
				 ((TermContext)_localctx).result =  factory.createBinary(((TermContext)_localctx).op, _localctx.result, ((TermContext)_localctx).factor.result); 
				}
				}
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FactorContext extends ParserRuleContext {
		public PreProExpressionNode result;
		public Token IDENTIFIER;
		public FunctionCallStatementContext functionCallStatement;
		public Token STRING_LITERAL;
		public Token NUMERIC_LITERAL;
		public Token name;
		public Token s;
		public ArithmeticContext expr;
		public Token e;
		public TerminalNode IDENTIFIER() { return getToken(PreProParser.IDENTIFIER, 0); }
		public FunctionCallStatementContext functionCallStatement() {
			return getRuleContext(FunctionCallStatementContext.class,0);
		}
		public TerminalNode STRING_LITERAL() { return getToken(PreProParser.STRING_LITERAL, 0); }
		public TerminalNode NUMERIC_LITERAL() { return getToken(PreProParser.NUMERIC_LITERAL, 0); }
		public TerminalNode EXISTS() { return getToken(PreProParser.EXISTS, 0); }
		public ArithmeticContext arithmetic() {
			return getRuleContext(ArithmeticContext.class,0);
		}
		public FactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor; }
	}

	public final FactorContext factor() throws RecognitionException {
		FactorContext _localctx = new FactorContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_factor);
		try {
			setState(160);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(140);
				((FactorContext)_localctx).IDENTIFIER = match(IDENTIFIER);
				 PreProExpressionNode assignmentName = factory.createStringLiteral(((FactorContext)_localctx).IDENTIFIER, false); 
				setState(146);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__2:
					{
					setState(142);
					((FactorContext)_localctx).functionCallStatement = functionCallStatement(assignmentName);
					 ((FactorContext)_localctx).result =  ((FactorContext)_localctx).functionCallStatement.result; 
					}
					break;
				case T__3:
				case T__8:
				case T__9:
				case T__12:
				case T__13:
				case T__14:
				case T__15:
				case T__16:
				case T__17:
				case T__18:
				case T__19:
				case T__20:
				case T__21:
				case T__22:
				case T__23:
				case T__24:
					{
					 ((FactorContext)_localctx).result =  factory.createRead(assignmentName); 
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(148);
				((FactorContext)_localctx).STRING_LITERAL = match(STRING_LITERAL);
				 ((FactorContext)_localctx).result =  factory.createStringLiteral(((FactorContext)_localctx).STRING_LITERAL, true); 
				}
				break;
			case NUMERIC_LITERAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(150);
				((FactorContext)_localctx).NUMERIC_LITERAL = match(NUMERIC_LITERAL);
				 ((FactorContext)_localctx).result =  factory.createNumericLiteral(((FactorContext)_localctx).NUMERIC_LITERAL); 
				}
				break;
			case EXISTS:
				enterOuterAlt(_localctx, 4);
				{
				setState(152);
				match(EXISTS);
				setState(153);
				((FactorContext)_localctx).name = match(IDENTIFIER);
				 ((FactorContext)_localctx).result =  factory.createExistsExpression(((FactorContext)_localctx).name); 
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 5);
				{
				setState(155);
				((FactorContext)_localctx).s = match(T__2);
				setState(156);
				((FactorContext)_localctx).expr = arithmetic();
				setState(157);
				((FactorContext)_localctx).e = match(T__3);
				 ((FactorContext)_localctx).result =  factory.createParenExpression(((FactorContext)_localctx).expr.result, ((FactorContext)_localctx).s.getStartIndex(), ((FactorContext)_localctx).e.getStopIndex() - ((FactorContext)_localctx).s.getStartIndex() + 1); 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionCallStatementContext extends ParserRuleContext {
		public PreProExpressionNode assignmentName;
		public PreProExpressionNode result;
		public ArithmeticContext arithmetic;
		public Token e;
		public List<ArithmeticContext> arithmetic() {
			return getRuleContexts(ArithmeticContext.class);
		}
		public ArithmeticContext arithmetic(int i) {
			return getRuleContext(ArithmeticContext.class,i);
		}
		public FunctionCallStatementContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public FunctionCallStatementContext(ParserRuleContext parent, int invokingState, PreProExpressionNode assignmentName) {
			super(parent, invokingState);
			this.assignmentName = assignmentName;
		}
		@Override public int getRuleIndex() { return RULE_functionCallStatement; }
	}

	public final FunctionCallStatementContext functionCallStatement(PreProExpressionNode assignmentName) throws RecognitionException {
		FunctionCallStatementContext _localctx = new FunctionCallStatementContext(_ctx, getState(), assignmentName);
		enterRule(_localctx, 18, RULE_functionCallStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(T__2);
			 List<PreProExpressionNode> parameters = new ArrayList<>();
			                                      ((FunctionCallStatementContext)_localctx).result =  factory.createRead(assignmentName); 
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << EXISTS) | (1L << IDENTIFIER) | (1L << STRING_LITERAL) | (1L << NUMERIC_LITERAL))) != 0)) {
				{
				setState(164);
				((FunctionCallStatementContext)_localctx).arithmetic = arithmetic();
				 parameters.add(((FunctionCallStatementContext)_localctx).arithmetic.result); 
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__9) {
					{
					{
					setState(166);
					match(T__9);
					setState(167);
					((FunctionCallStatementContext)_localctx).arithmetic = arithmetic();
					 parameters.add(((FunctionCallStatementContext)_localctx).arithmetic.result); 
					}
					}
					setState(174);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(177);
			((FunctionCallStatementContext)_localctx).e = match(T__3);
			 ((FunctionCallStatementContext)_localctx).result =  factory.createCall(_localctx.result, parameters, ((FunctionCallStatementContext)_localctx).e); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3#\u00b7\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\5\2\31\n\2\3\2\3\2\7\2\35\n\2\f\2\16\2 \13\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3.\n\3\f\3\16\3\61\13\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4?\n\4\3\4\3\4\3\4\3\4\3\4\7"+
		"\4F\n\4\f\4\16\4I\13\4\3\4\3\4\3\4\3\4\3\4\5\4P\n\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\7\5\\\n\5\f\5\16\5_\13\5\5\5a\n\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6n\n\6\3\7\5\7q\n\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\7\b\177\n\b\f\b\16\b\u0082\13\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\7\t\u008a\n\t\f\t\16\t\u008d\13\t\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\5\n\u0095\n\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n"+
		"\u00a3\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u00ad\n\13\f\13"+
		"\16\13\u00b0\13\13\5\13\u00b2\n\13\3\13\3\13\3\13\3\13\2\2\f\2\4\6\b\n"+
		"\f\16\20\22\24\2\4\3\2\17\20\3\2\21\33\2\u00c1\2\30\3\2\2\2\4#\3\2\2\2"+
		"\6\65\3\2\2\2\b`\3\2\2\2\nm\3\2\2\2\fp\3\2\2\2\16x\3\2\2\2\20\u0083\3"+
		"\2\2\2\22\u00a2\3\2\2\2\24\u00a4\3\2\2\2\26\31\5\4\3\2\27\31\5\6\4\2\30"+
		"\26\3\2\2\2\30\27\3\2\2\2\31\36\3\2\2\2\32\35\5\4\3\2\33\35\5\6\4\2\34"+
		"\32\3\2\2\2\34\33\3\2\2\2\35 \3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37!"+
		"\3\2\2\2 \36\3\2\2\2!\"\7\2\2\3\"\3\3\2\2\2#$\7\3\2\2$%\7\4\2\2%&\7\5"+
		"\2\2&\'\b\3\1\2\'(\7\6\2\2()\b\3\1\2)/\7\7\2\2*+\5\n\6\2+,\b\3\1\2,.\3"+
		"\2\2\2-*\3\2\2\2.\61\3\2\2\2/-\3\2\2\2/\60\3\2\2\2\60\62\3\2\2\2\61/\3"+
		"\2\2\2\62\63\7\b\2\2\63\64\b\3\1\2\64\5\3\2\2\2\65\66\7\3\2\2\66\67\7"+
		"!\2\2\678\7\5\2\289\b\4\1\29:\5\b\5\2:>\7\6\2\2;<\7\t\2\2<=\7\37\2\2="+
		"?\b\4\1\2>;\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\b\4\1\2AG\7\7\2\2BC\5\n\6\2"+
		"CD\b\4\1\2DF\3\2\2\2EB\3\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2HO\3\2\2\2"+
		"IG\3\2\2\2JK\7\n\2\2KL\5\16\b\2LM\b\4\1\2MN\7\13\2\2NP\3\2\2\2OJ\3\2\2"+
		"\2OP\3\2\2\2PQ\3\2\2\2QR\7\b\2\2RS\b\4\1\2S\7\3\2\2\2TU\7\37\2\2UV\7!"+
		"\2\2V]\b\5\1\2WX\7\f\2\2XY\7\37\2\2YZ\7!\2\2Z\\\b\5\1\2[W\3\2\2\2\\_\3"+
		"\2\2\2][\3\2\2\2]^\3\2\2\2^a\3\2\2\2_]\3\2\2\2`T\3\2\2\2`a\3\2\2\2a\t"+
		"\3\2\2\2bc\5\f\7\2cd\7\13\2\2de\b\6\1\2en\3\2\2\2fg\5\16\b\2gh\7\13\2"+
		"\2hi\b\6\1\2in\3\2\2\2jk\7\r\2\2kl\b\6\1\2ln\7\13\2\2mb\3\2\2\2mf\3\2"+
		"\2\2mj\3\2\2\2n\13\3\2\2\2oq\7\37\2\2po\3\2\2\2pq\3\2\2\2qr\3\2\2\2rs"+
		"\7!\2\2st\b\7\1\2tu\7\16\2\2uv\5\16\b\2vw\b\7\1\2w\r\3\2\2\2xy\5\20\t"+
		"\2y\u0080\b\b\1\2z{\t\2\2\2{|\5\20\t\2|}\b\b\1\2}\177\3\2\2\2~z\3\2\2"+
		"\2\177\u0082\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\17\3\2\2"+
		"\2\u0082\u0080\3\2\2\2\u0083\u0084\5\22\n\2\u0084\u008b\b\t\1\2\u0085"+
		"\u0086\t\3\2\2\u0086\u0087\5\22\n\2\u0087\u0088\b\t\1\2\u0088\u008a\3"+
		"\2\2\2\u0089\u0085\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b"+
		"\u008c\3\2\2\2\u008c\21\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u008f\7!\2\2"+
		"\u008f\u0094\b\n\1\2\u0090\u0091\5\24\13\2\u0091\u0092\b\n\1\2\u0092\u0095"+
		"\3\2\2\2\u0093\u0095\b\n\1\2\u0094\u0090\3\2\2\2\u0094\u0093\3\2\2\2\u0095"+
		"\u00a3\3\2\2\2\u0096\u0097\7\"\2\2\u0097\u00a3\b\n\1\2\u0098\u0099\7#"+
		"\2\2\u0099\u00a3\b\n\1\2\u009a\u009b\7 \2\2\u009b\u009c\7!\2\2\u009c\u00a3"+
		"\b\n\1\2\u009d\u009e\7\5\2\2\u009e\u009f\5\16\b\2\u009f\u00a0\7\6\2\2"+
		"\u00a0\u00a1\b\n\1\2\u00a1\u00a3\3\2\2\2\u00a2\u008e\3\2\2\2\u00a2\u0096"+
		"\3\2\2\2\u00a2\u0098\3\2\2\2\u00a2\u009a\3\2\2\2\u00a2\u009d\3\2\2\2\u00a3"+
		"\23\3\2\2\2\u00a4\u00a5\7\5\2\2\u00a5\u00b1\b\13\1\2\u00a6\u00a7\5\16"+
		"\b\2\u00a7\u00ae\b\13\1\2\u00a8\u00a9\7\f\2\2\u00a9\u00aa\5\16\b\2\u00aa"+
		"\u00ab\b\13\1\2\u00ab\u00ad\3\2\2\2\u00ac\u00a8\3\2\2\2\u00ad\u00b0\3"+
		"\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b2\3\2\2\2\u00b0"+
		"\u00ae\3\2\2\2\u00b1\u00a6\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\3\2"+
		"\2\2\u00b3\u00b4\7\6\2\2\u00b4\u00b5\b\13\1\2\u00b5\25\3\2\2\2\23\30\34"+
		"\36/>GO]`mp\u0080\u008b\u0094\u00a2\u00ae\u00b1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}