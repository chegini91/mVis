package com.github.TKnudsen.ComplexDataObject.model.io.parsers.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * Title: DateParser
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DateParser implements IObjectParser<Date> {

	private static DateFormat ISO0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	private static DateFormat ISO1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat IS02 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static DateFormat IS03 = new SimpleDateFormat("yyyy-MM-dd HH");
	private static DateFormat IS04 = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat IS05 = new SimpleDateFormat("yyyy-MM");
	private static DateFormat IS06 = new SimpleDateFormat("dd.MM.yyyy");
	private static DateFormat IS06b = new SimpleDateFormat("dd_MM_yyyy");
	private static DateFormat IS07 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private static DateFormat IS08 = new SimpleDateFormat("MM.dd.yyyy");
	private static DateFormat IS09a = new SimpleDateFormat("dd/MM/yyyy");
	private static DateFormat IS09b = new SimpleDateFormat("dd\\MM\\yyyy");
	private static SimpleDateFormat zzz = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	@Override
	public synchronized Date apply(Object inputObject) {
		Date date = null;

		String interpretableString = String.valueOf(inputObject);
		String replacedT = interpretableString.replace("T", " ");

		// speedup:
		if (interpretableString.equals("Date/Time"))
			return date;

		// alternative
		if (replacedT.length() == 23)
			try {
				// 2007-01-01 00:00:00:000
				synchronized (ISO0) {
					date = ISO0.parse(replacedT);
				}
			} catch (ParseException pe0) {
			}
		else if (replacedT.length() == 19)
			try {
				// 2007-01-01 00:00:00
				synchronized (ISO1) {
					date = ISO1.parse(replacedT);
				}
			} catch (ParseException pe8) {
			}
		else if (replacedT.length() == 16)
			try {
				// 2007-01-01 00:00
				synchronized (IS02) {
					date = IS02.parse(replacedT);
				}
			} catch (ParseException pe8) {
				try {
					// 2007.01.01 00:00
					date = IS07.parse(replacedT);
				} catch (ParseException pe7) {
				}
			}
		else if (replacedT.length() == 13)
			try {
				// 2007-01-01 00
				synchronized (IS03) {
					date = IS03.parse(replacedT);
				}
			} catch (ParseException pe8) {
			}
		else if (replacedT.length() == 10)
			try {
				// 2007-01-01
				synchronized (IS04) {
					date = IS04.parse(replacedT);
				}
			} catch (ParseException pe8) {
				try {
					// 13.01.1969
					synchronized (IS06) {
						date = IS06.parse(replacedT);
					}
				} catch (ParseException pe) {
					try {
						// 01_13_1969
						synchronized (IS06b) {
							date = IS06b.parse(replacedT);
						}
					} catch (ParseException pe_) {
						try {
							// 01.13.1969
							synchronized (IS08) {
								date = IS08.parse(replacedT);
							}
						} catch (ParseException pe__) {
							try {
								// 13/01/1969
								synchronized (IS09a) {
									date = IS09a.parse(replacedT);
								}
							} catch (ParseException pe___) {
								try {
									// 13\01\1969
									synchronized (IS09b) {
										date = IS09b.parse(replacedT);
									}
								} catch (ParseException pe____) {
								}
							}
						}
					}
				}
			}
		else if (replacedT.length() == 7)
			try {
				// 2007-01
				synchronized (IS05) {
					date = IS05.parse(replacedT);
				}
			} catch (ParseException parseException) {
			}
		else {
			try {
				// Thu Apr 13 00:00:00 CEST 2017
				synchronized (zzz) {
					date = zzz.parse(String.valueOf(inputObject));
				}
			} catch (ParseException parseException) {
			}
		}

		return date;
	}
}
