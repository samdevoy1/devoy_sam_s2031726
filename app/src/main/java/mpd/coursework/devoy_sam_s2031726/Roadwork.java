package mpd.coursework.devoy_sam_s2031726;

import java.util.Date;

public class Roadwork {

    private String title;
    private String description;


    public Roadwork() {
        title = "";
        description = "";


        //pubDate = new Date();
    }

    public Roadwork(String atitle, String adescription, String alink, String ageorrsPoint, Date apubDate) {
        title = atitle;
        description = adescription;
        //link = alink;
        //georrsPoint = ageorrsPoint;

        //pubDate = apubDate;
    }

    //Getters
    public String getTitle() { return title; }
    public String getDescription() {return description;}
    //public String getLink() { return link; }
    //public String getGeorrsPoint() { return georrsPoint; }
    //public Date getPubDate() { return pubDate; }

    //Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    //public void setLink(String link) { this.link = link; }
    //public void setGeorrsPoint(String georrsPoint) { this.georrsPoint = georrsPoint; }
    //public void setPubDate(Date pubDate) { this.pubDate = pubDate; }


}



