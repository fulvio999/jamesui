package org.apache.james.jamesui.backend.authentication.manager;



import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
	
/**
* Spring MVC controller that handle all the requests incoming at JamesUI
* (Thanks to https://github.com/xpoft/vaadin-samples for the solution) 
*/
@Controller
@RequestMapping("/")
public class LoginController
{
	@RequestMapping(value = "/", method = RequestMethod.GET)
	/* "error" is a param in the url insert by Spring security (See security.xml) */
	public String login(@RequestParam(value = "error", defaultValue = "false", required = false)Boolean isError, ModelMap model)
	{
		if (isError)
		{			
			model.put("isError", isError); /* "isError"  key used in login.ftl page */			
		}
		
		return "login"; /* login.ftl */
	}
}


