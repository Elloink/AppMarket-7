package dle.appmarket.Bean;

/**
 * Created by dle on 2016/9/29.
 */
public class Home
{
    private String id;
    private String name;
    private String packgeName;
    private String iconUrl;
    private String stars;
    private String size;
    private String downloadUrl;
    private String des;
    private Boolean isDown;

    public Home(String id, String name, String packgeName, String iconUrl, String stars, String size, String
            downloadUrl, String des,Boolean isDown)
    {
        this.id = id;
        this.name = name;
        this.packgeName = packgeName;
        this.iconUrl = iconUrl;
        this.stars = stars;
        this.size = size;
        this.downloadUrl = downloadUrl;
        this.des = des;
        this.isDown = isDown;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPackgeName()
    {
        return packgeName;
    }

    public void setPackgeName(String packgeName)
    {
        this.packgeName = packgeName;
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

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }

    public String getDes()
    {
        return des;
    }

    public void setDes(String des)
    {
        this.des = des;
    }

    public Boolean getDown()
    {
        return isDown;
    }

    public void setDown(Boolean down)
    {
        isDown = down;
    }
}
