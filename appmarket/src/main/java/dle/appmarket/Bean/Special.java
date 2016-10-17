package dle.appmarket.Bean;

/**
 * Created by dle on 2016/9/22.
 */
public class Special
{
    private String url;
    private String des;

    public Special(String des, String url)
    {
        this.des = des;
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDes()
    {
        return des;
    }

    public void setDes(String des)
    {
        this.des = des;
    }
}
