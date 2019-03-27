package ir.asta.samplebot.util.date;

import com.ibm.icu.util.PersianCalendar;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class JalaliCalendar extends Calendar {
    private static final long serialVersionUID = 1L;

    com.ibm.icu.util.Calendar delegate;

    public JalaliCalendar() {
        this(Calendar.getInstance().getTimeZone(), new Locale("fa"));
    }

    @SuppressWarnings("deprecation")
    public JalaliCalendar(TimeZone zone, Locale aLocale) {
        super(zone, aLocale);
        delegate = new PersianCalendar(com.ibm.icu.util.TimeZone.getTimeZone(zone.getID(),
                com.ibm.icu.util.TimeZone.TIMEZONE_JDK), aLocale);
    }

    @Override
    public long getTimeInMillis() {
        return delegate.getTimeInMillis();
    }

    @Override
    public void setTimeInMillis(long millis) {
        delegate.setTimeInMillis(millis);
    }

    @Override
    public final int get(int field) {
        return delegate.get(field);
    }

    @Override
    public final void set(int field, int value) {
        delegate.set(field, value);
    }


    @Override
    public boolean before(Object when) {
        return delegate.before(when);
    }

    @Override
    public boolean after(Object when) {
        return delegate.after(when);
    }

    @Override
    public int getActualMaximum(int field) {
        return delegate.getActualMaximum(field);
    }

    @Override
    public int getActualMinimum(int field) {
        return delegate.getActualMinimum(field);
    }

    @Override
    public final void roll(int field, boolean up) {
        delegate.roll(field, up);
    }

    @Override
    public void roll(int field, int amount) {
        delegate.roll(field, amount);
    }

    @Override
    public void add(int field, int amount) {
        delegate.add(field, amount);
    }

    @Override
    public void setTimeZone(TimeZone value) {
        delegate.setTimeZone(com.ibm.icu.util.TimeZone.getTimeZone(value.getID(), com.ibm.icu.util.TimeZone.TIMEZONE_JDK));
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(delegate.getTimeZone().getID());
    }

    @Override
    public void setLenient(boolean lenient) {
        delegate.setLenient(lenient);
    }

    @Override
    public boolean isLenient() {
        return delegate.isLenient();
    }

    @Override
    public void setFirstDayOfWeek(int value) {
        delegate.setFirstDayOfWeek(value);
    }

    @Override
    public int getFirstDayOfWeek() {
        return delegate.getFirstDayOfWeek();
    }

    @Override
    public void setMinimalDaysInFirstWeek(int value) {
        delegate.setMinimalDaysInFirstWeek(value);
    }

    @Override
    public int getMinimalDaysInFirstWeek() {
        return delegate.getMinimalDaysInFirstWeek();
    }

    @Override
    public final int getMinimum(int field) {
        return delegate.getMinimum(field);
    }

    @Override
    public final int getMaximum(int field) {
        return delegate.getMaximum(field);
    }

    @Override
    public final int getGreatestMinimum(int field) {
        return delegate.getGreatestMinimum(field);
    }

    @Override
    public final int getLeastMaximum(int field) {
        return delegate.getLeastMaximum(field);
    }


    @Override
    protected void computeTime() {
        // this is an empty method
    }
    @Override
    protected void computeFields() {
        // this is an empty method
    }

}
