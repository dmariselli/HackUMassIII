package controller;

import analysis.FacialAnalysis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    FacialAnalysis facialAnalysis = new FacialAnalysis();

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @RequestMapping("/analysis")
    public String analysis(@RequestParam(value="url", required=false) String url, Model model) {
        String[] results = new String[3];
        if (url != null) {
            try {
                results = facialAnalysis.analysis(url);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("MICROSOFT FAILED");
            }
            model.addAttribute("originalURL", results[0]);
            model.addAttribute("hero", "You are " + results[1] + "!");
            model.addAttribute("heroURL", results[2]);
        } else {
            model.addAttribute("originalURL", "http://www.cowboysfans.com/wp-content/uploads/2015/04/Silhouette-question-mark-300x300.jpeg");
            model.addAttribute("hero", "");
            model.addAttribute("heroURL", "http://pictogram-free.com/material/098.png");
        }
        return "analysis";
    }

}