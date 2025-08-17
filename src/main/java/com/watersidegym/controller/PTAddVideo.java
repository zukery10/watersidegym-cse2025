package com.watersidegym.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.File;
import java.time.LocalDate;
import java.util.*;

@ManagedBean(name = "videoControllerBean")
@ApplicationScoped
public class PTAddVideo {

    public static class Video {
        private int videoID;
        private String title;
        private String link;     // "/resources/videos/Name.mp4"
        private String date;     // "YYYY-MM-DD"
        private String description;
        private String rule;     // "Member"/"Non-Member"
        private String category; // "Recently Added", "Yoga", ...

        public Video(int videoID, String title, String link, String date,
                     String description, String rule, String category) {
            this.videoID = videoID; this.title = title; this.link = link;
            this.date = date; this.description = description;
            this.rule = rule; this.category = category;
        }
        public int getVideoID() { return videoID; }  public void setVideoID(int v) { this.videoID = v; }
        public String getTitle() { return title; }   public void setTitle(String v) { this.title = v; }
        public String getLink() { return link; }     public void setLink(String v) { this.link = v; }
        public String getDate() { return date; }     public void setDate(String v) { this.date = v; }
        public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
        public String getRule() { return rule; }     public void setRule(String v) { this.rule = v; }
        public String getCategory() { return category; } public void setCategory(String v) { this.category = v; }
    }

    private final List<Video> videos = new ArrayList<>();
    private int nextId = 1;

    @PostConstruct
    public void preload() {
        addPreloaded(new Video(nextId++,"Tai Chi","/resources/videos/Jimmi_Chan_Tai%20chi.mp4","2025-08-12","Calming Tai Chi.","Non-Member","Recently Added"));
        addPreloaded(new Video(nextId++,"Yoga","/resources/videos/Francine_Cipollone_Yoga.mp4","2025-08-12","Gentle flow.","Non-Member","Recently Added"));
        addPreloaded(new Video(nextId++,"Core Strength","/resources/videos/Kaleigh_Cohen_Core_Strength.mp4","2025-08-12","Core routine.","Member","Recently Added"));
        addPreloaded(new Video(nextId++,"CrossFit","/resources/videos/CrossFit_Dan%20Bailey.mp4","2025-08-12","Coach intensity.","Member","Yoga"));
        addPreloaded(new Video(nextId++,"Aqua Aerobics","/resources/videos/Pooja_Aqua_Aerobics.mp4","2025-08-12","Low impact.","Non-Member","Yoga"));
        addPreloaded(new Video(nextId++,"Kickboxing","/resources/videos/Kickboxing_Rebecca_Louise.mp4","2025-08-12","Kickboxing burn.","Member","Yoga"));
        addPreloaded(new Video(nextId++,"Tabata","/resources/videos/Anna_Tabata.mp4","2025-08-12","Tabata session.","Non-Member","Yoga"));
        addPreloaded(new Video(nextId++,"Pilates","/resources/videos/Rachel_Lawrence_pilates.mp4","2025-08-12","Pilates basics.","Member","Yoga"));
        addPreloaded(new Video(nextId++,"Zumba","/resources/videos/Zumba_Holly_Corrente.mp4","2025-08-12","Zumba cardio.","Non-Member","Yoga"));

        syncFromFolder(); // optional; needs Servlet API (provided scope)
        sortNewestFirst();
    }
    private void addPreloaded(Video v) { videos.add(v); }
    private void sortNewestFirst() {
        videos.sort(Comparator.comparing(Video::getDate, Comparator.nullsLast(String::compareTo)).reversed());
    }

    public void syncFromFolder() {
        try {
            ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String base = sc.getRealPath("/resources/videos");
            if (base == null) return;
            File[] files = new File(base).listFiles((d,n)->n.toLowerCase().endsWith(".mp4")||n.toLowerCase().endsWith(".mov"));
            if (files == null) return;
            for (File f : files) {
                String link = "/resources/videos/" + f.getName().replace(" ","%20");
                boolean exists = false;
                for (Video v : videos) if (link.equalsIgnoreCase(v.getLink())) { exists = true; break; }
                if (!exists) {
                    String title = f.getName().replaceFirst("\\.[^.]+$","");
                    add(title, link, java.time.LocalDate.now().toString(), "Auto-imported", "Non-Member", "Recently Added");
                }
            }
        } catch (Exception ignored) {}
    }

    public List<Video> getVideos() { return videos; }
    public List<Video> getRecentlyAdded() { return getByCategory("Recently Added"); }
    public List<Video> getYogaOnly() { return getByCategory("Yoga"); }
    public List<Video> getByCategory(String cat) {
        List<Video> out = new ArrayList<>();
        for (Video v: videos) if (v.getCategory()!=null && v.getCategory().equalsIgnoreCase(cat)) out.add(v);
        out.sort(Comparator.comparing(Video::getDate, Comparator.nullsLast(String::compareTo)).reversed());
        return out;
    }

    public boolean deleteIfExists(int id) {
        Iterator<Video> it = videos.iterator();
        while (it.hasNext()) if (it.next().getVideoID()==id) { it.remove(); return true; }
        return false;
    }
    public Video add(String title, String link, String date, String desc, String rule, String category) {
        Video v = new Video(nextId++, title, link, date, desc, rule, category);
        videos.add(v); sortNewestFirst(); return v;
    }
    public String rescanAndStay() { syncFromFolder(); return null; }

    // Form fields (pure JSF)
    private String formTitle, formLink, formDate, formDescription, formRule="Non-Member", formCategory="Recently Added";
    public String addFromForm() {
        if (formTitle!=null && formLink!=null && !formTitle.isEmpty() && !formLink.isEmpty())
            add(formTitle, formLink, formDate, formDescription, formRule, formCategory);
        formTitle=formLink=formDate=formDescription=null; formRule="Non-Member"; formCategory="Recently Added";
        return "video-gallery.xhtml?faces-redirect=true";
    }
    public String getFormTitle(){return formTitle;} public void setFormTitle(String v){formTitle=v;}
    public String getFormLink(){return formLink;}   public void setFormLink(String v){formLink=v;}
    public String getFormDate(){return formDate;}   public void setFormDate(String v){formDate=v;}
    public String getFormDescription(){return formDescription;} public void setFormDescription(String v){formDescription=v;}
    public String getFormRule(){return formRule;}   public void setFormRule(String v){formRule=v;}
    public String getFormCategory(){return formCategory;} public void setFormCategory(String v){formCategory=v;}
}
