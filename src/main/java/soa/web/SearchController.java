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

        // We add all the query less the last word unless there's only one.
        String search = args[0];
        int lastIdx = args.length - 1;
        for(int i = 1; i < lastIdx; i++) {
            search += " " + args[i];
        }

        // If the last word is another normal word to search, we add it to the
        // query. If not, we set the info for the max param.
        if(lastIdx != 0) {
            if(args[lastIdx].startsWith("max:")) {
                String[] max = args[lastIdx].split(":");
                if(max[0].equals("max")) {
                    params.put("CamelTwitterCount", max[1]);
                }
            } else {
                search += " " + args[lastIdx];
            }
        }

        params.put("CamelTwitterKeywords",search);


        return producerTemplate.requestBodyAndHeaders("direct:search", "", params);
    }
}