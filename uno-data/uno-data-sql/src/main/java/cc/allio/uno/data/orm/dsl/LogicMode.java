package cc.allio.uno.data.orm.dsl;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public enum LogicMode {
    AND, OR;

    public SQLExpr appendWhere(SQLObject sqlObject, SQLExpr where, SQLExpr condition, DbType dbType) {
        if (this == AND) {
            return andWhere(sqlObject, where, condition, dbType);
        } else if (this == OR) {
            return orWhere(sqlObject, where, condition, dbType);
        }
        return where;
    }

    private SQLExpr andWhere(SQLObject sqlObject, SQLExpr where, SQLExpr condition, DbType dbType) {
        if (condition == null) {
            return where;
        }
        if (where == null) {
            condition.setParent(sqlObject);
            where = condition;
            return where;
        }

        List<SQLExpr> items = SQLBinaryOpExpr.split(where, SQLBinaryOperator.BooleanAnd);
        for (SQLExpr item : items) {
            if (condition.equals(item)) {
                return where;
            }

            if (condition instanceof SQLInListExpr inListExpr) {
                if (item instanceof SQLBinaryOpExpr binaryOpItem) {
                    SQLExpr left = binaryOpItem.getLeft();
                    SQLExpr right = binaryOpItem.getRight();
                    if (inListExpr.getExpr().equals(left)
                            && binaryOpItem.getOperator() == SQLBinaryOperator.Equality
                            && !(right instanceof SQLNullExpr)
                    ) {
                        if (inListExpr.getTargetList().contains(right)) {
                            return where;
                        }

                        SQLUtils.replaceInParent(item, new SQLBooleanExpr(false));
                        return where;
                    }
                } else {
                    if (item instanceof SQLInListExpr inListItem && (inListExpr.getExpr().equals(inListItem.getExpr()))) {
                        TreeSet<SQLExpr> set = new TreeSet<>(inListItem.getTargetList());
                        List<SQLExpr> andList = new ArrayList<SQLExpr>();
                        for (SQLExpr exprItem : inListExpr.getTargetList()) {
                            if (set.contains(exprItem)) {
                                andList.add(exprItem.clone());
                            }
                        }

                        if (andList.isEmpty()) {
                            SQLUtils.replaceInParent(item, new SQLBooleanExpr(false));
                            return where;
                        }

                        inListItem.getTargetList().clear();
                        for (SQLExpr val : andList) {
                            inListItem.addTarget(val);
                        }
                        return where;

                    }
                }
            }
        }
        where = SQLBinaryOpExpr.and(where, condition);
        where.setParent(sqlObject);
        return where;
    }

    private SQLExpr orWhere(SQLObject sqlObject, SQLExpr where, SQLExpr condition, DbType dbType) {
        if (condition == null) {
            return where;
        }

        if (where == null) {
            condition.setParent(sqlObject);
            where = condition;
        } else if (SQLBinaryOpExpr.isOr(where) || SQLBinaryOpExpr.isOr(condition)) {
            SQLBinaryOpExprGroup group = new SQLBinaryOpExprGroup(SQLBinaryOperator.BooleanOr, dbType);
            group.add(where);
            group.add(condition);
            group.setParent(sqlObject);
            where = group;
        } else {
            where = SQLBinaryOpExpr.or(where, condition);
            where.setParent(sqlObject);
        }
        return where;
    }
}
