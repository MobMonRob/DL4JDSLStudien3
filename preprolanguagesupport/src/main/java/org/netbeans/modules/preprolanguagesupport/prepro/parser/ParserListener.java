package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.netbeans.modules.preprolanguagesupport.prepro.PreProNode;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProLexer;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProParserBaseListener;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProParser;

/**
 * Extends antlr generated class, whenever rule is met in parser listener gets notified
 * @author paula
 */
public final class ParserListener extends  PreProParserBaseListener{
    private final List<PreProNode> nodes = new ArrayList<>();
    
    public List<PreProNode> getNodes(){
        return nodes;
    }
    
    private void addNode(TerminalNode node, int type, int offset){
        if(node != null && !(node instanceof ErrorNode)){
            addNode(node.getText(), type, offset);
        }
    }
    
    private void addNode(String text, int type, int offset){
        nodes.add(new PreProNode(text, type, offset));
    }
    private int getStart(ParserRuleContext ctx){
        return ctx.getStart().getStartIndex();
    }
        
}
