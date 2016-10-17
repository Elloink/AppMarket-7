package dle.appmarket.Bean;

import java.io.Serializable;

/**
 * Created by dle on 2016/9/26.
 */
public class AboutAppinfo implements Serializable
{
    private String appname;
    private String iconUrl;
    private String stars;
    private String downloadNum;
    private String version;
    private String date;
    private String size;
    private String downloadurl;
    private String des;
    private String author;

    public String getAppname()
    {
        return appname;
    }

    public void setAppname(String appname)
    {
        this.appname = appname;
    }

    public String getIconUrl()
    {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl)
    {
        this.iconUrl = iconUrl;
    }

    public String getStars()
    {
        return stars;
    }

    public void setStars(String stars)
    {
        this.stars = stars;
    }

    public String getDownloadNum()
    {
        return downloadNum;
    }

    public void setDownloadNum(String downloadNum)
    {
        this.downloadNum = downloadNum;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getDownloadurl()
    {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl)
    {
        this.downloadurl = downloadurl;
    }

    public String getDes()
    {
        return des;
    }

    public void setDes(String des)
    {
        this.des = des;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }
}
