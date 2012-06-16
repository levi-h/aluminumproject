/*
 * Copyright 2012 Aluminum project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.aluminumproject.context.g11n;

import com.googlecode.aluminumproject.context.Context;

import java.util.TimeZone;

/**
 * Provides a constant time zone, irrespective of context.
 */
public class ConstantTimeZoneProvider implements TimeZoneProvider {
	private TimeZone timeZone;

	/**
	 * Creates a constant time zone provider.
	 *
	 * @param timeZone the time zone to provide
	 */
	public ConstantTimeZoneProvider(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public TimeZone provide(Context context) {
		return timeZone;
	}
}