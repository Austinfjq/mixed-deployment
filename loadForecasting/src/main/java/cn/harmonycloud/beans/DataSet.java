package cn.harmonycloud.beans;

import java.util.*;

/**
 * @author wangyuzhong
 * @date 18-12-4 上午10:30
 * @Despriction
 */
public class DataSet extends AbstractCollection<DataPoint> {

    private Collection<DataPoint> dataPoints = new ArrayList<DataPoint>();

    private int periodsPerYear;

    private int timeInterval;

    private double minTime;

    private double maxTime;


    public DataSet()
    {
        periodsPerYear = 1;
        timeInterval = 1;

    }


    public DataSet( DataSet dataSet )
    {
        this( dataSet.getPeriodsPerYear(),
                dataSet.getTimeInterval(),
                dataSet.dataPoints );
    }


    public DataSet( int periodsPerYear, int timeInterval,Collection<DataPoint> c )
    {
        this.periodsPerYear = periodsPerYear;

        this.timeInterval = timeInterval;

        addAll( c );
    }


    public boolean add( DataPoint obj )
    {
        if ( obj == null )
            throw new NullPointerException("DataSet does not support addition of null DataPoints");

        return dataPoints.add( obj );
    }

    public boolean addAll( Collection<? extends DataPoint> c )
    {
        Iterator<?> it = c.iterator();
        while ( it.hasNext() )
            add( (DataPoint)it.next() );

        return true;
    }


    public void clear()
    {
        dataPoints.clear();
    }


    public boolean isEmpty()
    {
        return dataPoints.isEmpty();
    }


    public boolean contains( Object obj )
    {
        if ( obj == null )
            throw new NullPointerException("DataSet does not support null DataPoint objects");

        DataPoint dataPoint = (DataPoint)obj;

        Iterator<DataPoint> it = this.iterator();
        while ( it.hasNext() )
        {
            DataPoint dp = it.next();
            if ( dataPoint.equals(dp) )
                return true;
        }

        return false;
    }

    public boolean containsAll( Collection<?> c )
            throws ClassCastException, NullPointerException
    {
        Iterator<?> it = c.iterator();
        while ( it.hasNext() )
        {
            DataPoint dp = (DataPoint)it.next();
            if ( !this.contains(dp) )
                return false;
        }

        return true;
    }

    public boolean remove( Object obj )
    {
        if ( obj == null )
            throw new NullPointerException("DataSet does not support null DataPoint objects");

        return dataPoints.remove( obj );
    }


    public int size()
    {
        return dataPoints.size();
    }


    public Iterator<DataPoint> iterator()
    {
        return dataPoints.iterator();
    }


    public void sort()
    {

        SortedMap<Double,DataPoint> sortedMap = new TreeMap<Double,DataPoint>(new Comparator<Double>()
        {
            public int compare( Double o1, Double o2 )
            {
                return o1.compareTo(o2);
            }
        } );

        // Add each element in the array list to the sorted map.
        Iterator<DataPoint> it = dataPoints.iterator();
        while ( it.hasNext() )
        {
            // By putting each DataPoint in the list, it will
            // automatically sort them by key
            DataPoint dp = it.next();
            sortedMap.put(
                    new Double(dp.getTimeValue()), dp );
        }

        dataPoints.clear();

        dataPoints.addAll( sortedMap.values() );
    }


    public void setPeriodsPerYear( int periodsPerYear )
    {
        if ( periodsPerYear < 1 )
            throw new IllegalArgumentException( "periodsPerYear parameter must be at least 1" );

        this.periodsPerYear = periodsPerYear;
    }

    public int getPeriodsPerYear()
    {
        return periodsPerYear;
    }

    public void setTimeInterval( int timeInterval )
    {
        if ( timeInterval < 1 )
            throw new IllegalArgumentException( "timeInterval parameter must be at least 1" );

        this.timeInterval = timeInterval;
    }

    public int getTimeInterval()
    {
        return timeInterval;
    }

    public boolean removeAll( Collection<?> c )
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("DataSet.removeAll not yet supported");
    }


    public boolean retainAll( Collection<?> c )
            throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("DataSet.retainAll not yet supported");
    }

//    public int hashCode()
//    {
//        return dataPoints.size()*100
//                + getIndependentVariables().length;
//    }

    public boolean equals( Object obj )
    {
        if ( obj == null )
            return false;

        if ( !(obj instanceof DataSet) )
            return false;

        return this.equals( (DataSet)obj );
    }

    public boolean equals( DataSet dataSet )
    {
        if ( dataSet == null )
            return false;

        if ( this.size() != dataSet.size() )
            return false;

        Iterator<DataPoint> it = dataSet.iterator();
        while ( it.hasNext() )
        {
            DataPoint dataPoint = it.next();
            if ( !this.contains( dataPoint ) )
                return false;
        }

        return true;
    }

    public String toString()
    {
        String lineSeparator = System.getProperty("line.separator");
        String result = "( " + lineSeparator;

        Iterator<DataPoint> it = dataPoints.iterator();
        while ( it.hasNext() )
        {
            result += "  " + it.next().toString()
                    + lineSeparator;
        }

        return result + ")";
    }

    public double getMinTime(){
        Iterator<DataPoint> iterator = this.iterator();
        return iterator.next().getTimeValue();
    }

    public double getMaxTime(){
        Iterator<DataPoint> iterator = this.iterator();

        DataPoint dp = null;

        while(iterator.hasNext()) {
            dp = iterator.next();
        }

        return dp.getTimeValue();
    }

    public void init(){
        this.minTime = getMinTime();
        this.maxTime = getMaxTime();
    }
}


