package com.example.android.quakereport;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private String mLocationDistace;
    private String mLocationRegion;
    private String mUrlString;
    private long mDate;
    private boolean hasDistance;
    public Earthquake(double mag, String loc, long date, String url) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        mMagnitude = mag;
        mLocation = loc;
        mDate = date;
        mUrlString = url;
        separateLocationStrings();
    }
    public double getmMagnitude() { return mMagnitude; }
    public String getmLocation() { return mLocation; }
    private void separateLocationStrings() {
        String separator1 = " of ";
        String separator2 = " the ";
        if (mLocation.contains(separator1)) {
            compileLocationString(separator1);
            hasDistance = true;
        }
        else if (mLocation.contains(separator2)) {
            compileLocationString(separator2);
            hasDistance = true;
        } else {
            mLocationRegion = mLocation;
            hasDistance = false;
        }
    }
    private void compileLocationString(String separator) {
        String[] stringGroup = mLocation.split(separator);
        mLocationDistace = stringGroup[0] + separator;
        mLocationRegion = stringGroup[1];
    }
    public String getLocationDistance() { return mLocationDistace; }
    public String getLocationRegion() { return mLocationRegion; }
    public long getmDate() { return mDate; }
    public String getmDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");      // Retrieved from: https://stackoverflow.com/questions/9754600/converting-epoch-time-to-date-string
        return sdf.format(new Date(mDate));
    }
    public String getUrl() { return mUrlString; }
    public boolean hasDistance() { return hasDistance; }
}