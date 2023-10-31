package schoolsystem

import grails.gorm.transactions.Transactional

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Transactional
class UtiDateService {

    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd")
    SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    String DEFAULT_TIME_ZONE_ID = "Asia/Phnom_Penh"
    TimeZone tz = TimeZone.getTimeZone(DEFAULT_TIME_ZONE_ID)
    List<String> knowJavaDatePatterns = ["yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd HH:mm:ssZ"]
    List<String> knowPatterns = ['dd-MM-yyyy', 'dd/MM/yyyy', 'dd-MMM-yyyy', 'dd/MMM/yyyy', 'dd-MMMM-yyyy', 'dd/MMMM/yyyy', 'dd-MM-yy', 'dd/MM/yy', 'dd-MMM-yy',
                                 'dd/MMM/yy', 'dd-MMMM-yy', 'dd/MMMM/yy', 'yyyy-MM-dd', 'yyyy/MM/dd', 'yyyy-MMM-dd', 'yyyy/MMM/dd', 'yyyy-MMMM-dd', 'yyyy/MMMM/dd']

    /**
     * Return the given date with reset time and minus 7 hours
     * Example
     * Date: 2023-01-25 16:59:59 getStart(Date) => result 2023-01-25 00:00:00
     * @param date
     * @return
     */

    Date getStart(Date date) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTimeZone(this.tz)
        calendar.setTime(date)
        calendar.clearTime()

