package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Controller
public class SearchController {

	@Autowired
	  private ProducerTemplate producerTemplate;

	@RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        String[] args = q.split(" ");

        Map<String,Object> params = new HashMap<>();

        String search = args[0];
        for(int i = 1; i < args.length -1; i++) {
            search += " " + args[i];
        }

        params.put("CamelTwitterKeywords",search);

        String[] max = args[1].split(":");
        if(max[0].equals("max")) {
            params.put("CamelTwitterCount", max[1]);
        }
        return producerTemplate.requestBodyAndHeaders("direct:search", "", params);
    }
}