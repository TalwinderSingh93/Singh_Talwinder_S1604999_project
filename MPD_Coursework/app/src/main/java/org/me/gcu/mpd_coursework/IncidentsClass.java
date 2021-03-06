//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/
//\ NAME        =                 Talwinder
//\ SURNAME     =                   Singh
//\ ID NUMBER   =                 S1604999
//\ DATE        =                 17/04/2020
//\ MODULE      =       Mobile Platform Development(MHI322959-19-B)
//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/

package org.me.gcu.mpd_coursework;

import java.util.Date;

public class IncidentsClass
{
    private String szTitle;
    private String szDescription;
    private String szLink;
    private String szGeoLocation;
    private double dLatitude;
    private double dLongitude;
    private String szAuthor;
    private String szComments;
    private Date pPubDate;
    private String szPubDate;

    //Constructor
    public IncidentsClass()
    {
        szTitle = "";
        szDescription = "";
        szLink = "";
    }

    //Construtor
    public IncidentsClass(String abolt,String awasher,String anut)
    {
        szTitle = abolt;
        szDescription = awasher;
        szLink = anut;
    }

    //Function to set the title of the incident
    public void setTitle(String title)
    {
        szTitle = title;
    }

    public void setDescription(String description)
    {
        szDescription = description;
    }

    public void setLink(String link)
    {
        szLink = link;
    }

    public void setGeoLocationV2(String geoLocation)
    {
        szGeoLocation = geoLocation;
    }

    public void setGeoLocation(double latitude, double longitude)
    {
        dLatitude = latitude;
        dLongitude = longitude;
    }

    public void setAuthor(String author)
    {
        szAuthor = author;
    }

    public void setComment(String comment)
    {
        szComments = comment;
    }

    public void setPubDate(Date pubDate)
    {
        pPubDate = pubDate;
    }

    public void setPubDateV2(String pubDate)
    {
        szPubDate = pubDate;
    }




    public String getTitle()
    {
        return szTitle;
    }

    public String getDescription()
    {
        return szDescription;
    }

    public String getLink()
    {
        return szLink;
    }

    public double getGeoLocationLat()
    {
        return dLatitude;
    }

    public double getGeoLocationLong()
    {
        return dLongitude;
    }

    public String getGeoLocation()
    {
        return szGeoLocation;
    }

    public String getAuthor()
    {
        return szAuthor;
    }

    public String getComment()
    {
        return szComments;
    }

    public String getPubDate()
    {
        return szPubDate;
    }
}

