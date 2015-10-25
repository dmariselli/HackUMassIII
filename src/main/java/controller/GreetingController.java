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
        String result = null;
        if (url != null) {
            try {
                result = facialAnalysis.analysis(url);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("MICROSOFT FAILED");
            }
            model.addAttribute("url", result);
        }
        return "analysis";
    }

}