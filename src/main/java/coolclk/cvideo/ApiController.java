package coolclk.cvideo;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import coolclk.cvideo.api.ExtensiveFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("api")
public class ApiController {
    @Data
    public static class User {
        public final static User UNREGISTERED = new User("unknown") {
            @Override
            public boolean isActive() {
                return false;
            }
        };

        private final String id;
        private String nickname;
        private String password;
        private String description;

        private int fans;
        private int follows;

        private User(String id) {
            this.id = id;
        }

        public boolean isActive() {
            return true;
        }

        public String getFormattedValue(Number v) {
            int value = v.intValue();
            if (value >= 100000000) {
                return ((double) (value / 1000000) / 100000) + "亿";
            } else if (value >= 10000) {
                return ((double) (value / 100) / 10) + "万";
            }
            return String.valueOf(value);
        }

        public static User getUser(String id) throws FileNotFoundException {
            if (id != null && !id.isEmpty()) {
                File usersFolder = new File("users");
                if ((usersFolder.mkdirs() || usersFolder.exists()) && usersFolder.isDirectory()) {
                    File[] userFolders = usersFolder.listFiles(filepath -> filepath.isDirectory() && filepath.getName().equals(id));
                    if (userFolders != null && userFolders.length > 0) {
                        File userFolder = userFolders[0];
                        File infoFile = new File(userFolder, "info.json");
                        if (infoFile.exists() && infoFile.isFile()) {
                            Map<String, Object> info = new Gson().fromJson(new JsonReader(new FileReader(infoFile)), Map.class);
                            User user = new User(id);
                            user.setNickname((String) info.getOrDefault("nickname", id));
                            user.setPassword((String) info.getOrDefault("password", ""));
                            user.setDescription((String) info.getOrDefault("description", ""));
                            return user;
                        }
                    }
                }
            }
            return UNREGISTERED;
        }
    }

    @Data
    public static class Video {
        private static final Video NOT_FOUND = new Video("unknown", User.UNREGISTERED) {
            @Override
            public boolean isActive() {
                return false;
            }
        };

        private final String id;
        private final User uploader;
        private String title;
        private String description;
        private Double timestamp;
        private int views;
        private int bullets;
        private int likes;
        private ExtensiveFile videoFolder;
        private ExtensiveFile coverFile;
        private ExtensiveFile videoFile;

        private Video(String id, User uploader) {
            this.id = id;
            this.uploader = uploader;
        }

        public boolean isActive() {
            return true;
        }

        public static Video getVideo(String id) throws FileNotFoundException {
            File videosFolder = new File("videos");
            if ((videosFolder.mkdirs() || videosFolder.exists()) && videosFolder.isDirectory()) {
                File[] videoFolders = videosFolder.listFiles(filepath -> filepath.isDirectory() && filepath.getName().equals(id));
                if (videoFolders != null && videoFolders.length > 0) {
                    File videoFolder = videoFolders[0];
                    File infoFile = new File(videoFolder, "info.json");
                    if (infoFile.exists() && infoFile.isFile()) {
                        Map<String, Object> info = new Gson().fromJson(new JsonReader(new FileReader(infoFile)), Map.class);
                        Video video = new Video(id, info.containsKey("uploader") ? ApiController.User.getUser(String.valueOf(info.get("uploader"))) : User.UNREGISTERED);
                        video.setVideoFolder(new ExtensiveFile(videoFolder.getAbsolutePath()));
                        video.setTitle((String) info.getOrDefault("title", "-"));
                        video.setTimestamp(info.containsKey("timestamp") ? (Double) info.get("timestamp") : null);
                        video.setLikes(((Double) info.getOrDefault("likes", 0)).intValue());
                        video.setViews(((Double) info.getOrDefault("views", 0)).intValue());
                        video.setDescription((String) info.getOrDefault("description", "-"));
                        video.setCoverFile(new ExtensiveFile(videoFolder, String.valueOf(info.getOrDefault("cover", "-"))));
                        video.setVideoFile(new ExtensiveFile(videoFolder, String.valueOf(info.getOrDefault("video", "-"))));
                        return video;
                    }
                }
            }
            return NOT_FOUND;
        }

