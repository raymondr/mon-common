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
package com.hpcloud.util.time;

/**
 * Time resolution.
 */
public enum TimeResolution {
  ABSOLUTE {
    @Override
    public long adjust(long timestamp) {
      return timestamp;
    }
  },
  SECONDS {
    @Override
    public long adjust(long timestamp) {
      return Times.roundDownToNearestSecond(timestamp);
    }
  },
  MINUTES {
    @Override
    public long adjust(long timestamp) {
      return Times.roundDownToNearestMinute(timestamp);
    }
  };

  /**
   * Returns the {@code timestamp} adjusted for the resolution.
   */
  public abstract long adjust(long timestamp);
}