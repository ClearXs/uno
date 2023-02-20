package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.ExpressionColumnStatement;
import cc.allio.uno.data.sql.SQLException;
import cc.allio.uno.data.sql.expression.ExpressionContext;
import cc.allio.uno.data.sql.expression.PlainExpression;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * 分组语句
 *
 * @author jiangwei
 * @date 2022/9/30 13:40
 * @since 1.1.0
 */
public class GroupStatement extends ExpressionColumnStatement<GroupStatement> implements Group<GroupStatement> {

    public GroupStatement(ExpressionContext expressionContext) {
        super(expressionContext);
    }

    @Override
    public GroupStatement byOne(String fieldName) {
        GroupColumn groupColumn = new GroupColumn(StringUtils.camelToUnderline(fieldName), null, null);
        PlainExpression plainExpression = new PlainExpression(groupColumn, expressionContext);
        lazyOffer(plainExpression, StringPool.COMMA);
        addPrivatizationColumn(groupColumn);
        return this;
    }

    @Override
    public GroupStatement byOnes(String... fieldNames) {
        return byOnes(Lists.newArrayList(fieldNames));
    }

    @Override
    public GroupStatement byOnes(Collection<String> fieldNames) {
        for (String name : fieldNames) {
            GroupColumn groupColumn = new GroupColumn(StringUtils.camelToUnderline(name), null, null);
            PlainExpression plainExpression = new PlainExpression(groupColumn, expressionContext);
            lazyOffer(plainExpression, StringPool.COMMA);
            addPrivatizationColumn(groupColumn);
        }
        return self();
    }

    @Override
    public void syntaxCheck() throws SQLException {

    }

    @Override
    public int order() {
        return GROUP_ORDER;
    }

    @Override
    protected String getStatementSyntax() {
        return GROUP_BY;
    }
}