        public String getFormattedTimestamp() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(this.timestamp.longValue()));
        }

        public String getFormattedValue(Number v) {
            int value = v.intValue();
            if (value >= 100000000) {
                return ((double) (value / 1000000) / 100000) + "亿";
            } else if (value >= 10000) {
                return ((double) (value / 100) / 10) + "万";
            }
            return String.valueOf(value);
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", this.id);
            map.put("uploader", this.uploader);
            map.put("title", this.title);
            map.put("description", this.description);
            map.put("timestamp", this.timestamp);
            map.put("views", this.views);
            map.put("likes", this.likes);
            map.put("bullets", 0);
            if (coverFile.exists()) {
                map.put("cover", coverFile.getName());
            }
            if (videoFile.exists()) {
                map.put("video", videoFile.getName());
            }
            return map;
        }

        public void writeToFiles() throws IOException {
            Map<String, Object> data = this.toMap();
            data.remove("id");
            data.remove("bullets");
            data.put("uploader", ((User) data.getOrDefault("uploader", User.UNREGISTERED)).id);
            String serialization = new Gson().toJson(data, Map.class);
            ExtensiveFile infoFile = new ExtensiveFile(videoFolder, "info.json");
            if (infoFile.exists() || infoFile.createNewFile()) {
                infoFile.writeBytes(serialization.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private final static ApiController INSTANCE = new ApiController();

    public static ApiController getInstance() {
        return INSTANCE;
    }

    @ResponseBody
    @RequestMapping(value = "banner")
    public byte[] getBanner(HttpServletResponse response) throws IOException {
        byte[] request = new byte[0];
        File bannerFolder = new File("banners");
        if ((bannerFolder.mkdirs() || bannerFolder.exists()) && bannerFolder.isDirectory()) {
            File[] bannerFiles = bannerFolder.listFiles();
            if (bannerFiles != null && bannerFiles.length > 0) {
                File bannerFile = bannerFiles[(int) ((Math.random() * bannerFiles.length) - 1)];
                String fileExpandName = bannerFile.getName().substring(bannerFile.getName().lastIndexOf(".")).replace(".", "").toLowerCase();
                response.setContentType("image/" + (fileExpandName.equals("gif") ? "gif" : (fileExpandName.equals("jpg") || fileExpandName.equals("jpeg") ? "jpeg" : "png")));
                try (FileInputStream fis = new FileInputStream(bannerFile)) {
                    request = fis.readAllBytes();
                }
            }
        }
        return request;
    }

    @ResponseBody
    @RequestMapping(value = "video/{id}/file/{filename}", produces = { MediaType.ALL_VALUE })
    public void getVideoFile(HttpServletResponse response, @PathVariable String id, @PathVariable String filename) throws IOException {
        File videosFolder = new File("videos");
        if ((videosFolder.mkdirs() || videosFolder.exists()) && videosFolder.isDirectory()) {
            File[] videoFolders = videosFolder.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals(id));
            if (videoFolders != null && videoFolders.length > 0) {
                File videoFolder = videoFolders[0];
                ExtensiveFile file = new ExtensiveFile(videoFolder, filename);
                response.setContentType("video/*");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(Video.getVideo(id).getTitle(), StandardCharsets.UTF_8) + "_CVideo\"");
                response.setContentLength(file.getInputStream().available());
                response.setHeader("Content-Range", String.valueOf(file.getInputStream().available()));
                response.setHeader("Accept-Ranges", "bytes");
                file.getInputStream().transferTo(response.getOutputStream());
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "video/{id}/info")
    public Map<String, Object> getVideoInfo(@PathVariable String id) throws IOException {
        Video video = Video.getVideo(id);
        if (video.isActive()) {
            Map<String, Object> videoRequest = video.toMap();
            videoRequest.put("cover", "/api/video/" + videoRequest.get("id") + "/file/" + videoRequest.getOrDefault("cover", ""));
            videoRequest.put("video", "/api/video/" + videoRequest.get("id") + "/file/" + videoRequest.getOrDefault("video", ""));
            return videoRequest;
        }
        return new HashMap<>();
    }

    @ResponseBody
    @RequestMapping(value = "videos")
    public Map<String, Object> getVideoList() throws IOException {
        Map<String, Object> request = new HashMap<>();
        List<Map<?, ?>> videosRequest = new ArrayList<>();
        File videosFolder = new File("videos");
        if ((videosFolder.mkdirs() || videosFolder.exists()) && videosFolder.isDirectory()) {
            File[] videoFolders = videosFolder.listFiles(File::isDirectory);
            if (videoFolders != null) {
                for (File videoFolder : videoFolders) {
                    videosRequest.add(this.getVideoInfo(videoFolder.getName()));
                }
            }
            request.put("videos", videosRequest);
        }
        return request;
    }

    @ResponseBody
    @RequestMapping(value = "user/{id}")
    public Map<String, Object> getUserInfo(@PathVariable String id) throws FileNotFoundException {
        Map<String, Object> request = new HashMap<>();
        File usersFolder = new File("users");
        if ((usersFolder.mkdirs() || usersFolder.exists()) && usersFolder.isDirectory()) {
            File userFolder = new File(usersFolder, id);
            if (userFolder.exists() && userFolder.isDirectory()) {
                File infoFile = new File(userFolder, "info.json");
                if (infoFile.exists() && infoFile.isFile()) {
                    request.put("id", id);
                    Map<String, Object> userInfo = new Gson().fromJson(new JsonReader(new FileReader(infoFile)), Map.class);
                    userInfo.remove("password");
                    request.putAll(userInfo);
                }
            }
        }
        return request;
    }
}
