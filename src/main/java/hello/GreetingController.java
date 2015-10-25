package hello;

import Analysis.FacialAnalysis;
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
    public String analysis(@RequestParam(value="url", required=true) String url, Model model) {
        String result = facialAnalysis.analysis(url);
        model.addAttribute("url", result);
        return "analysis";
    }

}