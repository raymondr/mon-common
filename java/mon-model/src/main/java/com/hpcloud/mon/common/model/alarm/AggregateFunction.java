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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.hpcloud.util.stats.Statistic;
import com.hpcloud.util.stats.Statistics.Average;
import com.hpcloud.util.stats.Statistics.Count;
import com.hpcloud.util.stats.Statistics.Max;
import com.hpcloud.util.stats.Statistics.Min;
import com.hpcloud.util.stats.Statistics.Sum;

public enum AggregateFunction {
  MIN, MAX, SUM, COUNT, AVG;

  @JsonCreator
  public static AggregateFunction fromJson(String text) {
    return valueOf(text.toUpperCase());
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  public Class<? extends Statistic> toStatistic() {
    if (AggregateFunction.AVG.equals(this))
      return Average.class;
    if (AggregateFunction.COUNT.equals(this))
      return Count.class;
    if (AggregateFunction.SUM.equals(this))
      return Sum.class;
    if (AggregateFunction.MIN.equals(this))
      return Min.class;
    if (AggregateFunction.MAX.equals(this))
      return Max.class;
    return null;
  }
}