        return calendar.getTime()
    }

    /**
     * Returns the given date with time set to the end of the day
     * Example
     * Date: 2021-06-29 16:59:59 getEnd(Date) => result 2021-06-29 23:59:59
     */

    Date getEnd(Date date) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTimeZone(this.tz)
        calendar.setTime(date)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)

        return calendar.getTime()
    }

    /**
     * This function is used for get year from the date
     * @param date
     * @return
     */

    Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTimeZone(this.tz)
        calendar.setTime(date)

        Integer year = calendar.get(Calendar.YEAR)

        return year
    }

    /**
     * This function is used for convert date string to java date
     * @param dateString
     * @param timeZone
     * @return
     */

    Date convertStringToDate(String dateString, String timeZone = null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT)

        Date date = dateFormat.parse(dateString)
        if (timeZone) {
            return dateFormatter(date, timeZone)
        }

        return date
    }

    /**
     * This function is used for convert java date to any format in date string
     * @param date
     * @param timeZone
     * @param pattern
     * @return
     */

    String convertDateToString(Date date, String timeZone = null, String pattern = DEFAULT_DATE_FORMAT) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern)
        if (timeZone) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone))
        }

        return dateFormat.format(date)
    }

    /**
     * This function is used for format java date with timezone
     * @param date
     * @param timeZone
     * @return
     */

    Date dateFormatter(Date date, String timeZone = null) {
        for (String pattern : knowJavaDatePatterns) {
            try {
                SimpleDateFormat readFormat = new SimpleDateFormat(pattern)
                if (timeZone) {
                    readFormat.setTimeZone(TimeZone.getTimeZone(timeZone))
                }
                String dateStr = readFormat.format(date)
                SimpleDateFormat writeFormat = new SimpleDateFormat(pattern)

                return writeFormat.parse(dateStr)
            } catch (ParseException p) {
            }
        }

        return null
    }

    /**
     * This function is used for format date string to java date with timezone
     * @param date
     * @param timeZone
     * @return
     */

    Date dateFormatter(String date, String timeZone = null) {
        for (String pattern : knowJavaDatePatterns) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern)
                if (timeZone) {
                    dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone))
                }

                return dateFormat.parse(date)
            } catch (ParseException p) {
            }
        }

        return null
    }

    /**
     * This function is used for format java date to any date string format
     * @param date
     * @param pattern
     * @param timeZone
     * @return
     */

    String formatDate(Date date, String pattern = DEFAULT_DATE_FORMAT, String timeZone = null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern)
        if (timeZone) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone))
        }

        return dateFormat.format(date)
    }

    /**
     * This function is used for format date string to any date string format
     * @param dateStr
     * @param pattern
     * @param timeZone
     * @return
     */

    String formatDate(String dateStr, String pattern = DEFAULT_DATE_FORMAT, String timeZone = null) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT)
        if (timeZone) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone))
        }

        Date date = dateFormat.parse(dateStr)

        return date.format(pattern)
    }

    Integer getDayOfWeekend(Date date) {
        return date.getDay()
    }

    /**
     * This function is used only for convert date getting from excel to java date
     * Date in excel is in user's timezone (Ex: UTC+7)
     * This function will convert the date to timezone UTC
     * Ex: 2022-03-20 00:00:00 UTC+7 -> 2022-03-19 17:00:00 UTC
     * @param date
     */

    Date dateConverter(Date date) {
        for (String pattern : knowJavaDatePatterns) {
            try {
                SimpleDateFormat readFormat = new SimpleDateFormat(pattern)
                String dateStr = readFormat.format(date)
                SimpleDateFormat writeFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                writeFormat.setTimeZone(tz)
                return writeFormat.parse(dateStr)
            } catch (ParseException p) {
            }
        }

        return null
    }

    /**
     * This function use only for convert local date gatting from excel to java date
     * Date in excel is in user's timezone (Ex: UTC+7)
     * This function will convert the date to timezone UTC
     * Ex: 2022-03-20 00:00:00 UTC+7 -> 2022-03-19 17:00:00 UTC
     * @param dateTime
     */

    Date dateConverter(LocalDate dateTime) {
        for (String pattern : knowPatterns) {
            try {
                Date date = Date.from(dateTime.atStartOfDay(ZoneId.systemDefault()).toInstant())

                SimpleDateFormat readFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                String dateStr = readFormat.format(date)

                SimpleDateFormat writeFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                writeFormat.setTimeZone(tz)

                return writeFormat.parse(dateStr)
            } catch (Exception ex) {
            }
        }

        return null
    }

    /** This function is used only for convert date getting from excel to java date
     * Date in excel is in user's timezone (Ex: UTC+7)
     * This function will convert the date to timezone UTC
     * Ex: 2022-03-20 00:00:00 UTC+7 -> 2022-03-19 17:00:00 UTC
     * @param date
     */

    Date dateConverter(String date) {
        for (String pattern : knowPatterns) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern)
                dateFormat.setTimeZone(tz)
                return dateFormat.parse(date)
            } catch (ParseException p) {
            }
        }

        return null
    }

    /**
     * This function is used ony for get days between assuredFrom or riskEffectFrom and assuredTo or riskEffectTo
     * Plus 1 second due to expiry date of policy end with 00-00-000 23:59:59
     * @param firstDate
     * @param secondDate
     */

    Integer getDaysBetween(Date firstDate, Date secondDate) {
        Long oneSecondInMilli = 1000
        Long diffInMilli = Math.abs(secondDate.getTime() - firstDate.getTime()) + oneSecondInMilli
        TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS)
    }

    /**
     * This function is used ony for get days between assuredFrom or riskEffectFrom and assuredTo or riskEffectTo
     * Plus 1 second due to expiry date of policy end with 00-00-000 23:59:59
     * @param firstDate
     * @param secondDate
     */

    Integer getDaysBetween(String firstDate, String secondDate) {
        Long oneSecondInMilli = 1000
        Long diffInMilli = Math.abs(dateFormatter(secondDate).getTime() - dateFormatter(firstDate).getTime()) + oneSecondInMilli
        TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS)
    }

    /**
     * This function is used ony for get days between assuredFrom or riskEffectFrom and assuredTo or riskEffectTo
     * Plus 1 second due to expiry date of policy end with 00-00-000 23:59:59
     * @param firstDate
     * @param secondDate
     */

    Integer getDaysBetween(Date firstDate, String secondDate) {
        Long oneSecondInMilli = 1000
        Long diffInMilli = Math.abs(dateFormatter(secondDate).getTime() - firstDate.getTime()) + oneSecondInMilli
        TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS)
    }

    /**
     * This function is used ony for get days between assuredFrom or riskEffectFrom and assuredTo or riskEffectTo
     * Plus 1 second due to expiry date of policy end with 00-00-000 23:59:59
     * @param firstDate
     * @param secondDate
     */

    Integer getDaysBetween(String firstDate, Date secondDate) {
        Long oneSecondInMilli = 1000
        Long diffInMilli = Math.abs(secondDate.getTime() - dateFormatter(firstDate).getTime()) + oneSecondInMilli
        TimeUnit.DAYS.convert(diffInMilli, TimeUnit.MILLISECONDS)
    }

    /**
     * This function use for count weekend's day
     * @param startDate
     * @param endDate
     */

    Integer getWeekendDay(Date startDate, Date endDate) {
        Calendar calendarStart = Calendar.getInstance()
        Calendar calendarEnd = Calendar.getInstance()
        calendarStart.setTime(startDate)
        calendarEnd.setTime(endDate)
        Integer numberOfWeekendDays = 0
        while (calendarStart.before(calendarEnd)) {
            if ((Calendar.SATURDAY == calendarStart.get(Calendar.DAY_OF_WEEK)) || (Calendar.SUNDAY == calendarStart.get(Calendar.DAY_OF_WEEK))) {
                numberOfWeekendDays++
            }
            calendarStart.add(Calendar.DATE, 1)
        }
        return numberOfWeekendDays
    }

    /**
     * This function use for convert Date to year
     */

    def convertDateToYear() {

        def date = Calendar.getInstance().time // Replace this with your actual date variable

        def calendar = Calendar.getInstance()
        calendar.time = date
        def year = calendar.get(Calendar.YEAR)
        return year
    }

    /**
     * Convert  Hour to Minute
     * @param time
     * @return
     */

    def covertHourToMinute(LocalTime time) {
        def result = time.hour * 60 + time.minute
        return result
    }

    /**
     * Convert Minute to Hour
     * @param time
     * @return
     */

    def convertMinuteToHour(Integer minute) {
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm")
        Date minutes = minuteFormat.parse(minute as String)
        minuteFormat = new SimpleDateFormat("HH:mm")
        return minuteFormat.format(minutes)
    }

    /**
     * Function get start date of year
     * @param year
     * @return
     */

    Date getStartOfYear(int year) {
        Calendar calendar = Calendar.getInstance()
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    /**
     * Function get end date of year
     * @param year
     * @return
     */

    Date getEndOfYear(int year) {
        Calendar calendar = Calendar.getInstance()
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

    /**
     * function for change year of birthday
     * @param year
     * @param dob
     * @return
     */

    Date changeYear(Integer year, Date dob) {
        Calendar calendar = Calendar.getInstance()
        calendar.time = dob
        calendar.set(Calendar.YEAR, year)
        return calendar.time
    }

}
