package dle.appmarket.Bean;

/**
 * Created by dle on 2016/9/21.
 */
public class AppInfo
{
    private String url;
    private String name;
    private String parents;

    public AppInfo(String parents, String url, String name)
    {
        this.parents = parents;
        this.url = url;
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getParents()
    {
        return parents;
    }

    public void setParents(String parents)
    {
        this.parents = parents;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
