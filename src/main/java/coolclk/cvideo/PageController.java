package coolclk.cvideo;

import coolclk.cvideo.api.ExtensiveFile;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.File;
import java.io.IOException;

@Controller
public class PageController {
    @RequestMapping(value = "video/{id}")
    public ModelAndView videoHandler(HttpServletResponse response, @PathVariable String id) throws IOException {
        ModelAndView modelAndView = new ModelAndView("video.html");
        ApiController.Video videoObject = ApiController.Video.getVideo(id);
        if (videoObject.isActive()) {
            videoObject.setViews(videoObject.getViews() + 1);
            videoObject.writeToFiles();
            modelAndView.addObject("video", videoObject);
        } else {
            response.sendRedirect("/error/404");
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "licence")
    public String licenceHandler() throws IOException {
        return new ExtensiveFile("licence.html").getContent();
    }

    @RequestMapping(value = "search/{type}")
    public ModelAndView searchHandler(HttpServletResponse response, @PathVariable String type, @RequestParam String keyword) throws IOException {
        ModelAndView modelAndView = new ModelAndView("search.html");
        return modelAndView;
    }

    @RequestMapping(value = "error/{code}")
    public ModelAndView errorHandler(@PathVariable String id) {
        return new ModelAndView("error.html");
    }
}
