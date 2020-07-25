package org.zreock.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class CommonContoller {
	
	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model mode) {
		
		log.info( "access Denied : " + auth );

		mode.addAttribute("msg", "Access Denied");
	}
	
	@GetMapping("/customLogin")
	public void loginInput( String error, String logout, Model model ) {
		
		log.info( "error : " + error );
		log.info( "logout : " + logout );
		
		if ( error != null ) {
			model.addAttribute("error", "Login Error Check your Account" );
		}
		
		if ( logout != null ) {
			model.addAttribute("logout", "Logout!!!");
		}
		
		
	}
}
