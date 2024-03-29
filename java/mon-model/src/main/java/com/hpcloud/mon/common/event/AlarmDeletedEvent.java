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
package com.hpcloud.mon.common.event;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.hpcloud.mon.common.model.metric.MetricDefinition;

/**
 * Represents an alarm having been deleted.
 */
@JsonRootName(value = "alarm-deleted")
public class AlarmDeletedEvent implements Serializable {
  private static final long serialVersionUID = -845914476456541787L;

  public String tenantId;
  public String alarmId;
  public Map<String, MetricDefinition> subAlarmMetricDefinitions;

  public AlarmDeletedEvent() {
  }

  public AlarmDeletedEvent(String tenantId, String alarmId,
      Map<String, MetricDefinition> subAlarmMetricDefinitions) {
    this.tenantId = tenantId;
    this.alarmId = alarmId;
    this.subAlarmMetricDefinitions = subAlarmMetricDefinitions;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AlarmDeletedEvent other = (AlarmDeletedEvent) obj;
    if (alarmId == null) {
      if (other.alarmId != null)
        return false;
    } else if (!alarmId.equals(other.alarmId))
      return false;
    if (subAlarmMetricDefinitions == null) {
      if (other.subAlarmMetricDefinitions != null)
        return false;
    } else if (!subAlarmMetricDefinitions.equals(other.subAlarmMetricDefinitions))
      return false;
    if (tenantId == null) {
      if (other.tenantId != null)
        return false;
    } else if (!tenantId.equals(other.tenantId))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((alarmId == null) ? 0 : alarmId.hashCode());
    result = prime * result
        + ((subAlarmMetricDefinitions == null) ? 0 : subAlarmMetricDefinitions.hashCode());
    result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return String.format("AlarmDeletedEvent [tenantId=%s, alarmId=%s, subAlarmIds=%s]", tenantId,
        alarmId, subAlarmMetricDefinitions);
  }
}
