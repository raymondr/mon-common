/*
 * Copyright (c) 2014 Hewlett-Packard Development Company, L.P.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpcloud.mon.common.model.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.hpcloud.mon.common.model.metric.MetricDefinition;

/**
 * Complex alarm parser lister for sub expression extraction.
 */
class AlarmSubExpressionListener extends AlarmExpressionBaseListener {
  private final boolean simpleExpression;
  private AggregateFunction function;
  private String namespace;
  private SortedMap<String, String> dimensions;
  private AlarmOperator operator;
  private double threshold;
  private int period = AlarmSubExpression.DEFAULT_PERIOD;
  private int periods = AlarmSubExpression.DEFAULT_PERIODS;
  private List<Object> elements = new ArrayList<Object>();

  AlarmSubExpressionListener(boolean simpleExpression) {
    this.simpleExpression = simpleExpression;
  }

  private void saveSubExpression() {
    AlarmSubExpression subExpression = new AlarmSubExpression(function, new MetricDefinition(
        namespace, dimensions), operator, threshold, period, periods);
    elements.add(subExpression);

    function = null;
    namespace = null;
    dimensions = null;
    operator = null;
    threshold = 0;
    period = AlarmSubExpression.DEFAULT_PERIOD;
    periods = AlarmSubExpression.DEFAULT_PERIODS;
  }

  @Override
  public void exitRelationalExprFwd(AlarmExpressionParser.RelationalExprFwdContext ctx) {
    // This is *right now* basically the same as a min or max function, convert it
    if (operator == AlarmOperator.GT || operator == AlarmOperator.GTE)
      function = AggregateFunction.MAX;
    else
      function = AggregateFunction.MIN;
    saveSubExpression();
  }

  @Override
  public void exitRelationalExprFuncFwd(AlarmExpressionParser.RelationalExprFuncFwdContext ctx) {
    saveSubExpression();
  }

  @Override
  public void exitRelationalExprBwd(AlarmExpressionParser.RelationalExprBwdContext ctx) {
    operator = AlarmOperator.reverseOperator(operator);
    // This is *right now* basically the same as a min or max function, convert it
    if (operator == AlarmOperator.GT || operator == AlarmOperator.GTE)
      function = AggregateFunction.MAX;
    else
      function = AggregateFunction.MIN;
    saveSubExpression();
  }

  @Override
  public void exitRelationalExprFuncBwd(AlarmExpressionParser.RelationalExprFuncBwdContext ctx) {
    operator = AlarmOperator.reverseOperator(operator);
    saveSubExpression();
  }

  @Override
  public void enterFunctionType(AlarmExpressionParser.FunctionTypeContext ctx) {
    function = AggregateFunction.valueOf(ctx.getChild(0).getText().toUpperCase());
  }

  @Override
  public void enterNamespace(AlarmExpressionParser.NamespaceContext ctx) {
    namespace = ctx.getChild(0).getText();
  }

  @Override
  public void enterDimension(AlarmExpressionParser.DimensionContext ctx) {
    if (dimensions == null)
      dimensions = new TreeMap<String, String>();
    String dimensionName = ctx.getChild(0).getText();
    if (dimensions.put(dimensionName, ctx.getChild(2).getText()) != null)
      throw new IllegalArgumentException("More than one value was given for dimension "
          + dimensionName);
  }

  @Override
  public void enterPeriod(AlarmExpressionParser.PeriodContext ctx) {
    period = Integer.valueOf(ctx.getChild(0).getText());
  }

  @Override
  public void enterRepeat(AlarmExpressionParser.RepeatContext ctx) {
    periods = Integer.valueOf(ctx.getChild(0).getText());
  }

  @Override
  public void enterLt(AlarmExpressionParser.LtContext ctx) {
    assertSimpleExpression();
    operator = AlarmOperator.LT;
  }

  @Override
  public void enterLte(AlarmExpressionParser.LteContext ctx) {
    assertSimpleExpression();
    operator = AlarmOperator.LTE;
  }

  @Override
  public void enterGt(AlarmExpressionParser.GtContext ctx) {
    assertSimpleExpression();
    operator = AlarmOperator.GT;
  }

  @Override
  public void enterGte(AlarmExpressionParser.GteContext ctx) {
    assertSimpleExpression();
    operator = AlarmOperator.GTE;
  }

  @Override
  public void exitLiteral(AlarmExpressionParser.LiteralContext ctx) {
    threshold = Long.valueOf(ctx.getChild(0).getText());
  }

  @Override
  public void exitOrExpr(AlarmExpressionParser.OrExprContext ctx) {
    elements.add(BooleanOperator.OR);
  }

  @Override
  public void exitAndExpr(AlarmExpressionParser.AndExprContext ctx) {
    elements.add(BooleanOperator.AND);
  }

  /**
   * Returns the operator and operand elements of the expression in postfix order. Elements will be
   * of types AlarmSubExpression and BooleanOperator.
   */
  List<Object> getElements() {
    return elements;
  }

  private void assertSimpleExpression() {
    if (simpleExpression && !elements.isEmpty())
      throw new IllegalArgumentException("Expected a simple expression");
  }
}
