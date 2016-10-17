package dle.appmarket.Bean;

/**
 * Created by dle on 2016/9/26.
 */
public class Safe
{
//    safeUrl: "app/com.itheima.www/safeIcon0.jpg",
//    safeDesUrl: "app/com.itheima.www/safedes.jpg",
//    safeDes: "已通过安智市场安全检测，请放心使用",
//    safeDesColor: 0
    private String safeUrl;
    private String safeDesUrl;
    private String safeDes;
    private String safeDesColor;

    public Safe(String safeUrl, String safeDesUrl, String safeDes, String safeDesColor)
    {
        this.safeUrl = safeUrl;
        this.safeDesUrl = safeDesUrl;
        this.safeDes = safeDes;
        this.safeDesColor = safeDesColor;
    }

    public String getSafeUrl()
    {
        return safeUrl;
    }

    public void setSafeUrl(String safeUrl)
    {
        this.safeUrl = safeUrl;
    }

    public String getSafeDesUrl()
    {
        return safeDesUrl;
    }

    public void setSafeDesUrl(String safeDesUrl)
    {
        this.safeDesUrl = safeDesUrl;
    }

    public String getSafeDes()
    {
        return safeDes;
    }

    public void setSafeDes(String safeDes)
    {
        this.safeDes = safeDes;
    }

    public String getSafeDesColor()
    {
        return safeDesColor;
    }

    public void setSafeDesColor(String safeDesColor)
    {
        this.safeDesColor = safeDesColor;
    }
}
