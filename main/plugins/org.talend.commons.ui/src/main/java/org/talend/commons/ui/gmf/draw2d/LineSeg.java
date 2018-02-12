// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.gmf.draw2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;


/**
 * This is a geometric utility class that allows for manipulation of line segments.
 * A line segment is defined as a set of two points where one point is designated as the
 * origin and the other is the terminal.
 * 
 * @author sshaw
 * @canBeSeenBy %partners
 */
public class LineSeg 
    implements Cloneable, java.io.Serializable, Translatable {
    
    final private static int DEFAULT_INTERSECTION_TOLERANCE = 1; 

    private Point origin;
    private Point terminus;
    static final long serialVersionUID = 1;
    
    /**
     * Enumeration class for defining the keypoint along a line segment.  Can be one
     * of <code>ORIGIN</code>, <code>MIDPOINT</code> or <code>TERMINUS</code>.
     */
    static public class KeyPoint {
        private final String name;
        private KeyPoint(String name) { 
            this.name = name; 
        }
        
        public String toString() { return name; }
        
        /**
         * Constant designating the origin point on the line segment.
         */
        public static final KeyPoint ORIGIN = new KeyPoint("origin"); //$NON-NLS-1$
        
        /**
         * Constant designating the mid point on the line segment.
         */
        public static final KeyPoint MIDPOINT = new KeyPoint("midpoint");//$NON-NLS-1$
        
        /**
         * Constant designating the terminal point on the line segment.
         */
        public static final KeyPoint TERMINUS = new KeyPoint("terminus");//$NON-NLS-1$
    }

    /**
     * Enumeration class for defining the orientations of a point relative to the line
     * segment.  The orientations can be one of <code>POSITIVE</code> or 
     * <code>NEGATIVE</code>.
     */
    static public class Sign {
        private final String name;
        private Sign(String name) { 
            this.name = name; 
        }
        
        public String toString() { return name; }
        
        /**
         * Constant designating an orientation that is position relative to the lineseg
         * vector.
         */
        public static final Sign POSITIVE = new Sign("positive");//$NON-NLS-1$
        
        /**
         * Constant designating an orientation that is negative relative to the lineseg
         * vector.
         */
        public static final Sign NEGATIVE = new Sign("negative");//$NON-NLS-1$
    }

    /**
     * Constructor
     * 
     * @param ptStart Point indicating the start of the line segment
     * @param ptEnd Point indicating the end of the line segment
     */
    public LineSeg(Point ptStart, Point ptEnd) {
        origin = ptStart.getCopy();
        terminus = ptEnd.getCopy();
    }

    /**
     * Creates a segment using (fromX, fromY) as either the 
     * first point of the segment (start == Origin) or the midpoint of the 
     * segment (start == Midpoint), and using slope as its new slope and len as
     * the new length.  xdir indicates which direction the segment should
     * go in the x-axis.
     * 
     * @param start <code>KeyPoint</code> from which the other parameters are relative to
     * @param fromX int x value of start <code>KeyPoint</code>
     * @param fromY int y value of start <code>KeyPoint</code>
     * @param slope <code>float</code> slope of the line
     * @param len <code>long</code> length of the line
     * @param xdir direction
     */
    public LineSeg(
        final KeyPoint start,
        final int fromX,
        final int fromY,
        final float slope,
        final long len,
        final int xdir) {
        super();

        origin = new Point();
        terminus = new Point();

        int dx, dy;
        float dx_float;
        double len_squared;

        // Find the delta y and x needed to get to the end points.  See
        // pointOn() for explanation of these equations
        if (start == KeyPoint.ORIGIN) {
            len_squared = (float) len * (float) len;
        } else // start == DirectedLine::Midpoint
            {
            len_squared = len / 2.0 * len / 2.0;
        }

        double slope_squared = slope * slope;
        dx_float = (float) Math.sqrt(len_squared / (slope_squared + 1.0));

        // Set which direction the segment should go in the x direction.
        // The y direction will get set automatically based on slope
        // and the dx.

        dx_float *= xdir;
        dx = (int) (dx_float + 0.5);

        dy = (int) ((slope * dx_float) + 0.5);

        if (start == KeyPoint.ORIGIN) {
            origin.x = fromX;
            origin.y = fromY;
        } else // start == DirectedLine::Midpoint
            {
            origin.x = fromX - dx;
            origin.y = fromY - dy;
        }

        terminus.x = fromX + dx;
        terminus.y = fromY + dy;
    }

    /* 
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object seg) {
        if (!(seg instanceof LineSeg))
            return false;
        
        LineSeg ls = (LineSeg)seg;
        return getOrigin().equals(ls.getOrigin())
            && getTerminus().equals(ls.getTerminus());
    }
    
    /* 
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getOrigin().hashCode() ^ getTerminus().hashCode();
    }
    
    /**
     * Accesssor to retrieve the origin point of the line segement.
     * 
     * @return <code>Point</code> the origin of the line segment.
     */
    public Point getOrigin() {
        return origin.getCopy();
    }

    /**
     * Accesssor to retrieve the terminal point of the line segement.
     * 
     * @return <code>Point</code> the terminating point of the line segment
     */
    public Point getTerminus() {
        return terminus.getCopy();
    }

    /**
     * Sets the origin point of the line segment
     * 
     * @param origin Point to set as origin
     */
    public void setOrigin(Point origin) {
        this.origin = origin.getCopy();
    }

    /**
     * Sets the terminating point of the line segment.
     * 
     * @param terminus Point to set as terminus
     */
    public void setTerminus(Point terminus) {
        this.terminus = terminus.getCopy();
    }

    /**
     * Get points representing the highest point value 
     * for this line segment.
     * 
     * @return <code>Point</code> Representing the highest point value.
     */
    public final Point getSupremum() {
        return new Point(
            Math.max(origin.x, terminus.x),
            Math.max(origin.y, terminus.y));
    }

    /**
     * Get a <code>Point</code> representing the lowest point value 
     * for this line segment.
     * 
     * @return <code>Point</code> Representing the lowest point value.
     */
    public final Point getInfimum() {
        return new Point(
            Math.min(origin.x, terminus.x),
            Math.min(origin.y, terminus.y));
    }
    
    /**
     * Determines if this a horizontal segment
     * 
     * @return <code>boolean</code> <code>true</code> if horizontal, 
     * <code>false</code> otherwise.
     */
    public final boolean isHorizontal() {
        return (origin.y == terminus.y);
    }

    /**
     * Determines if this a vertical segment
     * 
     * @return <code>boolean</code> <code>true</code> if vertical, 
     * <code>false</code> otherwise.
     */
    public final boolean isVertical() {
        return (origin.x == terminus.x);
    }

    /**
     * Constant to avoid divide by zero errors.
     */
    private static final float BIGSLOPE = 9999;

    /**
     * Calculates the slope of this line segment (y=mx+b)
     * 
     * @return <code>float</code> the slope of this segment.  If the slope
     * is not defined such as when the line segment is vertical, then the
     * constant <code>BIGSLOPE</code> is returned to avoid divide by zero
     * errors.
     */
    public final float slope() {
        if (isVertical())
            return BIGSLOPE;

        return (float) (terminus.y - origin.y) / (float) (terminus.x - origin.x);
    }

    /**
     * Calculates the perpendicular slope of this line segment.  This calculates
     * the slope and then inverts it.  Again, to avoid divide by zero errors,
     * the constant <code>BIGSLOPE</code> is returned if the calculated slope
     * before inverting it was zero.
     * 
     * @return <code>float</code> the perpendicular slope value of the 
     * line segment.
     */
    public final float perpSlope() {
        float m = slope();
        if (m == 0.0)
            return BIGSLOPE;
        else
            return - (1.0F / m);
    }

    /**
     * Calculate the length of the line segment.
     * 
     * @return the <code>double</code> length of the line segment.
     */
    public final double length() {
        return getOrigin().getDistance(getTerminus());
    }

    /**
     * Determines the intersect point between this line and the line passed 
     * in as a parameter.  If they intersect, then true is returned and the 
     * point reference passed in will be set to the intersect point.  
     * If they don't intersect, then the method returns <code>false</code>.
     * 
     * @param line <code>LineSeg</code> to test the intersection against.
     * @param nTolerance int tolerance value for detecting the intersection.
     * @return <code>Point</code> that represents the intersection with this line, 
     * or <code>null</code> if the calculation is not possible.
     */
    public Point intersect(
        final LineSeg line,
        final int nTolerance) {
        PointList intersections = getLinesIntersections(line);
        if (intersections.size()>1) {
            intersections.addPoint(getOrigin().getCopy());
            intersections.addPoint(getTerminus().getCopy());
        }
        for (int i=0; i<intersections.size(); i++) {
            Point result = intersections.getPoint(i).getCopy();
            if (containsPoint(result, nTolerance)
                    && line.containsPoint(result, nTolerance)) {
                    return result;
            }
        }
        return null;
    }

    /**
     * Checks if this line segment contains the given point within a tolerance value.
     * 
     * @param aPoint <code>Point</code> to test if contained in this line.
     * @param tolerance int tolerance value for detecting the intersection.
     * 
     * @return <code>boolean</code> <code>true</code> if the given point lies on this 
     * segment, <code>false</code> otherwise.
     */
    public final boolean containsPoint(final Point aPoint, final int tolerance) {
        /*
         * We need perform the calculations in double numbers to avoid possible integer
         * overflows in Point#getDistance2() method
         */
        double lengthOfSegment = origin.getDistance(terminus);
        double lengthFromOriginToPoint = origin.getDistance(aPoint);
        double lengthFromTerminusToPoint = terminus.getDistance(aPoint);
        
        return lengthFromTerminusToPoint + lengthFromOriginToPoint - lengthOfSegment <= tolerance;
    }

    /**
     * Finds the percentage distance along this line segement where the given point
     * resides.
     * 
     * @param coord <code>Point</code> to determine how far along the line segment it resides.
     * @return <code>float</code> the distance along the line segment where the ptCoord is 
     *      in a percentage from.
     */
    public final float distanceAlong(Point coord) {
        int xCoord = coord.x;
        int yCoord = coord.y;

        /*
        Use parametric form for equation of a line segment:
        p + td, where 0 < t < 1 and d = p2 - p (direction vector)
        
        To find out if point lies "inside" line segment (i.e. can
        draw perpendicular line from segment to point), use projection
        of point (q) to line (p + td):
            t = (q-p).d/length(d)^2  (. is dot product)
        */

        /* get the direction vector */
        long dirx = (long) terminus.x - (long) origin.x;
        long diry = (long) terminus.y - (long) origin.y;

        /* get q - p */
        long qpx = (long) xCoord - (long) origin.x;
        long qpy = (long) yCoord - (long) origin.y;

        /* dot product of (q-p) and d */
        long dotprod = qpx * dirx + qpy * diry;

        /* avoid divide by 0 - check if point1 equals point2.  If so,
           there is no segment - return a value which indicates projection
           falls outside the segment. */
        if (dirx == 0 && diry == 0)
            return -1;

        /* length (magnitude) of d is sqrt(dirx^2 + diry^2).  Don't
           bother taking square root since we want the length squared. */
        return ((float) dotprod / (float) (dirx * dirx + diry * diry));
    }

    /**
     * Finds the perpendicular distance from a point coordinates to this line segment.
     * If point is "inside" line segment, then use distance from point to the line, 
     * otherwise use distance to nearest endpoint of segment
     * 
     * @param xCoord the x coordinate of the point.
     * @param yCoord the y coordinate of the point.
     * @return <code>long</code> the distance from the line segment to the given point.
     */
    public final long distanceToPoint(final int xCoord, final int yCoord) {
        
        double proj = projection(xCoord, yCoord);

        if (proj > 0 && proj < 1) {
            Point pt = perpIntersect(xCoord, yCoord);
            return Math.round(pt.getDistance(new Point(xCoord, yCoord)));
        }

        long d1 = Math.round(getOrigin().getDistance(new Point(xCoord, yCoord)));
        long d2 = Math.round(getTerminus().getDistance(new Point(xCoord, yCoord)));
        
        return (d1 < d2 ? d1 : d2);

        /* There are 2 general forms to equations of a line:
          1. y = mx + c, where c = y1 - m(x1), and
          2. ax + by + c = 0
           We know m and c, so putting first version in the form of
           the second version, we get:
          mx - y + c = 0
           So, for form 2, a = m, b = -1, and c = y1 - m(x1).
           Distance from point (x, y) to line (using form 2) is:
          |ax + by + c| / sqrt(a^2 + b^2)
           or
          |mx - y + y1 - m(x1)| / sqrt(m^2 + 1)
        */
    }

    /**
     * Calculates the perpendicular intersection point on 
     * the line segment from the given point.
     * 
     * @param startX the x coordinate of the point
     * @param startY the y coordinate of the point
     * @return <code>Point</code> value containment the perpendicular intersection point.
     */
    public final Point perpIntersect(final int startX, final int startY) {
        float fx;

        // The following equations are based on solving 2 equations with
        // 2 unknowns (x and y).  The 2 equations are equations for the
        // slope of each line segment where the slope and 1 point in the
        // segment are known:
        // (y1 - y) / (x1 - x) = m
        // (sy - y) / (sx - x) = -1/m   (-1/m is slope of perp. line)
        //
        Point ptResult = new Point();
        float m = slope();

        fx =
            (m * startY - m * getOrigin().y + m * m * getOrigin().x + startX)
                / (float) (m * m + 1.0);

        if (m == 0) {
            ptResult.y = getOrigin().y; // segment is horizontal - avoid divide by 0
        } else {
            ptResult.y = (int) (startY + ((startX - fx) / m) + 0.5);
        }

        ptResult.x = Math.round(fx); // add .5 for rounding
        return ptResult;
    }

    /**
     * Calculates the projection of the given point onto the line segment.
     * 
     * @param xCoord the x coordinate of the point.
     * @param yCoord the y coordinate of the point.
     * @return <code>double</code> value of the calculated projection.
     */
    public final double projection(final int xCoord, final int yCoord) {
        /*
            Use parametric form for equation of a line segment:
            p + td, where 0 < t < 1 and d = p2 - p (direction vector)
        
        To find out if point lies "inside" line segment (i.e. can
        draw perpendicular line from segment to point), use projection
        of point (q) to line (p + td):
            t = (q-p).d/length(d)^2  (. is dot product)
        */

        /* get the direction vector */
        long dirx = (long) getTerminus().x - (long) getOrigin().x;
        long diry = (long) getTerminus().y - (long) getOrigin().y;

        /* get q - p */
        long qpx = (long) xCoord - (long) getOrigin().x;
        long qpy = (long) yCoord - (long) getOrigin().y;

        /* dot product of (q-p) and d */
        long dotprod = qpx * dirx + qpy * diry;

        /* avoid divide by 0 - check if point1 equals point2.  If so,
           there is no segment - return a value which indicates projection
           falls outside the segment. */
        if (dirx == 0 && diry == 0)
            return -1.0F;

        /* length (magnitude) of d is sqrt(dirx^2 + diry^2).  Don't
           bother taking square root since we want the length squared. */
        return ((double) dotprod / (double) (dirx * dirx + diry * diry));
    }

    /**
     * Returns out a positive or negative value (Positive / Negative) depending
     * on the orientation of the given point to the line.
     * 
     *              Point on this side: Positive.                
     *                        
     *      P1------------------------------>
     *                  this segment
     *
     *              Point on this side: Negative.
     * 
     * @param rel <code>Point</code> to test the relative position against this line.
     * @return <code>Sign</code> value indicating the relative position of the given point.
     */
    public final Sign positionRelativeTo(Point rel) {
        Ray ptRelRay = new Ray(getOrigin(), rel);

        TrigValues val = getTrigValues(ptRelRay);
        if (val != null) {
            double dNewAngle = Math.atan2(-val.sinTheta, -val.cosTheta);
            if (dNewAngle > 0)
                return Sign.POSITIVE;
        }

        return Sign.NEGATIVE;
    }

    /**
     * Locates a point at a given height and distance along the line segment.
     * 
     *                   B (the point we are looking for)
     *                   +
     *                   |
     *            dist   |h       this segment
     *      P1-----------+------------------->
     *                   A
     *  
     *   get point A (on picture above)
     *
     * @param pctDist <code>double</code> distance along the line
     * @param theHeight <code>long</code> height above the line
     * @param asOriented <code>Sign</code> indicating relative position of the point to be located
     * @return <code>Point</code> value that was located on the line.
     */
    public final Point locatePoint(
        final double pctDist,
        final long theHeight,
        final Sign asOriented) {

        int xdir;
        int dist = (int) (pctDist * length());
        Point pt = new Point();
        pointOn(dist, KeyPoint.ORIGIN, pt); // (x,y) now = A

        // get linesegment AB
        // first determine the direction AB should go in the x axis.  Don't ask-
        // just have faith.

        if (getOrigin().y > getTerminus().y
            || (getOrigin().y == getTerminus().y && getOrigin().x < getTerminus().x)) {
            xdir = (asOriented == Sign.POSITIVE ? -1 : 1);
        } else {
            xdir = (asOriented == Sign.POSITIVE ? 1 : -1);
        }

        LineSeg linesegAB =
            new LineSeg(KeyPoint.ORIGIN, pt.x, pt.y, perpSlope(), theHeight, xdir);

        return (new Point(linesegAB.getTerminus().x, linesegAB.getTerminus().y));
    }

    /**
     * Gets the point on the line segment at the given distance away from the 
     * key point.
     * 
     * @param theDistance <code>long</code> distance along the line
     * @param fromKeyPoint <code>KeyPoint</code> to calculate the distance from
     * @param ptResult <code>Point</code> where the resulting calculating value is stored.
     * @return <code>boolean</code> <code>true</code> if point can be calculated, 
     * <code>false</code> otherwise.
     */
    public final boolean pointOn(
        final long theDistance,
        final KeyPoint fromKeyPoint,
        Point ptResult) {
        float m, dx_float;
        int dx, dy, startX = 0, startY = 0, otherX = 0, otherY = 0;

        // Set the point to offset from and the other point used to determine
        // which direction dx and dy should be applied to get a point on the
        // line.

        if (fromKeyPoint == KeyPoint.ORIGIN) {
            startX = getOrigin().x;
            startY = getOrigin().y;
            otherX = getTerminus().x;
            otherY = getTerminus().y;
        }
        else if (fromKeyPoint == KeyPoint.TERMINUS) {
            startX = getTerminus().x;
            startY = getTerminus().y;
            otherX = getOrigin().x;
            otherY = getOrigin().y;
        }
        else if (fromKeyPoint == KeyPoint.MIDPOINT) {
            startX = (getOrigin().x + getTerminus().x) / 2;
            startY = (getOrigin().y + getTerminus().y) / 2;
            otherX = getTerminus().x;
            otherY = getTerminus().y;
        }
        else {
            return false;
        }

        m = slope(); // get the slope of this line

        // Find dx and dy - the delta x and y to get from the endpoint to the
        // point on the line at the specified distance away.
        // The following is based on solving 2 equations with 2 unknowns:
        //   dy/dx = m    (m is slope of line)
        //   dy^2 + dx^2 = dist^2
        //
        double d_squared = (float) theDistance * (float) theDistance;
        double m_squared = m * m;

        // Add .5 so result is rounded to nearest integer when cast
        dx_float = (float) Math.sqrt(d_squared / (m_squared + 1.0));
        dx = (int) (dx_float + 0.5);
        dy = (int) (Math.sqrt(d_squared * m_squared / (m_squared + 1.0)) + 0.5);

        /* negative distance means we want point off the line */
        if (theDistance < 0) {
            dx = -dx;
            dy = -dy;
        }

        ptResult.x = ((startX > otherX) ? startX - dx : startX + dx);
        ptResult.y = ((startY > otherY) ? startY - dy : startY + dy);
        boolean in_line;
        if (startX > otherX)
            in_line = ptResult.x >= otherX;
        else
            in_line = ptResult.x <= otherX;
        if (in_line) {
            if (startY > otherY)
                in_line = ptResult.y >= otherY;
            else
                in_line = ptResult.y <= otherY;
        }
        return in_line;
    }

    /**
     * Structure to hold onto trig values that represent an angle
     * @author sshaw
     */
    static public class TrigValues {
        
        /**
         * Sin theta value
         */
        public double sinTheta;
        
        
        /**
         * Cos theta value.
         */
        public double cosTheta;
    }

    /**
     * Gets the trig values associated with the angle from this line segment
     * to the given vector.
     * 
     * @param ptToVector <code>Ray</code> value to calculate trig values of.
     * @return <code>TrigValues</code> object representing the trigonometry values
     * for the angle of the passed in <code>Ray</code> relative to <code>this</code>
     * or null if calculation is not possible, 
     */
    public TrigValues getTrigValues(final Ray ptToVector) {
        double dFromLength = length();
        double dToLength = ptToVector.length();

        Ray ptFromVector = new Ray(getOrigin(), getTerminus());

        if (dFromLength <= 0 || dToLength <= 0) {
            return null;
        }

        // 1. find angle for ptToVector relative to the origin.
        double dAlpha;
        double dCosAlpha, dSinAlpha;

        dCosAlpha = ptFromVector.x / dFromLength;
        dSinAlpha = ptFromVector.y / dFromLength;
        dAlpha = Math.atan2(dSinAlpha, dCosAlpha);

        // 2. inverse the angle to get the rotation
        dCosAlpha = Math.cos(-dAlpha);
        dSinAlpha = Math.sin(-dAlpha);

        // 3. rotate vector 2 by angle above so that it's angle relative to vector 1 can
        // be calculated
        double dRotateX = (ptToVector.x * dCosAlpha) - (ptToVector.y * dSinAlpha);
        double dRotateY = (ptToVector.x * dSinAlpha) + (ptToVector.y * dCosAlpha);

        // 4. Now calculate the Theta trig values
        TrigValues val = new TrigValues();
        val.cosTheta = dRotateX / dToLength;
        val.sinTheta = dRotateY / dToLength;

        return val;
    }

    /**
     * Returns a new <code>LineSeg</code> that is parallel to this by the given distance.  
     * Orientation is relative to the start and end.  Negative implies to the 
     * left and Position implies to the right.
     * 
     * @param ptLoc <code>Point</code> value to constrain the line to.
     * @return <code>LineSeg</code> line that was calculated going through the given point
     */
    public final LineSeg getParallelLineSegThroughPoint(Point ptLoc) {
        if (isHorizontal()) {
            return new LineSeg(new Point(getOrigin().x, ptLoc.y), new Point(getTerminus().x, ptLoc.y));
        }
        else if (isVertical()) {
            return new LineSeg(new Point(ptLoc.x, getOrigin().y), new Point(ptLoc.x, getTerminus().y));
        }
        else {
            Point ptProj = perpIntersect(ptLoc.x, ptLoc.y);
            long nHeight = Math.round(ptProj.getDistance(ptLoc));
            Sign position = positionRelativeTo(ptLoc);
        
            return new LineSeg(
                 locatePoint(0.0, nHeight, position),
                 locatePoint(1.0, nHeight, position));
        }
    }
        
    /**
     * Returns the coefficients of the generalized equation of the line passing through
     * points (x1,y1) and (x2,y2)
     * Generalized line equation: ax+by=c => a==result[0], b==result[1], c==result[2]
     * 
     * @param x1 - x coordinate of the 1st point
     * @param y1 - y coordinate of the 1st point
     * @param x2 - x coordinate of the 2nd point
     * @param y2 - y coordinate of the 2nd point
     * @return the coefficients of the generalized equation of the line passing through
     * points (x1,y1) and (x2,y2)
     */
    public static double [] getLineEquation(double x1, double y1, double x2, double y2) {
        double equation[] = new double[3];
        for (int i=0; i<3; i++)
            equation[i]=0;
        
        if (x1 == x2 && y1 == y2)
            return equation;
        
        if (x1 == x2) {
            equation[0]=1;
            equation[1]=0;
            equation[2]=x1;
            return equation;
        }
        
        equation[0]=(y1-y2)/(x2-x1);
        equation[1]=1.0;
        equation[2]=y2+equation[0]*x2;
        return equation;
    }

    /**
     * Returns array with 3 numbers in it, which are the coefficients of the
     * generalized line equation of the line corresponding to this line segment
     * a*x+b*y=c is the equation => result[0]=a, result[1]=b, result[2]=c
     * 
     * @return an array with 3 numbers in it, which are the coefficients of the
     * generalized line equation
     */
    public double [] getEquation() {
        PrecisionPoint preciseOrigin = new PrecisionPoint(origin);
        PrecisionPoint preciseTerminus = new PrecisionPoint(terminus);
        return getLineEquation(preciseOrigin.preciseX, preciseOrigin.preciseY,
                preciseTerminus.preciseX, preciseTerminus.preciseY);
    }
    
    /**
     * Returns intersection points of two lines that contain this line segment and
     * the argumet line segment.
     * The list of intersection points may contain at most two points and will contain
     * 2 points if and only if the lines are equal. The 2 points will be the end points
     * of the parameter line segment 
     * 
     * @param line - the line segment
     * @return intersection points of two lines that contain this line segment and
     * the argumet line segment.
     */
    public PointList getLinesIntersections (LineSeg line) {
        PrecisionPointList intersections = new PrecisionPointList();
        double temp[] = getEquation();
        double a1 = temp[0];
        double b1 = temp[1];
        double c1 = temp[2];

        temp = line.getEquation();
        double a2 = temp[0];
        double b2 = temp[1];
        double c2 = temp[2];
        // Cramer's rule for the system of linear equations
        double det = a1*b2 - b1*a2;
        if (det == 0) {
            if (a1==a2 && b1==b2 && c1==c2) {
                List<Point> points = new ArrayList<Point>(4);
                points.add(getOrigin());
                points.add(getTerminus());
                points.add(line.getOrigin());
                points.add(line.getTerminus());
                Collections.sort(points, new Comparator<Point>() {
                    public int compare(Point arg0, Point arg1) {
                        if (arg0.equals(arg1)) {
                            return 0;
                        } else if (arg0.preciseX() < arg1.preciseX()
                                || (arg0.preciseX() == arg1.preciseX() && arg0
                                        .preciseY() < arg1.preciseY())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }                   
                });
                // if lines are the same, then instead of infinite number of intersections
                // we will put the 2 points out of 4 points - ends of 2 segments. Look above.
                intersections.addPoint(points.get(1));
                intersections.addPoint(points.get(2));
            }
        }
        else {
            intersections.addPoint(new PrecisionPoint((c1*b2-b1*c2)/det, (a1*c2-c1*a2)/det));
        }
        return intersections;
    }
    
    /**
     * Calculates intersection points of the line of the line segment and ellipse 
     * 
     * @param ellipseBounds - width and height of the ellipse
     * @return - <Code>PointList</Code> containing all intersection points
     */
    public PointList getLineIntersectionsWithEllipse(Rectangle ellipseBounds) {
        PointList intersections = new PrecisionPointList();
        PrecisionPoint preciseOrigin = new PrecisionPoint(origin);
        PrecisionPoint preciseTerminus = new PrecisionPoint(terminus);
        PrecisionRectangle preciseEllipseBounds = new PrecisionRectangle(ellipseBounds);
        if (preciseEllipseBounds.preciseWidth == 0 || preciseEllipseBounds.preciseHeight == 0)
            return intersections;
        PrecisionPoint ellipsePreciseCenter = new PrecisionPoint(
                preciseEllipseBounds.getCenter());
        double xl1 = preciseOrigin.preciseX - ellipsePreciseCenter.preciseX;
        double xl2 = preciseTerminus.preciseX - ellipsePreciseCenter.preciseX;
        double yl1 = preciseOrigin.preciseY - ellipsePreciseCenter.preciseY;
        double yl2 = preciseTerminus.preciseY - ellipsePreciseCenter.preciseY;
        double [] equation = LineSeg.getLineEquation(xl1, yl1, xl2, yl2);
        
        if (equation.length<3 || (equation[0] == 0 && equation[1] == 0))
            return intersections;
        
        double a = equation[0];
        double b = equation[1];
        double c = equation[2];
        double w = preciseEllipseBounds.preciseWidth;
        double h = preciseEllipseBounds.preciseHeight;
        
        // Ellipse with a center at the origin has an equation:
        // (h*x)^2+(w*y)^2=(h*w/2)^2
        // Line equation: a*x+b*y=c
        
        if (b==0) {
            // b==0 is a special case since in general case we will express
            // y in terms of x, i.e. we need to divide by b, which should not
            // be 0
            // b==0 => a*x=c +> x=c/a;
            double x = c/a;
            // y^2 = (h/2)^2-((h*c)/(a*w))^2
            double y = Math.pow(h/2,2)-Math.pow((h*c)/(a*w),2);
            if (y<0)
                return intersections;
            intersections.addPoint(new PrecisionPoint(x+ellipsePreciseCenter.preciseX, Math.sqrt(y)+ellipsePreciseCenter.preciseY));
            intersections.addPoint(new PrecisionPoint(x+ellipsePreciseCenter.preciseX, -Math.sqrt(y)+ellipsePreciseCenter.preciseY));
        }
        else {
            // y = (c-a*x)/b => we get quadratic equation for x
            // x^2*(h^2+(w*a/b)^2)-x*(2*w^2*a*c)/(b^2)+((w*c/b)^2-(h*w/2)^2)=0 or
            // x^2*xA+x*xB+xC=0
            double xA = Math.pow(h,2)+Math.pow((w*a)/b,2);
            double xB = (-2)*Math.pow(w,2)*a*c/Math.pow(b,2);
            double xC = Math.pow(w*c/b,2)-Math.pow(h*w/2,2);
            double xD = Math.pow(xB,2)-4*xA*xC;
            
            if (xD<0)
                return intersections;
            
            double x1 = (-xB+Math.sqrt(xD))/(2*xA);
            double x2 = (-xB-Math.sqrt(xD))/(2*xA);
            intersections.addPoint(new PrecisionPoint(x1+ellipsePreciseCenter.preciseX, (c-a*x1)/b+ellipsePreciseCenter.preciseY));
            intersections.addPoint(new PrecisionPoint(x2+ellipsePreciseCenter.preciseX, (c-a*x2)/b+ellipsePreciseCenter.preciseY));
        }
        
        return intersections;
    }
    
    /**
     * Calculates intersection points of the line that contains this line segment with
     * a list of other line segments. If the list of points (line segments) form a closed
     * <Code>PolyLine</Code>, i.e form a closed polygon figure, then the method will
     * claculate intersections of a line and a figure
     * 
     * @param points - list of points that form linesegments, i.e the <Code>PolyLine</Code>
     * @return the intersection points of the line that contains this line segment with
     * a list of other line segments.
     */
    public PointList getLineIntersectionsWithLineSegs(final PointList points) {
        PointList intersections = new PrecisionPointList();
        if (points.size()<2) {
            if (containsPoint(points.getFirstPoint(), DEFAULT_INTERSECTION_TOLERANCE)) {
                intersections.addPoint(points.getFirstPoint());
            }
        } else {    
            for (int i=0; i<points.size()-1; i++) {
                LineSeg seg = new LineSeg(points.getPoint(i), points.getPoint(i+1));
                PointList currentIntersections = getLinesIntersections(seg);
                for (int j=0; j<currentIntersections.size(); j++) {
                    Point intersection = currentIntersections.getPoint(j);
                    if (seg.containsPoint(intersection, DEFAULT_INTERSECTION_TOLERANCE)) {
                        intersections.addPoint(currentIntersections.getPoint(j));
                    }
                }
            }
        }
        return intersections;
    }

    /* 
     * (non-Javadoc)
     * @see org.eclipse.draw2d.geometry.Translatable#performScale(double)
     */
    public void performScale(double factor) {
        setOrigin(getOrigin().scale(factor));
        setTerminus(getTerminus().scale(factor));
    }

    /* 
     * (non-Javadoc)
     * @see org.eclipse.draw2d.geometry.Translatable#performTranslate(int, int)
     */
    public void performTranslate(int dx, int dy) {
        setOrigin(getOrigin().translate(dx, dy));
        setTerminus(getTerminus().translate(dx, dy));
    }
    
}
