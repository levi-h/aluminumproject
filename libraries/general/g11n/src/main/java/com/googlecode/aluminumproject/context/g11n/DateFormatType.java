/*
 * Copyright 2010 Levi Hoogenberg
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

import java.util.Date;

/**
 * A way to format a {@link Date date}.
 *
 * @author levi_h
 */
public enum DateFormatType {
	/**
	 * A short date representation.
	 */
	SHORT_DATE,

	/**
	 * A short time representation.
	 */
	SHORT_TIME,

	/**
	 * A short date and time representation.
	 */
	SHORT_DATE_AND_TIME,

	/**
	 * A medium-sized date representation.
	 */
	MEDIUM_DATE,

	/**
	 * A medium-sized time representation.
	 */
	MEDIUM_TIME,

	/**
	 * A medium-sized date and time representation.
	 */
	MEDIUM_DATE_AND_TIME,

	/**
	 * A long date representation.
	 */
	LONG_DATE,

	/**
	 * A long time representation.
	 */
	LONG_TIME,

	/**
	 * A long date and time representation.
	 */
	LONG_DATE_AND_TIME,

	/**
	 * Using a custom pattern.
	 */
	CUSTOM;
}