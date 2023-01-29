/*  ---------------------------------------------------------------------------
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */

package io.github.jdevlibs.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import io.github.jdevlibs.utils.DateFormats;

/**
* @author supot
* @version 1.0
*/
public class TestDates {

	public static void main(String[] args) {
		System.out.println(DateFormats.format(new Date()));
		System.out.println(DateFormats.format(LocalDate.now()));
		System.out.println(DateFormats.format(LocalDateTime.now()));
		
		java.sql.Date date = new java.sql.Date(new Date().getTime());
		System.out.println("Sql date : " + date);
		System.out.println("10:20 : " + DateFormats.time("10:20"));
		System.out.println("10:20:45 : " + DateFormats.time("10:20:45"));
		System.out.println("10:70 : " + DateFormats.time("10:70"));
		System.out.println("Date : " + DateFormats.time(new Date()));
		System.out.println("LocalDate : " + DateFormats.time(LocalDate.now()));
		System.out.println("LocalDateTime : " + DateFormats.time(LocalDateTime.now()));
		
		System.out.println("Date -> " + DateFormats.date(new Date()));
		System.out.println("LocalDate -> " + DateFormats.date(LocalDate.now()));
		System.out.println("LocalDateTime -> " + DateFormats.date(LocalDateTime.now()));
		System.out.println("12/02/2020 -> " + DateFormats.date("12/02/2020"));
		System.out.println("12/02/2020 10:20 -> " + DateFormats.date("12/02/2020 10:20"));
		System.out.println("12/02/2020 10:20:30 -> " + DateFormats.date("12/02/2020 10:20:30"));
		System.out.println("2020-08-20 10:20:30 -> " + DateFormats.date("2020-08-20 10:20:30"));
		System.out.println("2020-08-20 10:20 -> " + DateFormats.date("2020-08-20 10:20"));
		System.out.println("2020-08-20 -> " + DateFormats.date("2020-08-20"));
	}

}
